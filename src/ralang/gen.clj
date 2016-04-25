(ns ralang.gen
  (:use [clojure.java.io])
  (:require [clojure.string :as string])
  (:gen-class))

(def outputFile "output.ra")
(def indent (string/join (repeat 4 " ")))

(defn initOutput [] (.delete (clojure.java.io/file outputFile)))

(defn write
  "Write bytecode to the output file."
  [bytecode]
  (spit outputFile (str bytecode "\n") :append true))

(defn genModule
  "Generates a module."
  [id]
  (write   (str ".class public " id))
  (write   ".super java/lang/Object")
  (write   ".method public <init>()V")
  (write   "    aload_0")
  (write   "    invokespecial java/lang/Object/<init>()V")
  (write   "    return")
  (write   ".end method"))

(defn genFunction
  "Generates a new function.
    name  - Function's name.
    ar    - Function's arguments.
    rt    - Function's return type."
  [name, ar, rt]
  (write   (str ".method public static " (str name "(" ar ")" rt)))
  (write   "    .limit stack 50")
  (write   "    .limit locals 50"))

(defn genEndFunction
  "Generates the ending of the function.
    rt    - Function's return type."
  [rt]
  (write   (str indent rt))
  (write   ".end method"))

(defn genPrint
  "Generates a print statement"
  [content]
  (write   "    getstatic java/lang/System/out Ljava/io/PrintStream;")
  (write   (str "    ldc " content))
  (write   "    invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V"))
