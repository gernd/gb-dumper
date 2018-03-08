(ns gb-dumper.core
  (:gen-class))

(defn print-usage
  "Prints usage information on howto use the GB ROM Analyzer"
  []
  (println "Usage: executable <PATH-TO-GB-ROM>"))

(defn read-rom
  [path-to-rom]
  with-open [in (input-stream (file path-to-rom))]
  (let [buf (byte-array 1000)
        n (.read in buf)]
    (println "Read" n "bytes.")))

(defn -main
  "Analyzes the GB ROM file given as the first parameter "
  [& args]
  (if (not (= 1 (count args)))
    (print-usage)
    (let [file-name (first args)]
    (do (println "Analyzing GB Rom File " file-name)
        
        )))
