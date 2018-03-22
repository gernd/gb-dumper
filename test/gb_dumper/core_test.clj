(ns gb-dumper.core-test
  (:require [clojure.test :refer :all]
            [gb-dumper.analyze :refer :all])
  (:import (java.nio ByteBuffer)))


(defn prepare-test-data
  "Prepares test ROM data and returns it as byte[]"
  []
  (let [byte-buffer (ByteBuffer/allocate 1)
        buf (byte-array 1)]
    (.put byte-buffer (.byteValue 0x12))                    ; rst$00
    (.flip byte-buffer)
    (.get byte-buffer buf)
    buf))

(deftest test-read-$00-address
  (testing "Checks that the restart $00 adress is correctly read from the ROM"
    (let [test-rom-data (prepare-test-data)]
      (do (println "Test ROM data is " (to-hex-string test-rom-data))
          (is (= 0x12 (get (unpack-rom-data test-rom-data) :rst$00)))))))
