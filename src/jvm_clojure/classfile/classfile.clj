(ns jvm-clojure.classfile.classfile
  (:import (java.nio ByteBuffer ByteOrder)))

(defrecord ClassReader [data]

  )

(defn readUint8 [class-reader]
  (let [
        val (ByteBuffer/wrap (byte-array (take 1 (.data class-reader))))
        the_rest (->> (drop 1 (.data class-reader)))
        ]
    {:value (.get (.order val ByteOrder/BIG_ENDIAN)), :ref (new ClassReader the_rest)}
    )
  )
(defn readUint16 [class-reader]
  (let [
        val (ByteBuffer/wrap (byte-array (take 2 (.data class-reader))))
        the_rest (->> (drop 2 (.data class-reader)))
        ]
    {:value (.getShort (.order val ByteOrder/BIG_ENDIAN)), :ref (new ClassReader the_rest)}
    )
  )
(defn readUint32 [class-reader]
  (let [
        val (ByteBuffer/wrap (byte-array (take 4 (.data class-reader))))
        the_rest (->> (drop 4 (.data class-reader)))
        ]
    {:value (.getInt (.order val ByteOrder/BIG_ENDIAN)), :ref (new ClassReader the_rest)}
    )
  )
(defn readUint64 [class-reader]
  (let [
        val (ByteBuffer/wrap (byte-array (take 8 (.data class-reader))))
        the_rest (->> (drop 8 (.data class-reader)))
        ]
    {:value (.getLong (.order val ByteOrder/BIG_ENDIAN)), :ref (new ClassReader the_rest)}
    )
  )
; read short table, the length is the first 2 bytes
(defn readUint16s [class-reader]
  (let [
        length (->> class-reader readUint16 :value)
        data-area (drop 2 (.data class-reader))
        groups (->> data-area (partition 2) (take length))
        ]
    {:value (map #(.getShort
                    (.order (ByteBuffer/wrap (byte-array %)) ByteOrder/BIG_ENDIAN)) groups),
     :ref   (new ClassReader
                 (drop (* 2 length) data-area))}
    )
  )
(defn readBytes [class-reader length]
  (let [value (take length (.data class-reader))
        the_rest (drop length (.data class-reader))
        ]
    {:value value, :ref (new ClassReader the_rest)}
    )
  )