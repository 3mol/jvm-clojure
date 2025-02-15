(ns jvm-clojure.instructions.base.bytecode-reader-test
  (:require [clojure.test :refer :all])
  (:require [jvm-clojure.instructions.base.bytecode-reader :refer :all]))

(deftest newBytecodeReader-test
  (testing "newFrame-2"
    (let [
          reader (newBytecodeReader (list (byte 1) (byte 2) (byte 3) (byte 4) (byte 1) (byte 1) (byte 1) (byte 1)) 0)
          ]
      (is (= 1 (readUint8 reader)))
      (is (= 2 (readInt8 reader)))
      (is (= 0x0304 (readUint16 reader)))
      (is (= 0x01010101 (readUint32 reader)))
      )))