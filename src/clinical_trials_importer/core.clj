(ns clinical-trials-importer.core
  (:require [clinical-trials-importer.importers.isrctn-site :as isrctn-site]
            [clinical-trials-importer.importers.plosapi :as plosapi]
            )
  (:gen-class))

(defn -main
  [& args]
  ; (isrctn-site/import-all)  
  ; (plosapi/import-all)
  
  )
