(ns jvm-clojure.classfile.classfile
  (:require [jvm-clojure.classfile.classreader :refer :all])
  (:import (jvm_clojure.classfile.classreader ClassReader)))

(defrecord Classfile
  [
   magic
   minor-version
   major-version
   constant-pool
   access-flags
   this-class
   super-class
   interfaces
   fields
   methods
   attributes
   ]
  )

(defn readAndCheckMagic [reader]
  (let [is-cafebabe (->> reader jvm-clojure.classfile.classreader/readUint32 :value (= 0xCAFEBABE))]
    (if is-cafebabe
      (println "cafebabe!")
      (throw (RuntimeException. "Class Format Error")))
    )
  )
(defn readAndCheckVersion [reader])
(defn readConstantPool [reader])
(defn readMembers [reader constantPool])
(defn readAttributes [reader constantPool])

(defn newClassfile [reader]
  (let [
        magic (readAndCheckMagic reader)
        version (readAndCheckVersion reader)
        constantPool (readConstantPool reader)
        accessFlag (jvm-clojure.classfile.classreader/readUint16 reader)
        thisClass (jvm-clojure.classfile.classreader/readUint16 reader)
        superClass (jvm-clojure.classfile.classreader/readUint16 reader)
        interfaces (jvm-clojure.classfile.classreader/readUint16s reader)
        fields (readMembers reader constantPool)
        methods (readMembers reader constantPool)
        attributes (readAttributes reader constantPool)
        ]
    (new Classfile magic nil nil constantPool accessFlag thisClass superClass interfaces fields methods attributes)))

;(defn minorVersion [classfile])                             ; getter
;(defn majorVersion [classfile])                             ; getter
;(defn constantPool [classfile])                             ; getter
;(defn accessFlags [classfile])                              ; getter
;(defn fields [classfile])                                   ; getter
;(defn methods [classfile])                                  ; getter
(defn className [classfile]
  (->> classfile :constantPool (.getClassName (:thisClass classfile)))
  )
(defn superClassName [classfile]
  (if (> classfile 0)
    (->> classfile :constantPool (.getClassName (:superClass classfile)))
    "")
  )
(defn interfaceName [classfile]
  (->> classfile :interfaces (map #(->> classfile :constantPool (.getClassName %))))
  )

(defn parse [data]
  (let [cr (jvm-clojure.classfile.classreader/newClassReader data)
        cf (newClassfile cr)
        ]
    cf)
  )