(ns gb-dumper.core
  (:gen-class))

(require '(clojure.java [io :as io]))

(defn print-usage
  "Prints usage information on howto use the GB ROM Analyzer"
  []
  (println "Usage: executable <PATH-TO-GB-ROM>"))


(defn read-rom
  "Reads the given ROM into a byte array"
  [file]
  (with-open [xin (io/input-stream file)
              xout (java.io.ByteArrayOutputStream.)]
    (io/copy xin xout)
    (.toByteArray xout)))

(defn -main
  "Analyzes the GB ROM file given as the first parameter "
  [& args]
  (if (not (= 1 (count args)))
    (print-usage)
    (let [file-name (first args)]
    (do (println "Analyzing GB Rom File " file-name)
        (let [rom-binary-data (read-rom file-name)]
        (println "rom binary data " rom-binary-data))))))
