(ns gb-dumper.analyze)

(defn to-hex-string
  "Convert a seq of bytes into a hex encoded string"
  [bytes]
  (apply str (for [byte bytes] (format "%02x" byte))))

(defn unpack-rom-data
  "unpacks the gb rom data given as byte array"
  [rom-data]
  (to-hex-string rom-data))
