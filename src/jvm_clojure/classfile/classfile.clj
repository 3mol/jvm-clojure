(ns jvm-clojure.classfile.classfile)

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
(defn parse [data]
  )

(defn read [classfile reader])
(defn readAndCheckMagic [classfile])
(defn readAndCheckVersion [classfile])
(defn minorVersion [classfile])
(defn majorVersion [classfile])
(defn constantPool [classfile])
(defn accessFlags [classfile])
(defn fields [classfile])
(defn methods [classfile])
(defn className [classfile])
(defn superClassName [classfile])
(defn interfaceName [classfile])