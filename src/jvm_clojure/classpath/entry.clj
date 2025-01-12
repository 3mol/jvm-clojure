(ns jvm-clojure.classpath.entry
  (:require [clojure.java.io :as io]
            [jvm-clojure.utils :as utils]
            ))

(defprotocol Entry
  (read-class [this class-name])
  (tostring [this]))

(defrecord DirEntry [path]
  Entry
  (read-class [this class-name]
    (let [slurp-data (slurp (clojure.java.io/input-stream (str path "/" class-name)))]
      {:data slurp-data :entity this}
      )
    )
  (tostring [this]
    (str "path: " path)
    ))

(defrecord CompositeEntry [path]
  Entry
  (read-class [this class-name])
  (tostring [this]))

(defrecord WildcardEntry [path]
  Entry
  (read-class [this class-name])
  (tostring [this]))
(defrecord ZipEntry [path]
  Entry
  (read-class [this class-name]
    {:data (utils/unzip-and-read path class-name) :entity this}
    )
  (tostring [this]
    (str "path: " path)
    ))

(defn getAbsPath
  [path]
  (-> (io/file path)
      (.getAbsolutePath))
  )
(defn newDirEntry [path]
  ; relation path to abs path
  (new DirEntry (getAbsPath path)))

(defn newZipEntry [path]
  (new ZipEntry (getAbsPath path)))

(defn newCompositeEntry [path]
  (new CompositeEntry (getAbsPath path)))

(defn newWildcardEntry [path]
  (new WildcardEntry (getAbsPath path)))


(defn newEntry [path]
  ; when contains "/" -> new CompositeEntry
  ; when file ends with  "*" -> new WildcardEntry
  ; when file ends with  ".jar" or ".JAR" or ".zip" or ".ZIP" -> new ZipEntry
  ; else -> new DirEntry
  ; using cond match case.
  (cond
    (.contains path "/") (newCompositeEntry path)
    (.endsWith path "*") (newWildcardEntry path)
    (.endsWith path ".jar") (newZipEntry path)
    (.endsWith path ".JAR") (newZipEntry path)
    (.endsWith path ".zip") (newZipEntry path)
    (.endsWith path ".ZIP") (newZipEntry path)
    :else (newDirEntry path)
    )
  )

; (read-class (newDirEntry "/home/huyujing/IdeaProjects/jvm-clojure/resources/testfiles") "test-read-abc.class")