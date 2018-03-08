(ns gb-dumper.core
  (:gen-class))

(defn print-usage
  []
  (println "Usage: executable <PATH-TO-GB-ROM>"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (if (not (= 1 (count args)))
    (print-usage)
    (println "Analyzing GB Rom File")))
