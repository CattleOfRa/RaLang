(ns ralang.core
  (:use [clojure.algo.generic.functor :only (fmap)])
  (:use [clojure.java.io])
  (:require [clojure.string :as string])
  (:require [instaparse.core :as insta])
  (:require [clojure.walk :as walk])
  (:gen-class))

(def outputFile "output.ra")

(def parser
  "EBFN parser for RaLang."
  (insta/parser (clojure.java.io/resource "parser.bnf")))

(defn debugMessage
  "Shows debugging messages."
  [message]
  (println (string/join message)))

(defn write
  "Write bytecode to the output file."
  [bytecode]
  (spit outputFile bytecode :append true))

(defn genClass
  "Generates a class"
  [name]
  (println ".class public" (read-string name))
  (println ".super java/lang/Object")
  (println ".method public <init>()V)")
  (println "aload_0")
  (println "invokespecial java/lang/Object/<init>()V")
  (println "return")
  (println ".end method"))

(defn tokenReader
  "Reads a token."
  [token]
  (def tkey (first token))
  (def tval (second token))
  (case tkey
    :token (tokenReader tval)
    :class (genClass tval)
    (println tval)))

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

(defn readSource
  "Read source file and remove empty lines."
  [file]
  (with-open [rdr (reader file)]
    (def source
      (for [line (line-seq rdr)] (removeEmptyLineOrComment line)))
    (doseq [x (remove empty? source)]
      (tokenReader (parser x)))))

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
  (cond
    (= (count args) 1) (checkSource (nth args 0))
    :else (println "Wrong number of arguments.")))
