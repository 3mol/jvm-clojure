(ns jvm-clojure.classfile.classfile-test
  (:require
    [clojure.java.io :as io]
    [clojure.spec.alpha :as s]
    [clojure.test :refer :all]
    [jvm-clojure.classfile.classreader :refer :all]
    [jvm-clojure.classfile.classfile :refer :all])
  (:import (java.io ByteArrayOutputStream)))

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

(def classfile-v1 (str "resources/testclass/Main.class"))
(def classfile-v2 (str "resources/testclass/Main2.class"))
(defn newMainClassData [path]
  (let [out (ByteArrayOutputStream.)
        _ (io/copy (clojure.java.io/input-stream path) out)
        ]
    (.toByteArray out)
    ))

(deftest testClassFile
  (testing "testClassFile"
    (is (= 1066 (alength (newMainClassData classfile-v1))))
    (let [classFile (newClassfileV2 classfile-v1)]
      (is (= 0 (:minor_version classFile)))
      (is (= 52 (:major_version classFile)))
      (is (= 59 (.size (:cp_info classFile))))
      (is (= 0x0021 (:access_flag classFile)))
      (is (= 7 (:this_class classFile)))
      (is (= 13 (:super_class classFile)))
      (is (= '[14] (:interfaces classFile)))
      (is (= 2 (.size (:fields classFile))))
      (is (= 5 (.size (:methods classFile))))
      (is (= 1 (.size (:attributes classFile))))
      )))


(deftest testClassFile
  (testing "testClassFile"
    (let [classFile (newClassfileV2 classfile-v2)]
      (is (= 0 (:minor_version classFile)))
      (is (= 52 (:major_version classFile)))
      (is (= (- (dec 116) 2) (.size (:cp_info classFile))))
      (is (= 0x0021 (:access_flag classFile)))
      (is (= 8 (:this_class classFile)))
      (is (= 2 (:super_class classFile)))
      (is (= '[96] (:interfaces classFile)))
      (is (= 13 (.size (:fields classFile))))
      (is (= 5 (.size (:methods classFile))))
      (is (= 1 (.size (:attributes classFile))))
      )))