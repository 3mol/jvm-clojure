(ns jvm-clojure.classpath.classpath-test
  (:require [clojure.test :refer :all])
  (:require [jvm-clojure.classpath.classpath :refer :all]))

(deftest parse-test
  (testing "parse"
    ; notnull
    (is (not-empty (parse "" "")))
    (is (not-empty (read-class (parse "" "") "java/lang/Object")))
    )
  )
