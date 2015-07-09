(ns clinical-trials-importer.importers.isrctn-site
  (:require [clinical-trials-importer.ext :as ext])
  (:require [clojure.data.json :as json])
  (:require [org.httpkit.client :as http])
  (:require [net.cgrand.enlive-html :as html])
  (:require [crossref.util.doi :as cr-doi]))

(def works-base "http://api.crossref.org/works")

; The container title is "http://isrctn.org/>". This is a typo, but it's what's in the API currently.
(def isrctn-filter "container-title:http://isrctn.org/>")

(def page-size 20)

; e.g. http://dx.doi.org/10.1186/ISRCTN21800480
(defn dois-from-isrctn-page [url]
  (prn "Scrape " url)
  (let [page @(http/get url)
        text (:body page)
        resource (html/html-resource (java.io.StringReader. text))
        links (filter #(= (-> % :content first) "Publisher Full Text") (html/select resource [:a]))
        dois (map #(-> % :attrs :href) links)]
    (map cr-doi/non-url-doi dois)))

(defn import-all
  []
  (let [initial-query @(http/request {:url works-base
                                      :method :get
                                      :query-params {:filter isrctn-filter :rows 0}})
        body (json/read-str (:body initial-query) :key-fn keyword)
        num-works (-> body :message :total-results)
        num-pages (inc (quot num-works page-size))]
    
    ; Each page
    (doseq [page-num (range num-pages)]
      (prn "Page" page-num)
      (let [offset (* page-size page-num)
            page-query @(http/request {:url works-base
                             :method :get
                             :query-params {:filter isrctn-filter
                                            :rows page-size
                                            :offset offset}})
        body (json/read-str (:body page-query) :key-fn keyword)
        works (-> body :message :items)]
      
        ; Each work
        (doseq [work works]
          (let [doi (:DOI work)
                potential-isrctn (second (.split doi "/"))
                full-doi (cr-doi/normalise-doi doi)]
            (prn "DOI" doi)
            (if (.startsWith potential-isrctn "isrctn")
              (do
                (ext/insert doi "doi" "represents" potential-isrctn "isrctn" "isrctn-crossref-metadata")
                (doseq [citing-doi (dois-from-isrctn-page full-doi)]
                  (ext/insert citing-doi "doi" "cites" potential-isrctn "isrctn" "isrctn-crossref-metadata")))
              
              (prn "No match:" potential-isrctn))))))))

