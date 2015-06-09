(ns clinical-trials-importer.core
  (:require [clinical-trials-importer.importers.isrctn :as isrctn]
            [clinical-trials-importer.importers.plosapi :as plosapi]
            )
  (:gen-class))

(defn -main
  [& args]
  (isrctn/import-all)  
  (plosapi/import-all))
