(defproject clinical-trials-importer "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [http-kit "2.1.18"]
                 [org.clojure/data.json "0.2.5"]
                 [clj-time "0.8.0"]
                 [org.clojure/tools.logging "0.3.0"]
                 [crossref-util "0.1.8"]
                 [enlive "1.1.5"]
                 [crossref-util "0.1.8"]
                 [org.clojure/data.xml "0.0.8"]]
  :main ^:skip-aot clinical-trials-importer.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
