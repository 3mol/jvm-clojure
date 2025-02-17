(ns jvm-clojure.instructions.constants.constants-inst
  (:require [jvm-clojure.rtda.thread :refer :all :as thread]))

(defn non
  "docstring"
  [arglist]
  (:fetch-operands (fn []))
  (:execute (fn [frame]))
  )

;type ACONST_NULL struct{ base.NoOperandsInstruction }
(defn aconstnull
  "docstring"
  [arglist]
  (:fetch-operands (fn []))
  (:execute (fn [frame]
              (-> frame :operand-stack (thread/pushRef nil))))
  )

;type DCONST_0 struct{ base.NoOperandsInstruction }
(defn dconst0
  "docstring"
  [arglist]
  (:fetch-operands (fn []))
  (:execute (fn [frame]
              (-> frame :operand-stack (thread/pushDouble 0.0))))
  )

;type DCONST_1 struct{ base.NoOperandsInstruction }
(defn dconst1
  "docstring"
  [arglist]
  (:fetch-operands (fn []))
  (:execute (fn [frame]
              (-> frame :operand-stack (thread/pushDouble 1.0))
              ))
  )

;type FCONST_0 struct{ base.NoOperandsInstruction }
(defn fconst0
  "docstring"
  [arglist]
  (:fetch-operands (fn []))
  (:execute (fn [frame]
              (-> frame :operand-stack (thread/pushFloat 0.0))))
  )

;type FCONST_1 struct{ base.NoOperandsInstruction }
(defn fconst1
  "docstring"
  [arglist]
  (:fetch-operands (fn []))
  (:execute (fn [frame]
              (-> frame :operand-stack (thread/pushFloat 1.0))))
  )

;type FCONST_2 struct{ base.NoOperandsInstruction }
(defn fconst2
  "docstring"
  [arglist]
  (:fetch-operands (fn []))
  (:execute (fn [frame]
              (-> frame :operand-stack (thread/pushFloat 2.0))))
  )

;type ICONST_M1 struct{ base.NoOperandsInstruction }
(defn iconstM1
  "docstring"
  [arglist]
  (:fetch-operands (fn []))
  (:execute (fn [frame]
              (-> frame :operand-stack (thread/pushInt -1))))
  )

;type ICONST_0 struct{ base.NoOperandsInstruction }
(defn iconst0
  "docstring"
  [arglist]
  (:fetch-operands (fn []))
  (:execute (fn [frame]
              (-> frame :operand-stack (thread/pushInt 0))))
  )

;type ICONST_1 struct{ base.NoOperandsInstruction }
(defn iconst1
  "docstring"
  [arglist]
  (:fetch-operands (fn []))
  (:execute (fn [frame]
              (-> frame :operand-stack (thread/pushInt 1))))
  )

;type ICONST_2 struct{ base.NoOperandsInstruction }
(defn iconst2
  "docstring"
  [arglist]
  (:fetch-operands (fn []))
  (:execute (fn [frame]
              (-> frame :operand-stack (thread/pushInt 2))))
  )

;type ICONST_3 struct{ base.NoOperandsInstruction }
(defn iconst3
  "docstring"
  [arglist]
  (:fetch-operands (fn []))
  (:execute (fn [frame]
              (-> frame :operand-stack (thread/pushInt 3))))
  )

;type ICONST_4 struct{ base.NoOperandsInstruction }
(defn iconst4
  "docstring"
  [arglist]
  (:fetch-operands (fn []))
  (:execute (fn [frame]
              (-> frame :operand-stack (thread/pushInt 4))))
  )

;type ICONST_5 struct{ base.NoOperandsInstruction }
(defn iconst5
  "docstring"
  [arglist]
  (:fetch-operands (fn []))
  (:execute (fn [frame]
              (-> frame :operand-stack (thread/pushInt 5))))
  )

;type LCONST_0 struct{ base.NoOperandsInstruction }
(defn lconst0
  "docstring"
  [arglist]
  (:fetch-operands (fn []))
  (:execute (fn [frame]
              (-> frame :operand-stack (thread/pushLong 0))))
  )

;type LCONST_1 struct{ base.NoOperandsInstruction }
(defn lconst1
  "docstring"
  [arglist]
  (:fetch-operands (fn []))
  (:execute (fn [frame]
              (-> frame :operand-stack (thread/pushLong 1))))
  )
