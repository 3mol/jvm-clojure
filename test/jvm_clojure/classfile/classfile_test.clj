(ns jvm-clojure.classfile.classfile-test
  (:require [clojure.test :refer :all]
            [jvm-clojure.classfile.classfile :refer :all])
  (:import (jvm_clojure.classfile.classfile ClassReader)))

(def data (byte-array [(byte 0x00) (byte 0x01) (byte 0x02) (byte 0x03) (byte 0x04) (byte 0x05) (byte 0x06) (byte 0x07)]))
(defn cr [] (new ClassReader data))
(def data2 (byte-array [(byte 0x00) (byte 0x03) (byte 0x01) (byte 0x02) (byte 0x03) (byte 0x04) (byte 0x05) (byte 0x06) (byte 0x07)]))
(defn cr2 [] (new ClassReader data2))
(deftest read-classfile
  (testing "read"
    (is (= 0 (:value (readUint8 (cr)))))
    (is (= (rest data) (:data (:ref (readUint8 (cr))))))
    (is (= 1 (:value (readUint16 (cr)))))
    (is (= 0x00010203 (:value (readUint32 (cr)))))
    (is (= 0x0001020304050607 (:value (readUint64 (cr)))))
    (is (= '((1 2) (3 4) (5 6) (7 8)) (partition 2 '(1 2 3 4 5 6 7 8))))
    (is (= '(0x0102 0x0304 0x0506) (:value (readUint16s (cr2)))))
    (is (= '(0x07) (:data (:ref (readUint16s (cr2))))))
    )
  )