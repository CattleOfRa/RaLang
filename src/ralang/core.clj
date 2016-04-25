(ns ralang.core
  (:use [clojure.algo.generic.functor :only (fmap)])
  (:use [clojure.java.io])
  (:require [clojure.string :as string])
  (:require [instaparse.core :as insta])
  (:require [clojure.walk :as walk])
  (:require [ralang.gen :as gen])
  (:gen-class))

(def parser
  "EBFN parser for RaLang."
  (insta/parser (clojure.java.io/resource "parser.bnf")))

(defn debugMessage
  "Shows debugging messages."
  [message]
  (println (string/join message)))

(defn tokenReader
  "Reads a token."
  [token]
  (def tkey (first token))
  (def tval (second token))
  (case tkey
    :token       (tokenReader tval)
    :keyword     (tokenReader tval)
    :module      (gen/genModule
                  (tokenReader tval))
    :modulename  (second tval)
    :mainfunc    (gen/genFunction "main" "[Ljava/lang/String;" "V")
    :print       (gen/genPrint tval)
    (println "Token is not recognised.")))

(defn removeEmptyLineOrComment
  "Find and remove empty lines and comments from the source code."
  [source]
  (def reComment #"\s*#.*")
  (def sourceWithoutComment (clojure.string/replace source reComment ""))
  (def reEmptyLine #"^\s*$")
  (def matchEmptyLine (re-matcher reEmptyLine sourceWithoutComment))
  (cond
    (= (re-find matchEmptyLine) true) source
    :else sourceWithoutComment))

(defn groupMethods
  "Group method instrunctions. View 'doc/ralang-methods.png'"
  [source]
  (def reMethod #"define")
  (def sourceAsStr (string/join "\n" source))
  (doseq [x (re-seq reMethod sourceAsStr)]
    (println x)))

(defn readSource
  "Read source file and remove empty lines."
  [file]
  (with-open [rdr (reader file)]
    (def source
      (for [line (line-seq rdr)] (removeEmptyLineOrComment line)))
    (doseq [x (remove empty? source)]
      (def parse (parser x))
      (tokenReader parse))
    (gen/genEndFunction "return")))

(defn checkSource
  "Check if source file exists."
  [file]
  (debugMessage ["Reading '" file "'"])
  (cond
    (.exists (as-file file)) (readSource file)
    :else (debugMessage ["Source file '" file "' does not exist."])))

(defn -main
  "RaLang's main function."
  [& args]
  (gen/initOutput)
  (cond
    (= (count args) 1) (checkSource (nth args 0))
    :else (println "Wrong number of arguments."))
  (debugMessage ["Finished compiling."]))
