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
          ]
      (setInt local-vars 0 100)
      (setInt local-vars 1 -100)
      (setLong local-vars 2 2997924580)
      (setLong local-vars 4 -2997924580)
      (setFloat local-vars 6 3.1415926)
      (setDouble local-vars 7 2.71828182845)
      (setRef local-vars 9 nil)
      (is (= 100 (getInt local-vars 0)))
      (is (= -100 (getInt local-vars 1)))
      (is (= 2997924580 (getLong local-vars 2)))
      (is (= -2997924580 (getLong local-vars 4)))
      (is (= 3.1415926 (getFloat local-vars 6)))
      (is (= 2.71828182845 (getDouble local-vars 7)))
      (is (= nil (getRef local-vars 9)))
      )
    )
  )
