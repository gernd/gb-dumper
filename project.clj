(defproject gb-dumper "0.1.0-SNAPSHOT"
  :description "A simple GB ROM dumper for the command line"
  :url "https://github.com/gernd/gb-dumper"
  :license {:name "MIT License"
            :url "LICENSE.md"}
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :main ^:skip-aot gb-dumper.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
