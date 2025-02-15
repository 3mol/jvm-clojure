(ns jvm-clojure.rtda.thread)

(defrecord Threadj [pc stack])

(defn getPc
  "docstring"
  [thread]
  (->> thread :pc)
  )

(defn setPc
  "docstring"
  [thread new-pc]
  (->> thread :stack (Threadj. new-pc))
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
    (->> newStack (Threadj. (:pc thread)))
    )
  )

(defn popFrame
  "docstring"
  [thread]
  (let [stack (:stack thread)
        popFrame (popFrameToStack stack)]
    (->> (filter #(not (= popFrame %)) (:elements stack)) (Threadj. (:pc thread))))
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
(defrecord LocalVars [slots])

; newLocalVars(maxLocals)
(defn newLocalVars
  "docstring"
  [max-locals]
  (LocalVars. (vec (repeat max-locals (Slot. nil nil)))))
; getInt(localVars)
(defn getInt
  "docstring"
  [local-vars index]
  (as-> local-vars $ (:slots $) (nth $ index) (:num $)))
; setInt(localVars)
(defn setInt
  "docstring"
  [local-vars index num]
  (let [slot (as-> local-vars $ (:slots $) (nth $ index))
        new-slot (Slot. num (:ref slot))]
    (assoc-in local-vars [:slots index] new-slot)))

; getFloat(localVars)
(defn getFloat
  "docstring"
  [local-vars index]
  (getInt local-vars index))
; setInt(localVars)
(defn setFloat
  "docstring"
  [local-vars index num]
  (setInt local-vars index num))

; mark setLong, will be split into two slots
(defn setLong
  "docstring"
  [local-vars index num]
  (setInt local-vars index num)
  ; (setInt local-vars (inc index) num)
  )

; getLong
(defn getLong
  "docstring"
  [local-vars index]
  (getInt local-vars index))


; setDouble, will be split into two slots
(defn setDouble
  "docstring"
  [local-vars index num]
  (setInt local-vars index num)
  ; (setInt local-vars (inc index) num)
  )

; getDouble
(defn getDouble
  "docstring"
  [local-vars index]
  (getInt local-vars index))

; setRef, will be split into two slots
(defn setRef
  "docstring"
  [local-vars index ref]
  (let [new-slot (Slot. nil ref)]
    (assoc-in local-vars [:slots index] new-slot))
  )

; getRef
(defn getRef
  "docstring"
  [local-vars index]
  (as-> local-vars $ (:slots $) (nth $ index) (:ref $))
  )


(defrecord OperandStack [size slots])
; newOperandStack(maxStack)
(defn newOperandStack
  "docstring"
  [max-stack]
  (OperandStack. 0 (vec (repeat max-stack (Slot. nil nil)))))
; pushInt
(defn pushInt
  "docstring"
  [operand-stack num]
  (let [new-size (+ 1 (:size operand-stack))]
    (when (> new-size (:max-stack operand-stack))
      (throw (RuntimeException. "stack overflow")))
    (assoc-in operand-stack [:slots new-size] (Slot. num nil))
    )
  )
; popInt
(defn popInt
  "docstring"
  [operand-stack]
  (let [new-size (- (:size operand-stack) 1)]
    (when (< new-size 0)
      (throw (RuntimeException. "stack underflow")))
    (->> operand-stack :slots (nth new-size) :num)
    )
  )
; pushFloat
(defn pushFloat
  "docstring"
  [operand-stack num]
  (pushInt operand-stack num))
; popFloat
(defn popFloat
  "docstring"
  [operand-stack]
  (popInt operand-stack))
; pushLong
(defn pushLong
  "docstring"
  [operand-stack num]
  (pushInt operand-stack num)
  )
; popLong
(defn popLong
  "docstring"
  [operand-stack]
  (popInt operand-stack))
; pushDouble
(defn pushDouble
  "docstring"
  [operand-stack num]
  (pushInt operand-stack num)
  )
; popDouble
(defn popDouble
  "docstring"
  [operand-stack]
  (popInt operand-stack))

; pushRef
(defn pushRef
  "docstring"
  [operand-stack ref]
  (let [new-size (+ 1 (:size operand-stack))]
    (when (> new-size (:max-stack operand-stack))
      (throw (RuntimeException. "stack overflow")))
    (assoc-in operand-stack [:slots new-size] (Slot. nil ref))
    )
  )
; popRef
(defn popRef
  "docstring"
  [operand-stack]
  (let [new-size (- (:size operand-stack) 1)]
    (when (< new-size 0)
      (throw (RuntimeException. "stack underflow")))
    (->> operand-stack :slots (nth new-size) :ref)
    )
  )

; newFrame(maxLocals, maxStack)
(defn newFrame
  "docstring"
  [max-locals max-stack]
  (Frame. nil (newLocalVars max-locals) (newOperandStack max-stack)))

; build whole thread.
(defn newThread []
  (Threadj. 0 (newStack 1024)))

; mark: _load_ is local-vars load to operand-stack
; mark: _store_ is operand-stack to local-vars
; mark: _bipush\ldc etc._ is constants load to operand-stack
