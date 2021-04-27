(ns blob-uploader.views.index
  (:require [blob-uploader.views.template :refer [page]]))

(defn list-options []
  (page
    [:div.container.jumbotron.bg-white.text-center
     [:row
      [:p
       [:a.btn.btn-primary {:href "/add-blob"} "Add a Film"]]]
     [:row
      [:p
       [:a.btn.btn-primary {:href "/list-blobs"} "List Films"]]]
     [:row
      [:p
       [:a.btn.btn-primary {:href "/find-by-name"} "Search Films"]]]]))
