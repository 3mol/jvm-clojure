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


(deftest              readAndCheckVersion-test
  (testing "version"
    (is (= nil (->> (newClassReader (byte-array [0x00 0x00 0x00 0x2D])) readAndCheckVersion)))
    (is (= nil (->> (newClassReader (byte-array [0x00 0x00 0x00 0x2E])) readAndCheckVersion)))
    (is (= nil (->> (newClassReader (byte-array [0x00 0x00 0x00 0x2F])) readAndCheckVersion)))
    (is (= nil (->> (newClassReader (byte-array [0x00 0x00 0x00 0x30])) readAndCheckVersion)))
    (is (= nil (->> (newClassReader (byte-array [0x00 0x00 0x00 0x31])) readAndCheckVersion)))
    (is (= nil (->> (newClassReader (byte-array [0x00 0x00 0x00 0x32])) readAndCheckVersion)))
    (is (= nil (->> (newClassReader (byte-array [0x00 0x00 0x00 0x33])) readAndCheckVersion)))
    (is (= nil (->> (newClassReader (byte-array [0x00 0x00 0x00 0x34])) readAndCheckVersion)))
    (is (thrown? RuntimeException (->> (newClassReader (byte-array [0x00 0x00 0x00 0x35])) readAndCheckVersion)))
    (is (thrown? RuntimeException (->> (newClassReader (byte-array [0x00 0x01 0x00 0x2E])) readAndCheckVersion)))
    (is (thrown? RuntimeException (->> (newClassReader (byte-array [0x00 0x00 0x00 0x35])) readAndCheckVersion)))
    )
  )
