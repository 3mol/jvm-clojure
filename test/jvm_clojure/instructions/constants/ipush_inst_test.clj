(ns jvm-clojure.instructions.constants.ipush-inst-test
  (:require [clojure.test :refer :all])
  (:require [jvm-clojure.rtda.thread :refer :all])
  (:require [jvm-clojure.instructions.base.bytecode-reader :refer :all])
  (:require [jvm-clojure.instructions.constants.ipush-inst :refer :all]))

(defn test-ipush [instruction expected-value bytecode]
  (let [frame (newFrame 100 100)
        reader (newBytecodeReader bytecode 0)
        inst (instruction frame reader)]
    ((:fetch-operands inst))
    ((:execute inst))
    (is (= expected-value (popInt (-> frame :operand-stack))))))

(deftest bipush-test
  (testing "bipush"
    (test-ipush bipush 1 (list (byte 1) (byte 2) (byte 3)))))

(deftest sipush-test
  (testing "sipush"
    (test-ipush sipush 0x0102 (list (byte 1) (byte 2) (byte 3)))))