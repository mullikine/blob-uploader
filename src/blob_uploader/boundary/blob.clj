(ns blob-uploader.boundary.blob
  (:require [clojure.java.jdbc :as jdbc]
            duct.database.sql)
  (:import java.sql.SQLException))

(defprotocol BlobDatabase
  (list-blobs [db])
  (fetch-blobs-by-name [db name])
  (create-blob [db blob]))

(extend-protocol BlobDatabase
  duct.database.sql.Boundary
  (list-blobs [{db :spec}]
    (jdbc/query db ["SELECT * FROM blob"]))
  (fetch-blobs-by-name [{db :spec} name]
    (let [search-term (str "%" name "%")]
      (jdbc/query db ["SELECT * FROM blob WHERE LOWER(name) like LOWER(?)" search-term])))
  (create-blob [{db :spec} blob]
    (try
     (let [result (jdbc/insert! db :blob blob)]
       (if-let [id (val (ffirst result))]
         {:id id}
         {:errors ["Failed to add blob."]}))
     (catch SQLException ex
       {:errors [(format "Blob not added due to %s" (.getMessage ex))]}))))
