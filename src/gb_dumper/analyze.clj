(ns gb-dumper.analyze
  (:import (java.nio ByteBuffer)))

(defn to-hex-string
  "Convert a seq of bytes into a hex encoded string"
  [bytes]
  (apply str "0x" (for [byte bytes] (format "%02x" byte))))

(defn unpack-rom-data
  "unpacks the gb rom data given as byte array"
  [rom-data]
  (let [byte-buffer (ByteBuffer/allocate (count rom-data))]
    (.put byte-buffer rom-data 0 (count rom-data))          ; fill byte buffer with rom data
    (.flip byte-buffer)                                     ; reset pointer for reading
    (let [rst$00 (.get byte-buffer)
          rst$08 (.get byte-buffer)
          rst$10 (.get byte-buffer)
          rst$18 (.get byte-buffer)
          rst$20 (.get byte-buffer)
          rst$28 (.get byte-buffer)
          rst$30 (.get byte-buffer)
          rst$38 (.get byte-buffer)]
      {:rst$00 rst$00 :rst$08 rst$08 :rst$10 rst$10 :rst$18 rst$18
       :rst$20 rst$20 :rst$28 rst$28 :rst$30 rst$30 :rst$38 rst$38})))
