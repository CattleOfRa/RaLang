(ns ralang.gen
  (:use [clojure.java.io])
  (:require [clojure.string :as string])
  (:gen-class))

(declare tokenReader)
(def outputFile "output.ra")
(def indent (string/join (repeat 4 " ")))
(def j_string "Ljava/lang/String;")
(def j_print "invokevirtual java/io/PrintStream/println")

(defn initOutput [] (.delete (clojure.java.io/file outputFile)))

(defn write
  "Write bytecode to the output file."
  [bytecode]
  (spit outputFile (str bytecode "\n") :append true))

(defn getType
  "Returns JVM type."
  [type]
  (case (str type)
    ":string" (str j_string)
    ":int"    ("I")
    type))

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

(defn genLdc
  "Generates a LDC. Returns type (string, int)."
  [content]
  (write (str "    ldc " (second content)))
  (first content))

(defn genPrint
  "Generates a print statement"
  [content]
  (write "    getstatic java/lang/System/out Ljava/io/PrintStream;")
  (def type (getType (tokenReader content)))
  (write (str indent j_print "(" type ")V")))

(defn genBipush
  "Generates a bipush."
  [number]
  (write (str "    bipush " number)))

(defn genIadd
  "Generates a iadd"
  [numbers]
  (tokenReader (first numbers))
  (tokenReader (second numbers))
  (write "    iadd"))

(defn tokenReader
  "Reads a token."
  [token]
  (def tkey (first token))
  (def tval (second token))
  (println token)
  (case tkey
    :token      (tokenReader tval)
    :keyword    (tokenReader tval)
    :module     (tokenReader tval)
    :modulename (genModule (second tval))
    :mainfunc   (genFunction "main" "[Ljava/lang/String;" "V")
    :print      (genPrint tval)
    :string     (genLdc token)
    :toStr      (tokenReader tval)
    :expr       (tokenReader tval)
    :add        (genIadd (rest token))
    :num        (genBipush tval)
    token))
