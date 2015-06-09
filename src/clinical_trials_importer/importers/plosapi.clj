(ns clinical-trials-importer.importers.plosapi
  (:require [clinical-trials-importer.ext :as ext]
            [clinical-trials-importer.util :as util])
  (:require [clojure.data.json :as json])
  (:require [org.httpkit.client :as http])
  (:require [net.cgrand.enlive-html :as html])
  (:require [crossref.util.doi :as cr-doi])
  (:require [clojure.data.xml :as xml]))


(def clinical-trial-filter-q "trial_registration:*")
(def clinical-trial-filter-fq "doc_type:full")
(def clinical-trial-filter-fl "id,title_display,trial_registration")


; &start=1&rows=15

(def api-base "http://api.plos.org/search")

(def page-size 20)



(defn import-all
  []
  (let [initial-query @(http/request {:url api-base
                                      :method :get
                                      :query-params {:q clinical-trial-filter-q
                                                     :fq clinical-trial-filter-fq
                                                     :fl clinical-trial-filter-fl
                                                     :rows 0}})
        text (:body initial-query)
        resource (html/html-resource (java.io.StringReader. text))
        num-found (-> (html/select resource [:result]) first :attrs :numfound Integer/parseInt)
        num-pages (inc (quot num-found page-size))]
    (prn "Num results:" num-found)
    
    ; Each page
    (doseq [page-num (range num-pages)]
      ; (prn "Page" page-num "/" num-pages)
      (let [offset (* page-size page-num)
            page-query @(http/request {:url api-base
                             :method :get
                             :query-params {:q clinical-trial-filter-q
                                            :fq clinical-trial-filter-fq
                                            :fl clinical-trial-filter-fl
                                            :rows page-size
                                            :start offset}})
            text (:body page-query)
            resource (html/html-resource (java.io.StringReader. text))
            items (html/select resource [:doc])]
              
        ; Each work
        (doseq [item items]
          ; (prn "item" item)
          (let [strs (html/select item [:str])
                arrs (html/select item [:arr])
                doi (->
                      (filter #(= (-> % :attrs :name) "id") strs)
                      first
                      :content
                      first)
                ; This will be something like ClinicalTrials.gov NCT01841229
                trial-regs (->>
                      (filter #(= (-> % :attrs :name) "trial_registration") arrs)
                      first
                      :content
                      (map #(-> % :content first)))]
            
            (doseq [trial trial-regs]
              (let [trial-numbers (util/extract-cts trial)]
                (doseq [[number type] trial-numbers]
                  (prn doi number (name type))
                  (ext/insert doi "doi" "cites" number (name type) "plos-api"))))))))))
