(ns gb-dumper.analyze-test
  (:require [clojure.test :refer :all]
            [gb-dumper.analyze :refer :all])
  (:import (java.nio ByteBuffer)))


(defn prepare-test-data
  "Prepares test ROM data and returns it as byte[]"
  []
  (let [test-rom-size 8
        byte-buffer (ByteBuffer/allocate test-rom-size)
        buf (byte-array test-rom-size)]
    (.put byte-buffer (.byteValue 0x12))                    ; rst$00
    (.put byte-buffer (.byteValue 0x33))                    ; rst$08
    (.put byte-buffer (.byteValue 0x00))                    ; rst$10
    (.put byte-buffer (.byteValue 0x12))                    ; rst$18
    (.put byte-buffer (.byteValue 0x13))                    ; rst$20
    (.put byte-buffer (.byteValue 0x03))                    ; rst$28
    (.put byte-buffer (.byteValue 0x24))                    ; rst$30
    (.put byte-buffer (.byteValue 0x42))                    ; rst$38
    (.flip byte-buffer)
    (.get byte-buffer buf)
    buf))

(deftest test-unpack-rom-data-restart-addresses-correct
  (testing "Checks that the restart adresses are correctly read from the ROM"
    (let [test-rom-data (prepare-test-data)
          unpacked-rom-data (unpack-rom-data test-rom-data)]
      (do (println "Test ROM data is" (to-hex-string test-rom-data))
          (is (= 0x12 (get unpacked-rom-data :rst$00)))
          (is (= 0x33 (get unpacked-rom-data :rst$08)))
          (is (= 0x00 (get unpacked-rom-data :rst$10)))
          (is (= 0x12 (get unpacked-rom-data :rst$18)))
          (is (= 0x13 (get unpacked-rom-data :rst$20)))
          (is (= 0x03 (get unpacked-rom-data :rst$28)))
          (is (= 0x24 (get unpacked-rom-data :rst$30)))
          (is (= 0x42 (get unpacked-rom-data :rst$38)))))))
