(ns jvm-clojure.classfile.constant-pool
  (:require [jvm-clojure.classfile.classreader :as classreader])
  )

(deftype ConstantPool [constantInfos])

(defrecord ConstantInfo [])


(defrecord readConstantInfo [reader])
(defn readConstantPool [reader]
  (let [cpCount (classreader/readUint16 reader)]
    (->> (range 1 cpCount) (map (fn [_] (readConstantInfo reader))))))
(defn getConstantInfo [cp index]
  (nth cp index))
(defn getNameAndType [cp index]
  {:name "" :type ""}
  )
(defn getClassName [cp index])
(defn getUtf8 [cp index])
