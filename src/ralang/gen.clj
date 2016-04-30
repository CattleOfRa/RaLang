(ns ralang.gen
  (:use [clojure.java.io])
  (:require [clojure.string :as string])
  (:gen-class))

(declare tokenReader)
(declare genReturn)
(def output1        "output-1.ra")
(def output2        "output-2.ra")
(def indent         (string/join (repeat 4 " ")))
(def functionsTable (hash-map))
(def j_string       "Ljava/lang/String;")
(def j_print        "invokevirtual java/io/PrintStream/println")

(defn initOutput
  "Deletes output1 and output2 iff they already exist." []
  (.delete (clojure.java.io/file output1))
  (.delete (clojure.java.io/file output2)))

(defn write
  "Write bytecode to the output file."
  [file, bytecode]
  (spit file (str bytecode "\n") :append true)
  (def lastWritten bytecode))

(defn getType
  "Returns JVM type."
  [type]
  (case (str type)
    ":int"    (str "i")
    ":string" (str "a")
    (str type)))

(defn getMethodType
  "Returns JVM type for methods."
  [type]
  (case (str type)
    ":string" (str j_string)
    ":int"    (str "I")
    ":float"  (str "F")
    (str "")))

(defn convertDatatype
  "Converts datatype from ralang to JVM."
  [type]
  (cond
    (= (first type) :array) (str "[" (tokenReader (second type)))
    :else (case type
            "String" (str j_string)
            "Int"    (str "I")
            "Void"   (str "V")
            (str type))))

(defn storeArgs
  "Read function arguments and store them."
  [args]
  (def localCount 0)
  (def localVariables {})
  (doseq [x args]
    (def aType (tokenReader (nth x 1)))
    (def aName (tokenReader (nth x 2)))
    (cond
      (= aType j_string) (def aType "a"))
    (def localVariables (merge localVariables {aName (str (string/lower-case aType) "load_" localCount)}))
    (def localCount (inc localCount))))

(defn storeVariables
  "Read and store variable."
  [variables]
  (doseq [x variables]
    (def aType (tokenReader (nth x 1)))
    (def aName (tokenReader (nth x 2)))
    (def localVariables (merge localVariables {aName (str (string/lower-case aType) "load_" localCount)}))
    (write output1 (str indent (string/lower-case aType) "store_" localCount))
    (def localCount (inc localCount))))

(defn readTuple
  "Function argument's tuple reader."
  [tuple]
  (storeArgs tuple)
  (def args (apply str (map #(tokenReader (nth % 1)) tuple)))
  (str "(" args ")"))

(defn genModule
  "Generates a module."
  [id]
  (initOutput)
  (write output1 (str ".class public " id))
  (write output1 ".super java/lang/Object")
  (write output1 ".method public <init>()V")
  (write output1 "    aload_0")
  (write output1 "    invokespecial java/lang/Object/<init>()V")
  (def moduleName id))

(defn genEndMethod
  "Generates a end method."
  []
  (write output1 ".end method"))

(defn genFunction
  "Generates a new function.
    name  - Function's name.
    ar    - Function's arguments.
    rt    - Function's return type."
  [name, ar, rt]
  (cond
    (not (= lastWritten ".end method")) (do (genReturn output1 nil) (genEndMethod)))
  (write output1 (str ".method public static " name ar rt))
  (write output1 "    .limit stack 50")
  (write output1 "    .limit locals 50")
  (def functionsTable (merge functionsTable {name (str ar rt)})))

(defn storeFunctionName
  "Stores the current function name to enable us to create local variables."
  [name]
  (def functionName name))

(defn genReturn
  "Generates the ending of the function.
    rt    - Function's return type."
  [file, rt]
  (def returnType (getType rt))
  (write file (str indent returnType "return")))

(defn genFunctionCallPlaceHolder
  "Generates a placeholder for function calls."
  [token]
  (def fName (tokenReader token))
  (write output1 (str "->fc" fName))
  fName)

(defn genOutput2
  "Generates:
    Function calls.
    Missing print statements." []
  (with-open [rdr (reader output1)]
    (doseq [line (line-seq rdr)]
      (def oPart (split-at 4 line))
      (def oCode (apply str (first oPart)))
      (def oArgs (apply str (second oPart)))
      (case oCode
        "->fc" (write output2 (str indent "invokestatic " moduleName "/" oArgs (first (map functionsTable [oArgs]))))
        "->pr" (write output2 (str indent j_print "(" (second (string/split (first (map functionsTable [oArgs])) #"\(.*\)")) ")V"))
        (write output2 line))))
  ; The following checks for proper ending to a simple program with only 1 main function.
  ; It checks if the last line ends in ".end method"
  (with-open [rdr (reader output2)]
    (cond
      (not (= (last (line-seq rdr)) ".end method")) (genReturn output2 nil))))

(defn genLdc
  "Generates a LDC. Returns type (string, int)."
  [content]
  (write output1 (str "    ldc " (second content)))
  (first content))

(defn genFunctionCallArgs
  "Pushes function call arguments to the stack."
  [args]
  (doseq [x args]
    (tokenReader x)))

(defn genPrintOrPlaceHolder
  "Generates a print statement or a placeholder for a print statement."
  [content]
  (write output1 "    getstatic java/lang/System/out Ljava/io/PrintStream;")
  (cond
    (= (first content) :funccall) (genFunctionCallArgs (rest (nth content 2))))
  (def type (tokenReader content))
  (case (str (first content))
    ":funccall" (write output1 (str "->pr" (second (second content))))
    (write output1 (str indent j_print "(" (getMethodType type) ")V"))))

(defn genArithmetic
  "Generates an arithmetic expression for a particular type."
  [arith, numbers]
  (tokenReader (first numbers))
  (def type (tokenReader (second numbers)))
  (def dType (getType type))
  (write output1 (str indent dType arith))
  (str type))

(defn genVariable
  "Reads and stores a variable."
  [variable]
  (def name (conj () (first variable)))
  (def value (second variable))
  ; Value is the expression of the variable which gets calculated before assignment.
  (tokenReader value)
  (storeVariables name))

(defn genLocalVar
  "Gets a local variable by name."
  [variable]
  (def expression (map localVariables [variable]))
  (write output1 (str indent (first expression)))
  (first (first expression)))

(defn tokenReader
  "Reads a token."
  [token]
  (def tkey (first token))
  (def tval (second token))
  (def trst (rest token))
  (println token)
  (cond
    (= tkey :funcname) (storeFunctionName (tokenReader tval)))
  
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
    :funccall   (genFunctionCallPlaceHolder tval)
    :variable   (genVariable trst)
    :varName    (genLocalVar tval)
    :varID      (storeVariables trst)
    :datatype   (convertDatatype tval)
    :print      (genPrintOrPlaceHolder tval)
    :return     (do
                  (genReturn output1 (tokenReader tval))
                  (genEndMethod))
    :string     (genLdc token)
    :toStr      (tokenReader tval)
    :expr       (tokenReader tval)
    :add        (genArithmetic "add" trst)
    :sub        (genArithmetic "sub" trst)
    :mul        (genArithmetic "mul" trst)
    :div        (genArithmetic "div" trst)
    :num        (tokenReader tval)
    :int        (genLdc token)
    :float      (genLdc token)
    :double     (genLdc token)
    :tuple      (readTuple trst)
    token))
