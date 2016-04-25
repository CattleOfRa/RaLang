(ns ralang.gen
  (:require [clojure.string :as string])
  (:gen-class))

(def outputFile "output.ra")
(def indent (string/join (repeat 4 " ")))

(defn write
  "Write bytecode to the output file."
  [bytecode]
  (spit outputFile bytecode :append true)
  (spit outputFile "\n" :append true))

(defn genModule
  "Generates a module."
  [id]
  (println ".class public" id)
  (write   (str ".class public " id))
  (println ".super java/lang/Object")
  (write   ".super java/lang/Object")
  (println ".method public <init>()V")
  (write   ".method public <init>()V")
  (println "    aload_0")
  (write   "    aload_0")
  (println "    invokespecial java/lang/Object/<init>()V")
  (write   "    invokespecial java/lang/Object/<init>()V")
  (println "    return")
  (write   "    return")
  (println ".end method")
  (write   ".end method"))

(defn genFunction
  "Generates a new function.
    name  - Function's name.
    ar    - Function's arguments.
    rt    - Function's return type."
  [name, ar, rt]
  (println ".method public static" (str name "(" ar ")" rt))
  (write   (str ".method public static " (str name "(" ar ")" rt)))
  (println "    .limit stack 50")
  (write   "    .limit stack 50")
  (println "    .limit locals 50")
  (write   "    .limit locals 50"))

(defn genEndFunction
  "Generates the ending of the function.
    rt    - Function's return type."
  [rt]
  (println (str indent rt))
  (write   (str indent rt))
  (println ".end method")
  (write   ".end method"))

(defn genPrint
  "Generates a print statement"
  [content]
  (println "    getstatic java/lang/System/out Ljava/io/PrintStream;")
  (write   "    getstatic java/lang/System/out Ljava/io/PrintStream;")
  (println "    ldc " content)
  (write   (str "    ldc " content))
  (println "    invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V")
  (write   "    invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V"))
