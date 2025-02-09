(ns jvm-clojure.classfile.classreader-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer :all]
            [jvm-clojure.classfile.classreader :refer :all]
            [jvm-clojure.classfile.classfile :refer :all])
  (:import (java.io ByteArrayOutputStream DataInputStream)))

(def data (byte-array [(byte 0x00) (byte 0x01) (byte 0x02) (byte 0x03) (byte 0x04) (byte 0x05) (byte 0x06) (byte 0x07)]))
(def data2 (byte-array [(byte 0x00) (byte 0x03) (byte 0x01) (byte 0x02) (byte 0x03) (byte 0x04) (byte 0x05) (byte 0x06) (byte 0x07)]))
(deftest read-classfile
  (testing "read"
    (is (= 0 (:value (readUint8 (newClassReader data)))))
    (is (= 1 (:value (readUint16 (newClassReader data)))))
    (is (= 0x00010203 (:value (readUint32 (newClassReader data)))))
    (is (= 0x0001020304050607 (:value (readUint64 (newClassReader data)))))
    (is (= '((1 2) (3 4) (5 6) (7 8)) (partition 2 '(1 2 3 4 5 6 7 8))))
    (is (= '(0x00 0x01 0x02 0x03) (:value (readBytes (newClassReader data) 4)))))
  )
(deftest read-classfile2
  (testing "read"
    (is (= '(0x0102 0x0304 0x0506) (:value (readUint16s (newClassReader data2)))))
    )
  )

(deftest position-classfile
  (testing "position"
    (is (= {:value 1} (let [obj (newClassReader data2)]
                        (readUint8 obj)
                        (deref (:position obj)))))
    (is (= {:value (+ 1 2)} (let [obj (newClassReader data2)]
                              (is (= 0x00 (:value (readUint8 obj))))
                              (is (= 0x0301 (:value (readUint16 obj))))
                              (deref (:position obj)))))))

(def classfile-path (str "resources/testclass/Main.class"))
(defn newMainClassData []
  (let [out (ByteArrayOutputStream.)
        _ (io/copy (clojure.java.io/input-stream classfile-path) out)
        ]
    (.toByteArray out)
    ))

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
(defn read-cp-info [is count]
  ; do count times
  (vec (repeatedly count
                   #(let [tag (.readUnsignedByte is)
                          info (readInfoArrByTag tag is)]
                      (if (= 1 (:tag info))
                        (println info (str "<" (String. (byte-array (:bytes info)) "UTF-8") ">"))
                        (println info))
                      info)
                   ))
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
(deftest testClassFile
  (testing "testClassFile"
    (is (= 1066 (alength (newMainClassData))))
    (let [_is (->> classfile-path clojure.java.io/input-stream (DataInputStream.))
          magic (vec (repeatedly 4 #(.readUnsignedByte _is)))
          minor_version (.readUnsignedShort _is)
          major_version (.readUnsignedShort _is)
          cp_count (.readUnsignedShort _is)
          cp_info (read-cp-info _is (dec cp_count))
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
      (is (= '(0xCA 0xFE 0xBA 0xBE) magic))
      (is (= 0 minor_version))
      (is (= 52 major_version))
      (is (= 60 cp_count))
      (is (= (dec cp_count) (.size cp_info)))
      (is (= 0x0021 access_flag))
      (is (= 7 this_class))
      (is (= 13 supper_class))
      (is (= 1 interface_count))
      (is (= '[14] interfaces))
      (is (= 2 fields_count))
      (is (= 2 (.size fields)))
      (is (= 5 methods_count))
      (is (= 5 (.size methods)))
      (is (= 1 attributes_count))
      (is (= 1 (.size attributes)))
      )
    ))