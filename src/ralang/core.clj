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

(defn removeEmptyLineOrComment
  "Find and remove empty lines and comments from the source code."
  [source]
  (def reComment #"\s*#.*")
  (def reEmptyLine #"^\s+$")
  (def sourceWithoutComment
    (clojure.string/replace source reComment ""))
  (clojure.string/replace sourceWithoutComment reEmptyLine ""))

(defn readSource
  "Read source file and remove empty lines."
  [file]
  (with-open [rdr (reader file)]
    (def source
      (for [line (line-seq rdr)]
        (removeEmptyLineOrComment line)))
    (doseq [x (remove empty? source)]
      (def parse (parser x))
      (gen/tokenReader parse)))
  (gen/genEndMethod)
  (gen/genOutput2))

(defn -main
  "RaLang's main function."
  [& args]
  (if (= (count args) 0) (throw (Exception. "Wrong number of arguments.")))
  (def fileName (nth args 0))
  (cond
    (.exists (as-file fileName)) (readSource fileName)
    :else (throw (Exception. "Source file does not exist."))))