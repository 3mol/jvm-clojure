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
    (is (= -889275714 (->> (newClassReader class-data) readAndCheckMagic)))
    (is (thrown? RuntimeException (->> (newClassReader not-class-data) readAndCheckMagic)))
    ))


(deftest readAndCheckVersion-test
  (testing "version"
    (->> (range 45 53)
         (map
           (fn [i] (is (= {:major-version i :minor-version 0}
                          (->> (newClassReader (byte-array [0x00 0x00 0x00 i])) readAndCheckVersion))))))
    (is (thrown? RuntimeException (->> (newClassReader (byte-array [0x00 0x00 0x00 53])) readAndCheckVersion)))
    (is (thrown? RuntimeException (->> (newClassReader (byte-array [0x00 0x01 0x00 46])) readAndCheckVersion)))
    )
  )
