(ns jvm-clojure.classfile.classreader-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [jvm-clojure.classfile.classreader :refer :all]
            [jvm-clojure.classfile.classfile :refer :all])
  (:import (java.io ByteArrayOutputStream DataInputStream)))

(def data (byte-array [(byte 0x00) (byte 0x01) (byte 0x02) (byte 0x03) (byte 0x04) (byte 0x05) (byte 0x06) (byte 0x07)]))
(def data2 (byte-array [(byte 0x00) (byte 0x03) (byte 0x01) (byte 0x02) (byte 0x03) (byte 0x04) (byte 0x05) (byte 0x06) (byte 0x07)]))
(deftest read-classfile
  (testing "read"
    (is (= 0 (:value (readUint8 (newClassReader data)))))
    (is (= 1 (:value (readUint16 (newClassReader data)))))
    (is (= 0x00010203 (:value (readUint32 (newClassReader data)))))
    (is (= 0x0001020304050607 (:value (readUint64 (newClassReader data)))))
    (is (= '((1 2) (3 4) (5 6) (7 8)) (partition 2 '(1 2 3 4 5 6 7 8))))
    (is (= '(0x00 0x01 0x02 0x03) (:value (readBytes (newClassReader data) 4)))))
  )
(deftest read-classfile2
  (testing "read"
    (is (= '(0x0102 0x0304 0x0506) (:value (readUint16s (newClassReader data2)))))
    )
  )

(deftest position-classfile
  (testing "position"
    (is (= {:value 1} (let [obj (newClassReader data2)]
                        (readUint8 obj)
                        (deref (:position obj)))))
    (is (= {:value (+ 1 2)} (let [obj (newClassReader data2)]
                              (is (= 0x00 (:value (readUint8 obj))))
                              (is (= 0x0301 (:value (readUint16 obj))))
                              (deref (:position obj)))))))

