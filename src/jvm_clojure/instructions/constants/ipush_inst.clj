(ns jvm-clojure.instructions.constants.ipush-inst
  (:require [jvm-clojure.rtda.thread :refer :all :as thread])
  (:require [jvm-clojure.instructions.base.bytecode-reader :refer :all :as bytecode-reader]))


;type BIPUSH struct { val int8 } // Push byte
(defn bipush
  "docstring"
  [frame reader]
  (let [val (ref nil)
        init (fn [] (dosync (ref-set val (bytecode-reader/readInt8 reader))))
        exec (fn [] (-> frame :operand-stack (thread/pushInt @val)))
        ]
    {:fetch-operands init :execute exec}
    )
  )

;type SIPUSH struct { val int16 } // Push short
(defn sipush
  "docstring"
  [frame reader]
  (let [val (ref nil)
        init (fn [] (dosync (ref-set val (bytecode-reader/readUint16 reader))))
        exec (fn [] (-> frame :operand-stack (thread/pushInt @val)))
        ]
    {:fetch-operands init :execute exec}
    )
  )