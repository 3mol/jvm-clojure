(ns jvm-clojure.classfile.classfile
  (:require [jvm-clojure.classfile.classreader :refer :all :as classreader])
  (:import (java.io DataInputStream)
           (jvm_clojure.classfile.classreader ClassReader)))


(defn readInfoArrByTag
  "docstring"
  [tag is]
  (case tag
    ;CONSTANT_Utf8	1
    ;CONSTANT_Utf8_info {
    ;                    u1 tag;
    ;                    u2 length;
    ;                    u1 bytes[length];
    ;                    }
    1 (let [
            length (.readUnsignedShort is)
            bytes (vec (repeatedly length #(.readUnsignedByte is)))
            ]
        {:tag tag :length length :bytes bytes})

    ;CONSTANT_Integer	3
    ;CONSTANT_Integer_info {
    ;                       u1 tag;
    ;                       u4 bytes;
    ;                       }
    3 (let [
            bytes (.readInt is)
            ]
        {:tag tag :bytes bytes})
    ;CONSTANT_Float	4
    ;CONSTANT_Float_info {
    ;                     u1 tag;
    ;                     u4 bytes;
    ;                     }
    4 (let [
            high_bytes (.readUnsignedShort is)
            low_bytes (.readUnsignedShort is)
            ]
        {:tag tag :high_bytes high_bytes :low_bytes low_bytes})

    ; todo using 2 entity to represent long and double
    ;CONSTANT_Long	5
    ;CONSTANT_Long_info {
    ;                    u1 tag;
    ;                    u4 high_bytes;
    ;                    u4 low_bytes;
    ;                    }
    5 (let [
            high_bytes (.readUnsignedShort is)
            low_bytes (.readUnsignedShort is)
            ]
        {:tag tag :high_bytes high_bytes :low_bytes low_bytes})
    ;CONSTANT_Double	6
    ;CONSTANT_Double_info {
    ;                      u1 tag;
    ;                      u4 high_bytes;
    ;                      u4 low_bytes;
    ;                      }

    6 (let [
            high_bytes (.readUnsignedShort is)
            low_bytes (.readUnsignedShort is)
            ]
        {:tag tag :high_bytes high_bytes :low_bytes low_bytes})

    ;CONSTANT_Class	7
    ;CONSTANT_Class_info {
    ;                     u1 tag; // 如果是7， 表示期类型是 CONSTANT_Class_info
    ;                     u2 name_index; // info是name_index的值指向常量池Utf8_info的索引数据
    ;                     }
    7 (let [
            name_index (.readUnsignedShort is)
            ]
        {:tag tag :name_index name_index})

    ;CONSTANT_String	8
    ;CONSTANT_String_info {
    ;                      u1 tag;
    ;                      u2 string_index;
    ;                      }
    8 (let [
            string_index (.readUnsignedShort is)
            ]
        {:tag tag :string_index string_index})
    ;CONSTANT_Fieldref	9
    ;CONSTANT_Fieldref_info {
    ;                        u1 tag; // 9
    ;                        u2 class_index; // 指向的常量池索引的类型是 CONSTANT_Class_info
    ;                        u2 name_and_type_index; // 指向的常量池索引的类型是 CONSTANT_NameAndType_info
    ;                        }
    9 (let [
            class_index (.readUnsignedShort is)
            name_and_type_index (.readUnsignedShort is)
            ]
        {:tag tag :class_index class_index :name_and_type_index name_and_type_index}
        )
    ;CONSTANT_Methodref	10
    ;CONSTANT_Methodref_info {
    ;                         u1 tag; // 10
    ;                         u2 class_index;
    ;                         u2 name_and_type_index;
    ;                         }
    10 (let [
             class_index (.readUnsignedShort is)
             name_and_type_index (.readUnsignedShort is)
             ]
         {:tag tag :class_index class_index :name_and_type_index name_and_type_index})

    ;CONSTANT_InterfaceMethodref	11
    ;CONSTANT_InterfaceMethodref_info {
    ;                                  u1 tag; // 11
    ;                                  u2 class_index;
    ;                                  u2 name_and_type_index;
    ;                                  }
    11 (let [
             class_index (.readUnsignedShort is)
             name_and_type_index (.readUnsignedShort is)
             ]
         {:tag tag :class_index class_index :name_and_type_index name_and_type_index})
    ;CONSTANT_NameAndType	12
    ;CONSTANT_NameAndType_info {
    ;                           u1 tag;
    ;                           u2 name_index;
    ;                           u2 descriptor_index;
    ;                           }
    12 (let [
             name_index (.readUnsignedShort is)
             descriptor_index (.readUnsignedShort is)
             ]
         {:tag tag :name_index name_index :descriptor_index descriptor_index})
    ;CONSTANT_MethodHandle	15
    ;CONSTANT_MethodHandle_info {
    ;                            u1 tag;
    ;                            u1 reference_kind;
    ;                            u2 reference_index;
    ;                            }
    15 (let [
             reference_kind (.readUnsignedByte is)
             reference_index (.readUnsignedShort is)
             ]
         {:tag tag :reference_kind reference_kind :reference_index reference_index})
    ;CONSTANT_MethodType	16
    ;CONSTANT_MethodType_info {
    ;                          u1 tag;
    ;                          u2 descriptor_index;
    ;                          }
    16 (let [
             descriptor_index (.readUnsignedShort is)
             ]
         {:tag tag :descriptor_index descriptor_index})
    ;CONSTANT_InvokeDynamic	18
    ;CONSTANT_InvokeDynamic_info {
    ;                             u1 tag;
    ;                             u2 bootstrap_method_attr_index;
    ;                             u2 name_and_type_index;
    ;                             }
    18 (let [
             bootstrap_method_attr_index (.readUnsignedShort is)
             name_and_type_index (.readUnsignedShort is)
             ]
         {:tag tag :bootstrap_method_attr_index bootstrap_method_attr_index :name_and_type_index name_and_type_index})
    )
  )

(defn read-cp-info-v2 [is count]
  ; do count times
  (loop [times count
         arr (vec '())]
    (if (> times 0)
      (let [tag (.readUnsignedByte is)
            info (readInfoArrByTag tag is)
            tag-index-space (if (or (= tag 5) (= tag 6))
                              2
                              1)
            ]
        ; print if is CONSTANT_Utf8_info
        (if (= 1 tag)
          (println info (str "<" (String. (byte-array (:bytes info)) "UTF-8") ">")))
        (recur (- times tag-index-space) (conj arr info)))
      arr
      )
    )
  )

(defn read-interface [is count]
  ; do count times
  (vec (repeatedly count
                   #(let [interface-index (.readUnsignedShort is)]
                      interface-index)
                   ))
  )

; attribute_info {
;    u2 attribute_name_index; // cp类型 CONSTANT_Utf8_info
;    u4 attribute_length;
;    u1 info[attribute_length];
;}
(defn readAttributeInfo [is count]
  (vec (repeatedly count
                   #(let [attribute_name_index (.readUnsignedShort is)
                          attribute_length (.readInt is)
                          info (vec (repeatedly attribute_length (fn [] (.readUnsignedByte is))))]
                      {
                       :attribute_name_index attribute_name_index
                       :attribute_length     attribute_length
                       :info                 info
                       })
                   ))
  )

; field_info {
;    u2             access_flags;
;    u2             name_index; // cp 类型为 CONSTANT_Utf8_info
;    u2             descriptor_index; // cp 类型 CONSTANT_Utf8_info
;    u2             attributes_count;
;    attribute_info attributes[attributes_count];
;}
(defn readFieldInfo [is count]
  (vec (repeatedly count
                   #(let [access_flag (.readUnsignedShort is)
                          name_index (.readUnsignedShort is)
                          descriptor_index (.readUnsignedShort is)
                          attributes_count (.readUnsignedShort is)
                          attributes (readAttributeInfo is attributes_count)]
                      {:access_flag      access_flag
                       :name_index       name_index
                       :descriptor_index descriptor_index
                       :attributes_count attributes_count
                       :attributes       attributes
                       }
                      )
                   ))
  )

;method_info {
;    u2             access_flags;
;    u2             name_index;
;    u2             descriptor_index;
;    u2             attributes_count;
;    attribute_info attributes[attributes_count];
;}
(defn readMethodInfo
  "docstring"
  [is count]
  (vec (repeatedly count
                   #(let [access_flag (.readUnsignedShort is)
                          name_index (.readUnsignedShort is)
                          descriptor_index (.readUnsignedShort is)
                          attributes_count (.readUnsignedShort is)
                          attributes (readAttributeInfo is attributes_count)]
                      {:access_flag      access_flag
                       :name_index       name_index
                       :descriptor_index descriptor_index
                       :attributes_count attributes_count
                       :attributes       attributes
                       }
                      )
                   ))
  )

(defrecord Classfile
  [
   ; magic
   minor_version
   major_version
   cp_info
   access_flag
   this_class
   super_class
   interfaces
   fields
   methods
   attributes
   ]
  )

(defn newClassfileV2 [classfile-path]
  (let [_is (->> classfile-path clojure.java.io/input-stream (DataInputStream.))
        magic (vec (repeatedly 4 #(.readUnsignedByte _is)))
        minor_version (.readUnsignedShort _is)
        major_version (.readUnsignedShort _is)
        cp_count (.readUnsignedShort _is)
        cp_info (read-cp-info-v2 _is (dec cp_count))
        access_flag (.readUnsignedShort _is)
        this_class (.readUnsignedShort _is)
        supper_class (.readUnsignedShort _is)
        interface_count (.readUnsignedShort _is)
        interfaces (read-interface _is interface_count)
        ; u2             fields_count;
        fields_count (.readUnsignedShort _is)
        ;    field_info     fields[fields_count];
        fields (readFieldInfo _is fields_count)
        ;    u2             methods_count;
        methods_count (.readUnsignedShort _is)
        ;    method_info    methods[methods_count];
        methods (readMethodInfo _is methods_count)
        ;    u2             attributes_count;
        attributes_count (.readUnsignedShort _is)
        ;    attribute_info attributes[attributes_count];
        attributes (readAttributeInfo _is attributes_count)
        ]
    (println magic minor_version major_version access_flag)
    (println "fields:" fields)
    (println "methods:" methods)
    (println "attributes:" attributes)
    (Classfile. minor_version major_version cp_info access_flag this_class supper_class interfaces fields methods attributes)
    )
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