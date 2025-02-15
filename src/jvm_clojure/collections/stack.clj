(ns jvm-clojure.collections.stack)

(defn create-stack []
  (atom []))  ;; 使用 atom 存储栈元素

(defn push [stack element]
  (swap! stack conj element))  ;; 使用 conj 将元素压入栈

(defn pop [stack]
  (let [current-stack @stack]
    (if (empty? current-stack)
      (throw (Exception. "栈空，无法弹出元素"))  ;; 弹出前检查栈是否为空
      (do
        (swap! stack clojure.core/pop)  ;; swap! 修改原栈
        (last current-stack)))))  ;; 返回被弹出的元素

(defn peek [stack]
  (let [current-stack @stack]
    (if (empty? current-stack)
      (throw (Exception. "栈空，无法查看栈顶元素"))
      (last current-stack))))  ;; 返回栈顶元素但不弹出

(def my-stack (create-stack))
(push my-stack 1)
(push my-stack 2)
(println (peek my-stack))  ;; 输出 2
(println (pop my-stack))    ;; 输出 2
(println (pop my-stack))    ;; 输出 1
