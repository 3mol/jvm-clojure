(ns jvm-clojure.classfile.classfile
  (:require [jvm-clojure.classfile.classreader :refer :all :as classreader])
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
  (let [value (->> reader classreader/readUint32 :value)
        is-cafebabe (= -889275714 value)                    ; CAFEBABE = -889275714, u can see: https://www.23bei.com/tool/56.html?
        ]
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
        accessFlag (classreader/readUint16 reader)
        thisClass (classreader/readUint16 reader)
        superClass (classreader/readUint16 reader)
        interfaces (classreader/readUint16s reader)
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
  (let [cr (classreader/newClassReader data)
        cf (newClassfile cr)
        ]
    cf)
  )