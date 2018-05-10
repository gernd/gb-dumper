(ns gb-dumper.analyze
  (:import (java.nio ByteBuffer)
           (java.nio.charset Charset)))

(def expected-logo-graphic
  (byte-array
    [
     0xCE 0xED 0x66 0x66 0xCC 0x0D 0x00 0x0B 0x03 0x73 0x00 0x83 0x00 0x0C 0x00 0x0D
     0x00 0x08 0x11 0x1F 0x88 0x89 0x00 0x0E 0xDC 0xCC 0x6E 0xE6 0xDD 0xDD 0xD9 0x99
     0xBB 0xBB 0x67 0x63 0x6E 0x0E 0xEC 0xCC 0xDD 0xDC 0x99 0x9F 0xBB 0xB9 0x33 0x3E
     ]))

(defn to-hex-string
  "Convert a seq of bytes into a hex encoded string"
  [bytes]
  (apply str "0x" (for [byte bytes] (format "%02x" byte))))

(defn unpack-bytes
  "unpacks the given number of bytes from a ByteBuffer as vector of bytes"
  [byte-buffer number-of-bytes]
  (let [byte-arr (byte-array number-of-bytes)]
    (.get byte-buffer byte-arr)
    byte-arr))

(defn unpack-start-opcodes
  "unpacks the start opcodes from the ROM given as ByteBuffer"
  [rom-data-buffer]
  (unpack-bytes rom-data-buffer 4))

(defn unpack-logo-graphic
  "unpacks the start logo graphic from the ROM given as ByteBuffer"
  [rom-data-buffer]
  (unpack-bytes rom-data-buffer 48))

(defn unpack-eight-bytes
  "unpacks eight bytes from the ROM given as ByteBuffer"
  [rom-data-buffer]
  (unpack-bytes rom-data-buffer 8))

(defn unpack-name
  "unpacks the name for the ROM from the given ByteBuffer"
  [rom-data-buffer]
  (let [name-bytes (unpack-bytes rom-data-buffer 16)
        name-without-zero-bytes (byte-array (filter #(not (= % 0x00)) (vec name-bytes)))]
    (new String (bytes name-without-zero-bytes) (Charset/forName "US-ASCII"))))

(defn unpack-rom-data
  "unpacks the gb rom data given as byte array"
  [rom-data]
  (let [byte-buffer (ByteBuffer/allocate (count rom-data))]
    (.put byte-buffer rom-data 0 (count rom-data))          ; fill byte buffer with rom data
    (.flip byte-buffer)                                     ; reset pointer for reading
    (let [
          ; restart addresses
          rst$00 (unpack-eight-bytes byte-buffer)
          rst$08 (unpack-eight-bytes byte-buffer)
          rst$10 (unpack-eight-bytes byte-buffer)
          rst$18 (unpack-eight-bytes byte-buffer)
          rst$20 (unpack-eight-bytes byte-buffer)
          rst$28 (unpack-eight-bytes byte-buffer)
          rst$30 (unpack-eight-bytes byte-buffer)
          rst$38 (unpack-eight-bytes byte-buffer)
          ; interrupt addresses
          vertical-blank-interrupt (unpack-eight-bytes byte-buffer)
          lcdc-status-interrupt (unpack-eight-bytes byte-buffer)
          timer-overflow-interrupt (unpack-eight-bytes byte-buffer)
          serial-transfer-completion-interrupt (unpack-eight-bytes byte-buffer)
          high-to-low-interrupt (unpack-eight-bytes byte-buffer)
          ; code execution start opcodes start at 0x100, skip to there
          unused (.get byte-buffer (byte-array 152))
          ; code execution start opcodes
          code-execution-start-opcodes (unpack-start-opcodes byte-buffer)
          ; scrolling start logo
          start-logo (unpack-logo-graphic byte-buffer)
          ; name of the ROM
          name (unpack-name byte-buffer)
          ; flag indicating whether this ROM is a GB color ROM
          is-gb-color-rom (= (.byteValue 0x80) (.get byte-buffer))
          ]
      {
       :rst$00                               rst$00
       :rst$08                               rst$08
       :rst$10                               rst$10
       :rst$18                               rst$18
       :rst$20                               rst$20
       :rst$28                               rst$28
       :rst$30                               rst$30
       :rst$38                               rst$38
       :vertical-blank-interrupt             vertical-blank-interrupt
       :lcdc-status-interrupt                lcdc-status-interrupt
       :timer-overflow-interrupt             timer-overflow-interrupt
       :serial-transfer-completion-interrupt serial-transfer-completion-interrupt
       :high-to-low-interrupt                high-to-low-interrupt
       :start-opcodes                        code-execution-start-opcodes
       :logo                                 start-logo
       :logo-is-valid                        (= (seq expected-logo-graphic) (seq start-logo))
       :name                                 name
       :is-gb-color-rom                      is-gb-color-rom
       })))
