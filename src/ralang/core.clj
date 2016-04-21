(ns ralang.core
  (:use [clojure.algo.generic.functor :only (fmap)])
  (:use [clojure.java.io])
  (:require [clojure.string :as string])
  (comment (:require [clojure.edn]))
  (comment (:require [rhizome.dot]))
  (comment (:require [rhizome.viz]))
  (:require [instaparse.core :as insta])
  (:require [clojure.walk :as walk])
  (:gen-class))

(def outputFile "output.ra")

(def parser
  "EBFN parser for RaLang."
  (insta/parser (clojure.java.io/resource "parser.bnf")))

(comment (defn look [x]
  (println x)
  (cond
    (number? x) x
    :else (look (x 1)))))

(defn debugMessage [message]
  "Shows debugging messages."
  (println (string/join message)))

(defn write [bytecode]
  "Write bytecode to the output file."
  (spit outputFile bytecode :append true))

(defn tokenReader [token]
  "Reads a token."
  (def t (parser token))
  (println (apply array-map t))
  (comment (insta/visualize t))
  (println (second t)))

(defn removeEmptyLineOrComment [source]
  "Find and remove empty lines and comments from the source code."
  (def reComment #"\s*#.*")
  (def sourceWithoutComment (clojure.string/replace source reComment ""))
  (def reEmptyLine #"^\s*$")
  (def matchEmptyLine (re-matcher reEmptyLine sourceWithoutComment))
  (cond
    (= (re-find matchEmptyLine) true) source
    :else sourceWithoutComment))

(defn readSource [file]
  "Read source file and remove empty lines."
  (with-open [rdr (reader file)]
    (def source
      (for [line (line-seq rdr)] (removeEmptyLineOrComment line)))
    (doseq [x (remove empty? source)]
      (tokenReader x))))

(defn checkSource [file]
  "Check if source file exists."
  (debugMessage ["Reading '" file "'"])
  (cond
    (.exists (as-file file)) (readSource file)
    :else (debugMessage ["Source file '" file "' does not exist."])))

(defn -main
  "RaLang's main function."
  [& args]
  (cond
    (= (count args) 1) (checkSource (nth args 0))
    :else (println "Wrong number of arguments."))
  
  (comment
    (def f (parser "age = 20 + (3 - 5)"))
    (insta/visualize f)
    (walk/postwalk #(do (println %) %) f)))
