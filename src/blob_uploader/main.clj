(ns blob-uploader.main
  (:gen-class)
  (:require [clojure.java.io :as io]
            [duct.core :as duct]))

(duct/load-hierarchy)

(defn -main [& args]
  (let [keys (or (duct/parse-keys args) [:duct/migrator :duct/daemon])]
    (-> (duct/read-config (io/resource "blob_uploader/config.edn"))
        (duct/prep keys)
        (duct/exec keys))))
