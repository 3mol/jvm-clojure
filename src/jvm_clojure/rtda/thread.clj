(ns jvm-clojure.rtda.thread)

(defrecord Thread [pc stack])

(defn getPc
  "docstring"
  [thread]
  (->> thread :pc)
  )

(defn setPc
  "docstring"
  [thread new-pc]
  (->> thread :stack (Thread. new-pc))
  )

(defn pushFrameToStack
  "docstring"
  [stack]
  )
(defn popFrameToStack
  "docstring"
  [stack]
  )
(defn pushFrame
  "docstring"
  [thread frame]
  (let [stack (:stack thread)
        newStack (pushFrameToStack stack frame)]
    (->> newStack (Thread. (:pc thread)))
    )
  )

(defn popFrame
  "docstring"
  [thread]
  (let [stack (:stack thread)
        popFrame (popFrameToStack stack)]
    (->> (filter #(not (= popFrame %)) (:elements stack)) (Thread. (:pc thread))))
  )

(defn currentFrame
  "docstring"
  [thread]
  (->> thread :stack :elements first)
  )

; stack part
(defrecord Stack [max-size size elements])

; newStack()
(defn newStack
  "docstring"
  [max-size]
  (Stack. max-size 0 nil))
; push(stack frame)
(defn pushFrameToStack
  "docstring"
  [stack frame]
  (let [new-size (+ 1 (:size stack))]
    ; new-size > max-size throw exception
    (when (> new-size (:max-size stack))
      (throw (RuntimeException. "stack overflow")))
    (Stack. (:max-size stack) new-size (cons frame (:elements stack)))
    )
  )
; pop(stack)
(defn popFrameToStack
  "docstring"
  [stack]
  (let [new-size (- (:size stack) 1)]
    (when (< new-size 0)
      (throw (RuntimeException. "stack underflow")))
    ; move pointer to top.next
    (Stack. (:max-size stack) new-size (->> stack :elements second))
    )
  )
; top(stack)
(defn topFrame
  "docstring"
  [stack]
  (->> stack :elements first)
  )

; frame part.
; local-vars is pointers. each pointer point to a slot in local-vars
; we could use a vector to store the local-vars
(defrecord Frame [lowers local-vars operand-stack])
(defrecord Slot [num ref])

; newLocalVars(maxLocals)
(defn newLocalVars
  "docstring"
  [max-locals]
  (vec (repeat max-locals (Slot. nil nil))))
; newOperandStack(maxStack)
(defn newOperandStack
  "docstring"
  [max-stack]
  (Stack. max-stack 0 nil))

; newFrame(maxLocals, maxStack)
(defn newFrame
  "docstring"
  [max-locals max-stack]
  (Frame. nil (newLocalVars max-locals) (newOperandStack max-stack)))

; build whole thread.
(defn newThread []
  (Thread. 0 (newStack 1024)))
