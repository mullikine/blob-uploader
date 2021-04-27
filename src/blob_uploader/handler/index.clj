(ns blob-uploader.handler.index
  (:require [ataraxy.core :as ataraxy]
            [ataraxy.response :as response]
            [blob-uploader.views.index :as views.index]
            [integrant.core :as ig]))

(defmethod ig/init-key :blob-uploader.handler/index [_ _]
  (fn [{[_] :ataraxy/result}]
    [::response/ok (views.index/list-options)]))
