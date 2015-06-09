(ns clinical-trials-importer.ext
  (:require [clojure.data.json :as json])
  (:require [org.httpkit.client :as http])
  (:require [clojure.data.json :as json])
  (:require [crossref.util.config :refer [config]]))


(defn insert
  [subject subject-type predicate object object-type provenance]
  
  (prn "SAVE" subject subject-type predicate object object-type provenance)
  (let [response @(http/post (:links-store-url config) {:headers {"Content-Type" "application/json"}
                                         :body (json/write-str {:subject subject
                                                        :subject-type subject-type
                                                        :predicate predicate
                                                        :object object
                                                        :object-type object-type
                                                        :provenance provenance})})]
  (when (not= 201 (:status response))
    (prn response))))