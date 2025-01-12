(ns jvm-clojure.classpath.entry-test
  (:require [clojure.test :refer :all])
  (:require [jvm-clojure.classpath.entry :refer :all])
  )

(deftest newDirEntry-test
  (testing "abc"
    (is (= "abc"
           (:data (read-class (newDirEntry "/home/huyujing/IdeaProjects/jvm-clojure/resources/testfiles") "test-file-abc.txt")))))

  (testing "abc"
    (is (= "123"
           (:data (read-class (newZipEntry "/home/huyujing/IdeaProjects/jvm-clojure/resources/testfiles/test-zip-file.zip") "abcd")))))
  )
