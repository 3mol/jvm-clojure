(ns jvm-clojure.classfile.classfile
  (:require [jvm-clojure.classfile.classreader :refer :all :as classreader])
  (:import (jvm_clojure.classfile.classreader ClassReader)))

(defrecord Classfile
  [
   ; magic
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
(defrecord MemberInfo
  [
   cp
   access-flags
   name-index
   descriptor-index
   attributes
   ]
  )
(defn readAndCheckMagic [reader]
  (let [value (->> reader classreader/readUint32 :value)
        is-cafebabe (= -889275714 value)                    ; CAFEBABE = -889275714, u can see: https://www.23bei.com/tool/56.html?
        ]
    (if is-cafebabe
      -889275714
      (throw (RuntimeException. "Class Format Error")))
    )
  )
(defn readAndCheckVersion [reader]
  (let [minor-version (->> reader classreader/readUint16 :value)
        major-version (->> reader classreader/readUint16 :value)
        ]
    (cond
      (= major-version 45)
      {:major-version major-version :minor-version minor-version}

      (and (contains? #{46 47 48 49 50 51 52} major-version) (= minor-version 0))
      {:major-version major-version :minor-version minor-version}

      :else
      (throw (RuntimeException. "java.lang.UnsupportedVersionError"))
      )
    ))
(defn readAttributes [reader constantPool])                 ; see 3.4
(defn readConstantPool [reader])

(defn readMember [reader cp]
  (let [accessFlag (classreader/readUint16 reader)
        nameIndex (classreader/readUint16 reader)
        descriptorIndex (classreader/readUint16 reader)
        attributes (readAttributes reader cp)]
    (new MemberInfo cp accessFlag nameIndex descriptorIndex attributes)
    )
  )
(defn readMembers [reader constantPool]
  (let [memberCount (->> reader classreader/readUint16 :value)]
    (->> (range 0 memberCount) (map (fn [_] (readMember reader constantPool))))
    ))

(defn memberName [memberInfo]
  (let [cp (:cp memberInfo)
        name-index (:name-index memberInfo)]
    (->> cp (.getUtf8 name-index))
    )
  )
(defn memberDescriptor [memberInfo]
  (let [cp (:cp memberInfo)
        name-index (:name-index memberInfo)]
    (->> cp (.getUtf8 name-index))
    )
  )

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
    (println magic version constantPool accessFlag thisClass superClass interfaces fields methods attributes)
    (new Classfile (:minor-version version) (:major-version version) constantPool accessFlag thisClass superClass interfaces fields methods attributes)))

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