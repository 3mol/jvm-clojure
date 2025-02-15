(ns jvm-clojure.instructions.base.bytecode-reader)

(defrecord BytecodeReader [code pc])

(defn newBytecodeReader
  "docstring"
  [data init-pc]
  (BytecodeReader. data (ref init-pc))
  )
; func (self *BytecodeReader) ReadUint8() uint8 {
;i := self.code[self.pc]
;self.pc++
;return i
;}
(defn readUint8 [bytecode-reader]
  (let [
        pc-ref (:pc bytecode-reader)
        a-byte (nth (:code bytecode-reader) @pc-ref)]
    (do (dosync (ref-set pc-ref (inc @pc-ref))))
    a-byte
    )
  )
; todo signed
(defn readInt8 [bytecode-reader]
  (readUint8 bytecode-reader)
  )
(defn readUint16 [bytecode-reader]
  (let [
        pc-ref (:pc bytecode-reader)
        a-byte (nth (:code bytecode-reader) @pc-ref)
        b-byte (nth (:code bytecode-reader) (inc @pc-ref))
        ]
    (do (dosync (ref-set pc-ref (+ 2 @pc-ref))))
    (bit-or (bit-shift-left a-byte 8) b-byte)
  )
)

(defn readUint32 [bytecode-reader]
  (let [
        pc-ref (:pc bytecode-reader)
        a-byte (nth (:code bytecode-reader) @pc-ref)
        b-byte (nth (:code bytecode-reader) (inc @pc-ref))
        c-byte (nth (:code bytecode-reader) (+ 2 @pc-ref))
        d-byte (nth (:code bytecode-reader) (+ 3 @pc-ref))
        ]
    (do (dosync (ref-set pc-ref (+ 4 @pc-ref))))
    (bit-or (bit-shift-left a-byte 24) (bit-shift-left b-byte 16) (bit-shift-left c-byte 8) d-byte)
  )
)
