(ns jvm-clojure.utils-test
  (:require [clojure.test :refer :all])
  (:require [jvm-clojure.utils :refer [unzip-and-read]]))

(deftest unzip_apply-test
  (testing "unzip_apply"
    (is (= "123" (unzip-and-read "/home/huyujing/IdeaProjects/jvm-clojure/resources/testfiles/test-zip-file.zip"
                                 "abcd")))
    )
  )
