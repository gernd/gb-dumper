(ns gb-dumper.analyze
  (:import (java.nio ByteBuffer)))

(defn to-hex-string
  "Convert a seq of bytes into a hex encoded string"
  [bytes]
  (apply str (for [byte bytes] (format "%02x" byte))))

(defn unpack-rom-data
  "unpacks the gb rom data given as byte array"
  [rom-data]
  (let [byte-buffer (ByteBuffer/allocate (count rom-data))]
    (.put byte-buffer rom-data 0 (count rom-data))          ; fill byte buffer with rom data
    (.flip byte-buffer)                                     ; reset pointer for reading
    (let [rst$00 (.get byte-buffer 0)]
      {:rst$00 rst$00})))
