(ns clinical-trials-importer.util
  (:require [clojure.edn :as edn]
            [clojure.java.io :refer [reader resource input-stream]]
            [clojure.string :as string]))

(def namespaces
  "List of namespaces"
  (-> "clinical-trial-registries.edn" resource slurp edn/read-string))

(def namespaces-by-type
  "Map type to lsit of namespaces"
  (group-by :type namespaces))

(def namespaces-by-id
  (into {} (map (fn [nspace] [(:id nspace) nspace]) namespaces)))

; (def regex (re-pattern (str "(?:" (apply str (interpose "|" (keys prefixes))) ")" separator "[0-9-/a-zA-Z]+")))
(def regex (re-pattern (str "(?:" (apply str (interpose "|" (map :regular-expression-relaxed namespaces))) ")")))

(defn extract-cts
  "Take a string and try and extract clinical trial IDs and types."
  [input-string]
  (let [input (.toLowerCase input-string)
        tokens (re-seq regex input)
        with-types (map (fn [token]
                          (map #(when (re-matches (re-pattern (:regular-expression-relaxed %)) token)
                                  (let [[cleanup-re cleanup-replacement] (:regular-expression-cleanup %)
                                        ; Clean up if there's a re.
                                        cleaned (if cleanup-re
                                                    (string/replace token (re-pattern cleanup-re) cleanup-replacement)
                                                    token)]
                                    ; Now try and pass the cleaned-up token.
                                    (when (re-matches (re-pattern (:regular-expression-strict %)) cleaned)
                                      [[cleaned (:id %)]])))
                               namespaces)) tokens)
        ; Often duplicates.
        result (set (remove nil? (apply concat with-types)))]
    result))
  
(doseq [nspace namespaces]
  (doseq [[input-string expected] (:test nspace)]
    (let [output (extract-cts input-string)
          ; Each test case includes only one identifier.
          expected-output #{[[expected (:id nspace)]]}]
      
      (when (not= output expected-output)
        (prn "ERROR MATCHING")
        (prn "INPUT" input-string)
        (prn "EXPECTED" expected-output)
        (prn "ACTUAL" output)))))