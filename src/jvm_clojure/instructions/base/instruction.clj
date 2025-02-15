(ns jvm-clojure.instructions.base.instruction)
; def interface {
; fetch-operands(bytecode-reader)
; execute(frame)
; }
;
(defprotocol Instruction
  (fetch-operands [this reader])
  (execute [this frame]))

;type NoOperandsInstruction struct {}
(defrecord NoOperandsInstruction []
  Instruction
  (fetch-operands [this reader])
  (execute [this frame])
  )
;type BranchInstruction struct {
;Offset int
;}
(defrecord BranchInstruction [offset]
  Instruction
  (fetch-operands [this reader])
  (execute [this frame])
  )

(defrecord Index8Instruction [index]
  Instruction
  (fetch-operands [this reader])
  (execute [this frame])
  )
