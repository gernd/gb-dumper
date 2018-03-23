(defproject gb-dumper "0.1.0-SNAPSHOT"
  :description "A simple GB ROM dumper for the command line"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot gb-dumper.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
