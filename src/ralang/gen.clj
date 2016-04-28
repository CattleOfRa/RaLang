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
    ":int" (str "i")
    (str type)))

(defn getMethodType
  "Returns JVM type for methods."
  [type]
  (case (str type)
    ":string" (str j_string)
    ":int"    (str "I")
    ":float"  (str "F")
    (str type)))

(defn convertDatatype
  "Converts datatype from ralang to JVM."
  [type]
  (def a
    (cond
      (= (first type) :array) (str "[" (tokenReader (second type)))
      :else (case type
              "String" (str j_string)
              "Int"    (str "I")
              "Void"   (str "V")
              (str type))))
  (println "convertDatatype a:" a)
  a)

(defn readTuple
  "Function argument's tuple reader."
  [tuple]
  (println "readTuple:" tuple)
  (def b (str "(" (str (doseq [t tuple] (tokenReader (nth t 1)))) ")"))
  (def c (apply str (map #(tokenReader (nth % 1)) tuple)))
  (str "(" c ")"))

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
  (write   (str ".method public static " name ar rt))
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
  (def type (tokenReader content))
  (write (str indent j_print "(" (getMethodType type) ")V")))

(defn genAdd
  "Generates an add statement for a particular type."
  [numbers]
  (tokenReader (first numbers))
  (def type (tokenReader (second numbers)))
  (def dType (getType type))
  (write (str indent dType "add"))
  (str type))

(defn genArithmetic
  "Generates an arithmetic expression for a particular type."
  [arith, numbers]
  (tokenReader (first numbers))
  (def type (tokenReader (second numbers)))
  (def dType (getType type))
  (write (str indent dType arith))
  (str type))

(defn tokenReader
  "Reads a token."
  [token]
  (def tkey (first token))
  (def tval (second token))
  (println "--------------------------------")
  (println "tkey:" tkey)
  (println "tval:" tval)
  (case tkey
    :token      (tokenReader tval)
    :keyword    (tokenReader tval)
    :id         (str tval)
    :module     (genModule (tokenReader tval))
    :modulename (tokenReader tval)
    :function   (genFunction
                 (tokenReader tval)
                 (tokenReader (nth token 2))
                 (tokenReader (nth token 3)))
    :funcname   (tokenReader tval)
    :datatype   (convertDatatype tval)
    :print      (genPrint tval)
    :string     (genLdc token)
    :toStr      (tokenReader tval)
    :expr       (tokenReader tval)
    :add        (genArithmetic "add" (rest token))
    :sub        (genArithmetic "sub" (rest token))
    :mul        (genArithmetic "mul" (rest token))
    :div        (genArithmetic "div" (rest token))
    :num        (tokenReader tval)
    :int        (genLdc token)
    :float      (genLdc token)
    :double     (genLdc token)
    :tuple      (readTuple (rest token))
    token))
