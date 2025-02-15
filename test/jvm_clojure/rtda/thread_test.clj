(ns jvm-clojure.rtda.thread-test
  (:require [clojure.test :refer :all])
  (:require [jvm-clojure.rtda.thread :refer :all]))

; func startJVM(cmd *Cmd) {
;frame := rtda.NewFrame(100, 100)
;testLocalVars(frame.LocalVars())
;testOperandStack(frame.OperandStack())
;}
; func testLocalVars(vars rtda.LocalVars) {
;vars.SetInt(0, 100)
;vars.SetInt(1, -100)
;vars.SetLong(2, 2997924580)
;vars.SetLong(4, -2997924580)
;vars.SetFloat(6, 3.1415926)
;vars.SetDouble(7, 2.71828182845)
;vars.SetRef(9, nil)
;println(vars.GetInt(0))
;println(vars.GetInt(1))
;println(vars.GetLong(2))
;println(vars.GetLong(4))
;println(vars.GetFloat(6))
;println(vars.GetDouble(7))
;println(vars.GetRef(9))
;}
(deftest newFrame-test
  (testing "newFrame"
    (let [frame (newFrame 100 100)
          local-vars (:local-vars frame)
          d1 (setInt local-vars 0 100)
          d2 (setInt d1 1 -100)
          d3 (setLong d2 2 2997924580)
          d4 (setLong d3 4 -2997924580)
          d5 (setFloat d4 6 3.1415926)
          d6 (setDouble d5 7 2.71828182845)
          d7 (setRef d6 9 nil)
          ]
      (is (= 100 (getInt d7 0)))
      (is (= -100 (getInt d7 1)))
      (is (= 2997924580 (getLong d7 2)))
      (is (= -2997924580 (getLong d7 4)))
      (is (= 3.1415926 (getFloat d7 6)))
      (is (= 2.71828182845 (getDouble d7 7)))
      (is (= nil (getRef d7 9)))
      )
    )
  )
