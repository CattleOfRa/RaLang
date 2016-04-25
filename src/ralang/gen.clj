(ns ralang.gen
  (:gen-class))

(def outputFile "output.ra")

(defn write
  "Write bytecode to the output file."
  [bytecode]
  (spit outputFile bytecode :append true))

(defn genModule
  "Generates a module."
  [id]
  (println ".class public" id)
  (println ".super java/lang/Object")
  (println ".method public <init>()V)")
  (println "    aload_0")
  (println "    invokespecial java/lang/Object/<init>()V")
  (println "    return")
  (println ".end method"))

(defn genFunction
  "Generates a new function.
    name  - Function's name.
    ar    - Function's arguments.
    rt    - Function's return type."
  [name, ar, rt]
  (println ".method public static" (str name "(" ar ")" rt))
  (println "    .limit stack 50")
  (println "    .limit locals 50"))

(defn genPrint
  "Generates a print statement"
  [content]
  (println "    getstatic java/lang/System/out Ljava/io/PrintStream;")
  (println "    ldc" content)
  (println "    invokevirtual java/io/PrintStream/println(I)V"))
