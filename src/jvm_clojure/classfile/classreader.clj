(ns jvm-clojure.classfile.classreader
  (:import (java.nio ByteBuffer ByteOrder)))

(defrecord ClassReader [data position]

  )
(defn newClassReader [class-data] (new ClassReader class-data (ref {:value 0})))

(defn inc-position [class-reader delta]
  (dosync
    (let [position (->> class-reader :position)
          old-value (->> @position :value)
          new-value (+ delta old-value)]
      (dosync (ref-set position {:value new-value})))))

(defn read-from-position
  "docstring"
  [class-reader]
  (drop (->> class-reader :position deref :value) (:data class-reader))
  )

(defn readUint8 [class-reader]
  (let [
        data (read-from-position class-reader)
        val (ByteBuffer/wrap (byte-array (take 1 data)))
        ]
    (inc-position class-reader 1)
    {:value (.get (.order val ByteOrder/BIG_ENDIAN))}
    )
  )

(defn readUint16 [class-reader]
  (let [
        data (read-from-position class-reader)
        val (ByteBuffer/wrap (byte-array (take 2 data)))
        ]
    (inc-position class-reader 2)
    {:value (.getShort (.order val ByteOrder/BIG_ENDIAN))}
    )
  )

(defn readUint32 [class-reader]
  (let [
        data (read-from-position class-reader)
        val (ByteBuffer/wrap (byte-array (take 4 data)))
        ]
    (inc-position class-reader 4)
    {:value (.getInt (.order val ByteOrder/BIG_ENDIAN))}
    )
  )
(defn readUint64 [class-reader]
  (let [
        data (read-from-position class-reader)
        val (ByteBuffer/wrap (byte-array (take 8 data)))
        ]
    (inc-position class-reader 8)
    {:value (.getLong (.order val ByteOrder/BIG_ENDIAN))}
    )
  )
; read short table, the length is the first 2 bytes
(defn readUint16s [class-reader]
  (let [
        data (read-from-position class-reader)
        length (->> class-reader readUint16 :value)
        groups (->> (drop 2 data) (partition 2) (take length))
        ]
    (inc-position class-reader (+ 2 (* 2 length)))
    {:value (map #(.getShort
                    (.order (ByteBuffer/wrap (byte-array %)) ByteOrder/BIG_ENDIAN)) groups),
     }
    )
  )
(defn readBytes [class-reader length]
  (let [
        data (read-from-position class-reader)
        value (take length data)
        ]
    (inc-position class-reader length)
    {:value value}
    )
  )