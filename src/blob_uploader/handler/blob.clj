(ns blob-uploader.handler.blob
  (:require [ataraxy.core :as ataraxy]
            [ataraxy.response :as response]
            [blob-uploader.boundary.blob :as boundary.blob]
            [blob-uploader.views.blob :as views.blob]
            [integrant.core :as ig]))

(defmethod ig/init-key :blob-uploader.handler.blob/show-create [_ _]
  (fn [_]
    [::response/ok (views.blob/create-blob-view)]))

(defn- blob-form->blob
  [blob-form]
  (as-> blob-form blob
    (dissoc blob "__anti-forgery-token")
    (reduce-kv (fn [m k v] (assoc m (keyword k) v))
               {}
               blob)
    (update blob :rating #(Integer/parseInt %))))

(defmethod ig/init-key :blob-uploader.handler.blob/create [_ {:keys [db]}]
  (fn [{[_ blob-form] :ataraxy/result :as request}]
    (let [blob (blob-form->blob blob-form)
          result (boundary.blob/create-blob db blob)
          alerts (if (:id result)
                   {:messages ["Film added"]}
                   result)]
      [::response/ok (views.blob/blob-view blob alerts)])))

(defmethod ig/init-key :blob-uploader.handler.blob/list [_ {:keys [db]}]
  (fn [_]
    (let [blobs-list (boundary.blob/list-blobs db)]
      (if (seq blobs-list)
       [::response/ok (views.blob/list-blobs-view blobs-list {})]
       [::response/ok (views.blob/list-blobs-view [] {:messages ["No blobs found."]})]))))

(defmethod ig/init-key :blob-uploader.handler.blob/show-search [_ _]
  (fn [_]
    [::response/ok (views.blob/search-blob-by-name-view)]))

(defmethod ig/init-key :blob-uploader.handler.blob/find-by-name [_ {:keys [db]}]
  (fn [{[_ search-form] :ataraxy/result :as request}]
    (let [name (get search-form "name")
          blobs-list (boundary.blob/fetch-blobs-by-name db name)]
      (if (seq blobs-list)
        [::response/ok (views.blob/list-blobs-view blobs-list {})]
        [::response/ok (views.blob/list-blobs-view [] {:messages [(format "No blobs found for %s." name)]})]))))
