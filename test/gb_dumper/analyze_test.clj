(ns gb-dumper.analyze-test
  (:require [clojure.test :refer :all]
            [gb-dumper.analyze :refer :all])
  (:import (java.nio ByteBuffer)))

(def scrolling-nintendo-graphic
  (byte-array
    [
     0xCE 0xED 0x66 0x66 0xCC 0x0D 0x00 0x0B 0x03 0x73 0x00 0x83 0x00 0x0C 0x00 0x0D
     0x00 0x08 0x11 0x1F 0x88 0x89 0x00 0x0E 0xDC 0xCC 0x6E 0xE6 0xDD 0xDD 0xD9 0x99
     0xBB 0xBB 0x67 0x63 0x6E 0x0E 0xEC 0xCC 0xDD 0xDC 0x99 0x9F 0xBB 0xB9 0x33 0x3E
     ]))

(defn prepare-test-data
  "Prepares test ROM data and returns it as byte[]"
  []
  (let [test-rom-size (+ 17 (count scrolling-nintendo-graphic))
        byte-buffer (ByteBuffer/allocate test-rom-size)
        buf (byte-array test-rom-size)]
    (.put byte-buffer (.byteValue 0x12))                    ; rst$00
    (.put byte-buffer (.byteValue 0x33))                    ; rst$08
    (.put byte-buffer (.byteValue 0x00))                    ; rst$10
    (.put byte-buffer (.byteValue 0x12))                    ; rst$18
    (.put byte-buffer (.byteValue 0x13))                    ; rst$20
    (.put byte-buffer (.byteValue 0x03))                    ; rst$28
    (.put byte-buffer (.byteValue 0x24))                    ; rst$30
    (.put byte-buffer (.byteValue 0x99))                    ; rst$38
    (.put byte-buffer (.byteValue 0x01))                    ; vertical blank interrupt
    (.put byte-buffer (.byteValue 0x02))                    ; lcdc state interrupt
    (.put byte-buffer (.byteValue 0x03))                    ; timer overflow interrupt
    (.put byte-buffer (.byteValue 0x04))                    ; serial transfer complete interrupt
    (.put byte-buffer (.byteValue 0x05))                    ; high to low interrupt
    (.put byte-buffer (.byteValue 0x12))                    ; code execution point (4 bytes)
    (.put byte-buffer (.byteValue 0x13))                    ; code execution point (4 bytes)
    (.put byte-buffer (.byteValue 0x21))                    ; code execution point (4 bytes)
    (.put byte-buffer (.byteValue 0x70))                    ; code execution point (4 bytes)
    (.put byte-buffer scrolling-nintendo-graphic 0 (count scrolling-nintendo-graphic))
    (.flip byte-buffer)
    (.get byte-buffer buf)
    buf))

(defn is-same-byte
  "Checks that the byte value of two expressions is the same"
  [first-byte second-byte]
  (is (= (.byteValue first-byte) (.byteValue second-byte))))

(deftest test-unpack-rom-data-restart-addresses-correct
  (testing "Checks that the restart adresses are correctly read from the ROM"
    (let [test-rom-data (prepare-test-data)
          unpacked-rom-data (unpack-rom-data test-rom-data)]
      (do (println "Test ROM data is" (to-hex-string test-rom-data) " unpacked ROM data is " unpacked-rom-data)
          (is-same-byte 0x12 (get unpacked-rom-data :rst$00))
          (is-same-byte 0x33 (get unpacked-rom-data :rst$08))
          (is-same-byte 0x00 (get unpacked-rom-data :rst$10))
          (is-same-byte 0x12 (get unpacked-rom-data :rst$18))
          (is-same-byte 0x13 (get unpacked-rom-data :rst$20))
          (is-same-byte 0x03 (get unpacked-rom-data :rst$28))
          (is-same-byte 0x24 (get unpacked-rom-data :rst$30))
          (is-same-byte 0x99 (get unpacked-rom-data :rst$38))))))

(deftest test-unpack-rom-data-interrupt-addresses-correct
  (testing "Checks that the interrupt adresses are correctly read from the ROM"
    (let [test-rom-data (prepare-test-data)
          unpacked-rom-data (unpack-rom-data test-rom-data)]
      (do (println "Test ROM data is" (to-hex-string test-rom-data) " unpacked ROM data is " unpacked-rom-data)
          (is-same-byte 0x01 (get unpacked-rom-data :vertical-blank-interrupt))
          (is-same-byte 0x02 (get unpacked-rom-data :lcdc-status-interrupt))
          (is-same-byte 0x03 (get unpacked-rom-data :timer-overflow-interrupt))
          (is-same-byte 0x04 (get unpacked-rom-data :serial-transfer-completion-interrupt))
          (is-same-byte 0x05 (get unpacked-rom-data :high-to-low-interrupt))))))

(deftest test-unpack-rom-data-start-opcodes-correct
  (testing "Checks that the start opcodes are correctly read from the ROM"
    (let [test-rom-data (prepare-test-data)
          unpacked-rom-data (unpack-rom-data test-rom-data)]
      (do (println "Test ROM data is" (to-hex-string test-rom-data) " unpacked ROM data is " unpacked-rom-data)
          (is-same-byte 0x12 (nth (get unpacked-rom-data :start-opcodes) 0))
          (is-same-byte 0x13 (nth (get unpacked-rom-data :start-opcodes) 1))
          (is-same-byte 0x21 (nth (get unpacked-rom-data :start-opcodes) 2))
          (is-same-byte 0x70 (nth (get unpacked-rom-data :start-opcodes) 3))))))
