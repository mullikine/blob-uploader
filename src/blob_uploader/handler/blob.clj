(ns blob-uploader.handler.blob
  (:require [ataraxy.core :as ataraxy]
            [ataraxy.response :as response]
            [blob-uploader.boundary.blob :as boundary.film]
            [blob-uploader.views.blob :as views.film]
            [integrant.core :as ig]))

(defmethod ig/init-key :blob-uploader.handler.blob/show-create [_ _]
  (fn [_]
    [::response/ok (views.blob/create-film-view)]))

(defn- blob-form->film
  [blob-form]
  (as-> blob-form film
    (dissoc blob "__anti-forgery-token")
    (reduce-kv (fn [m k v] (assoc m (keyword k) v))
               {}
               blob)
    (update blob :rating #(Integer/parseInt %))))

(defmethod ig/init-key :blob-uploader.handler.blob/create [_ {:keys [db]}]
  (fn [{[_ blob-form] :ataraxy/result :as request}]
    (let [blob (film-form->film film-form)
          result (boundary.blob/create-film db film)
          alerts (if (:id result)
                   {:messages ["Film added"]}
                   result)]
      [::response/ok (views.blob/film-view film alerts)])))

(defmethod ig/init-key :blob-uploader.handler.blob/list [_ {:keys [db]}]
  (fn [_]
    (let [blobs-list (boundary.film/list-films db)]
      (if (seq blobs-list)
       [::response/ok (views.blob/list-films-view films-list {})]
       [::response/ok (views.blob/list-films-view [] {:messages ["No films found."]})]))))

(defmethod ig/init-key :blob-uploader.handler.blob/show-search [_ _]
  (fn [_]
    [::response/ok (views.blob/search-film-by-name-view)]))

(defmethod ig/init-key :blob-uploader.handler.blob/find-by-name [_ {:keys [db]}]
  (fn [{[_ search-form] :ataraxy/result :as request}]
    (let [name (get search-form "name")
          blobs-list (boundary.film/fetch-films-by-name db name)]
      (if (seq blobs-list)
        [::response/ok (views.blob/list-films-view films-list {})]
        [::response/ok (views.blob/list-films-view [] {:messages [(format "No films found for %s." name)]})]))))
