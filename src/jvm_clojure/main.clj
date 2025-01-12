(ns jvm-clojure.main
  (:require [clojure.tools.cli :as cli-tool])
  )

(def cli-options
  [["-h" "--help" "Show help"]
   ["-v" "--version" "Show version"]
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

(defn -main [& args]
  (let [[status msg] (validate-args args)]
    (if (= status :ok)
      (let [{:keys [classpath arguments]} msg]
        (println (format "classpath: %s, arguments: %s" classpath arguments))
        )
      (do
        (println msg)
        (System/exit 1)))))