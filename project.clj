(defproject ralang "0.1.0-SNAPSHOT"
  :description "Compiler for Ra programming language."
  :url "https://github.com/CattleOfRa/RaLang"
  :plugins [[speclj "2.9.0"]]
  :dependencies [[org.clojure/clojure "1.8.0"]]
  :test-paths ["spec"]
  :main ralang.core)