(defproject jvm-clojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [
                 [org.clojure/clojure "1.11.1"]
                 [org.clojure/tools.cli "1.1.230"]]
  :main jvm-clojure.main
  :profiles {:uberjar {:aot :all}
             :dev     {:dependencies [[speclj "3.3.2"]]}}
  :repl-options {:init-ns jvm-clojure.core})
