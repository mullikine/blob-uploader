(ns blob-uploader.handler.index-test
  (:require [blob-uploader.handler.index]
            [clojure.test :refer [deftest testing is]]
            [ring.mock.request :as mock]
            [integrant.core :as ig]))


(deftest check-index-handler
  (testing "Ensure that the index handler returns two links for add and list blobs"
    (let [handler (ig/init-key :blob-uploader.handler/index {})
          response (handler (mock/request :get "/"))]
      (is (= :ataraxy.response/ok (first response)))
      (is (= "href=\"/add-blob\""
            (re-find #"href=\"/add-blob\"" (second response))))
      (is (= "href=\"/list-blobs\""
            (re-find #"href=\"/list-blobs\"" (second response)))))))
