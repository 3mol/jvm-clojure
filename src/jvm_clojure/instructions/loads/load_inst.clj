(ns jvm-clojure.instructions.loads.load-inst
  (:require [jvm-clojure.rtda.thread :refer :all :as thread])
  (:require [jvm-clojure.instructions.base.bytecode-reader :refer :all :as bytecode-reader]))

; base
; func _iload(frame *rtda.Frame, index uint) {
;val := frame.LocalVars().GetInt(index)
;frame.OperandStack().PushInt(val)
;}
(defn _iload
  "docstring"
  [frame index]
  (let [value (-> frame :local-vars (thread/getInt index))]
    (-> frame :operand-stack (thread/pushInt value)))
  )

; iload: Load int from local variable, then push to operand stack
(defn iload
  "docstring"
  [frame reader]
  (let [val (ref nil)
        init (fn [] (dosync (ref-set val (bytecode-reader/readUint32 reader))))
        exec (fn [] (_iload frame @val))
        ]
    {:fetch-operands init :execute exec}
    )
  )

(defn base_iload
  "docstring"
  [frame reader index]
  (let [
        init (fn [] ())
        exec (fn [] (_iload frame index))
        ]
    {:fetch-operands init :execute exec}
    )
  )

; Load int from local variable

(defn iload0
  "docstring"
  [frame reader]
  (base_iload frame reader 0)
  )
(defn iload1
  "docstring"
  [frame reader]
  (base_iload frame reader 1)
  )
(defn iload2
  "docstring"
  [frame reader]
  (base_iload frame reader 2)
  )
(defn iload3
  "docstring"
  [frame reader]
  (base_iload frame reader 3)
  )
(defn iload4
  "docstring"
  [frame reader]
  (base_iload frame reader 4)
  )