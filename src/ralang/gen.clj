(ns ralang.gen
  (:gen-class))

(def outputFile "output.ra")

(defn write
  "Write bytecode to the output file."
  [bytecode]
  (spit outputFile bytecode :append true))

(defn genClass
  "Generates a class."
  [name]
  (println ".class public" (second name))
  (println ".super java/lang/Object")
  (println ".method public <init>()V)")
  (println "    aload_0")
  (println "    invokespecial java/lang/Object/<init>()V")
  (println "    return")
  (println ".end method"))
