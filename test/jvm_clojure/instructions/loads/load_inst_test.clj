(ns jvm-clojure.instructions.loads.load-inst-test
  (:require [clojure.test :refer :all])
  (:require [clojure.test :refer :all])
  (:require [jvm-clojure.rtda.thread :refer :all])
  (:require [jvm-clojure.instructions.base.bytecode-reader :refer :all])
  (:require [jvm-clojure.instructions.constants.ipush-inst :refer :all])
  (:require [jvm-clojure.instructions.loads.load-inst :refer [iload iload0]]))

(defn test-inst [instruction expected-value bytecode]
  (let [frame (newFrame 100 100)
        reader (newBytecodeReader bytecode 0)
        inst (instruction frame reader)]
    ; init local-vars
    (setInt (-> frame :local-vars) 0 100)
    (setInt (-> frame :local-vars) 50 51)
    ((:fetch-operands inst))
    ((:execute inst))
    (is (= expected-value (popInt (-> frame :operand-stack))))))

; The operand is 50,
; and the value 51 is taken from the local variable 50
; and push into stack
(deftest test-iload
  (testing "iload"
    (test-inst iload 51 (list (byte 0) (byte 0) (byte 0) (byte 50)))))

(deftest sipush-test
  (testing "sipush"
    (test-inst iload0 100 (list))))