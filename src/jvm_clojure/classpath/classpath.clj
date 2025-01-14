(ns jvm-clojure.classpath.classpath
  (:require [jvm-clojure.classpath.entry :as entry])
  )


(defprotocol ClasspathProtocol
  (read-class [this class-name])
  (tostring [this]))

(defn find-class [entry classname]
  (println (format "find-class entry: %s classname: %s" entry classname))
  (let [res (entry/read-class entry classname)]
    res))

(defrecord Classpath [boot-classpath ext-classpath user-classpath]
  ClasspathProtocol
  (read-class [this classname]
    (let [class-fullname (str classname ".class")
          cp-list (list boot-classpath ext-classpath user-classpath)]
      (->>
        cp-list
        (map #(find-class % class-fullname))
        (map #(:data %))
        (some #(not-empty %))))
    )
  (tostring [this]
    (entry/tostring user-classpath)
    )
  )

(defn get-jre-dir
  [opt]
  (if (= opt "")
    (if (System/getenv "JAVA_HOME")
      (str (System/getenv "JAVA_HOME"))
      "/usr/lib/jvm/java-8-openjdk/jre")
    )
  )
(defn parseBootAndExtClasspath [jar-opt]
  (let [get-jre-dir (get-jre-dir jar-opt)
        boot-classpath (entry/newWildcardEntry (str get-jre-dir "/lib/*"))
        ext-classpath (entry/newWildcardEntry (str get-jre-dir "/lib/ext/*"))
        ]
    {:boot-classpath boot-classpath :ext-classpath ext-classpath}
    )
  )
(defn parseUserClasspath [cp-opt]
  (if (= "" cp-opt)
    {:user-classpath (entry/newEntry ".")}
    {:user-classpath (entry/newEntry cp-opt)})
  )
(defn parse [jar-opt cp-opt]
  (let [{:keys [boot-classpath ext-classpath user-classpath]} (merge (parseBootAndExtClasspath jar-opt) (parseUserClasspath cp-opt))]
    (new Classpath boot-classpath ext-classpath user-classpath)
    )
  )


