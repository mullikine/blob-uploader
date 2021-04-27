(ns blob-uploader.views.index
  (:require [blob-uploader.views.template :refer [page]]))

(defn list-options []
  (page
    [:div.container.jumbotron.bg-white.text-center
     [:row
      [:p
       [:a.btn.btn-primary {:href "/add-film"} "Add a Film"]]]
     [:row
      [:p
       [:a.btn.btn-primary {:href "/list-films"} "List Films"]]]
     [:row
      [:p
       [:a.btn.btn-primary {:href "/find-by-name"} "Search Films"]]]]))