(ns jvm-clojure.classfile.classfile-test
  (:require
    [clojure.spec.alpha :as s]
    [clojure.test :refer :all]
    [jvm-clojure.classfile.classreader :refer :all]
    [jvm-clojure.classfile.classfile :refer :all])
  )

(def class-data (byte-array [0xca 0xfe 0xba 0xbe 0x03 0x04 0x05 0x06 0x07]))
(def not-class-data (byte-array [0xcc 0xfe 0xba 0xbe 0x03 0x04 0x05 0x06 0x07]))
(deftest readAndCheckMagic-test
  (testing "read"
    (is (= nil (->> (newClassReader class-data) readAndCheckMagic)))
    (is (thrown? RuntimeException (->> (newClassReader not-class-data) readAndCheckMagic)))
    ))