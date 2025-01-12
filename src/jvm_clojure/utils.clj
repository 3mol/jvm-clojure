(ns jvm-clojure.utils
  (:require [clojure.java.io :as io]
            [clojure.string :as string])
  (:import (java.io ByteArrayOutputStream)
           (java.util.zip ZipInputStream)))

(defn unzip_apply
  "docstring"
  [path apply_stream]
  (let [stream (->
                 (io/input-stream path)
                 (ZipInputStream.))]
    (.getNextEntry stream)
    (println (slurp stream))
    )
  )

;(defn unzip-and-read [zip-file]
;  (with-open [stream (-> zip-file io/input-stream ZipInputStream.)]
;    (loop []
;      (let [entry (.getNextEntry stream)]
;        (when entry
;          (let [file-name (.getName entry)
;                content ( (clojure.java.io/copy stream (clojure.java.io/file "/path/to/output/file")) )]
;            (println "文件名:" file-name)
;            (println "内容:" content)
;            (recur)))))))

;(defn unzip-and-read [zip-file]
;  (with-open [stream (-> zip-file io/input-stream ZipInputStream.)]
;    (loop []
;      (let [entry (.getNextEntry stream)]
;        (when entry
;          (let [file-name (.getName entry)
;                out (ByteArrayOutputStream.)
;                _ (io/copy stream out)
;                content (.toString out "UTF-8")]
;            (println "文件名:" file-name)
;            (println "内容:" content)
;            (recur)))))))

(defn unzip-and-read [zip-file filename]
  (with-open [stream (-> zip-file io/input-stream ZipInputStream.)]
    (loop []
      (let [entry (.getNextEntry stream)]
        (when entry
          (let [file-name (.getName entry)
                ]
            ; (println "zip文件名:" file-name)
            (if (= file-name filename)
              (let [out (ByteArrayOutputStream.)
                    _ (io/copy stream out)
                    content (.toString out "UTF-8")
                    ]
                (println (str "查询到目标类: " filename))
                content
                )
              (recur))
            ))))))