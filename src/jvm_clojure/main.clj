(ns jvm-clojure.main
  (:require [clojure.string :as string]
            [clojure.tools.cli :as cli-tool]
            [jvm-clojure.classpath.classpath :as classpath]
            )
  )

(def cli-options
  [["-h" "--help" "Show help"]
   ["-v" "--version" "Show version"]
   ["-X" "--xjre XJRE" "Path to jre"
    :default ""
    :parse-fn identity]
   ["-c" "--classpath CLASSPATH" "Classpath"
    :default ""
    :parse-fn identity]
   ])

(defn usage [options-summary]
  (->> ["这是一个程序"
        ""
        "选项:"
        options-summary]
       (clojure.string/join \newline)))

(defn error-msg [errors]
  (str "错误:\n\n" (clojure.string/join \newline errors)))

(defn validate-args
  "验证命令行参数。返回[true]或[false 错误消息]"
  [args]
  (let [{:keys [options errors summary arguments]} (cli-tool/parse-opts args cli-options)]
    (cond
      (:help options)
      [:exit (usage summary)]

      (:version options)
      [:exit "version 1.0.0"]

      errors
      [:exit (error-msg errors)]

      :else
      [:ok (merge options {:arguments arguments})])))

(defn start-jvm [xjre classpath arguments]
  (println (format "classpath: %s, xjre:%s arguments: %s" classpath xjre arguments))
  (let [classname (string/replace (first arguments) #"\." "/")
        args (rest arguments)
        class-data (classpath/read-class (classpath/parse xjre classpath) classname)]
    (if (not-empty class-data)
      (println "class Found: " (->> class-data (map #(format "%02x" %))))
      (println "No found class"))))

; lein run java.lang.Object
(defn -main [& args]
  (let [[status msg] (validate-args args)]
    (if (= status :ok)
      (let [{:keys [classpath xjre arguments]} msg]
        (start-jvm xjre classpath arguments)
        )
      (do
        (println msg)
        (System/exit 1)))))