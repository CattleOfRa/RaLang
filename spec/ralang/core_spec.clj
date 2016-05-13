(ns ralang.core-spec
    (:require [speclj.core :refer :all])
    (:require [ralang.core :refer :all]))
    
(defn specljTest [] true)
    
(describe "Unit tests"
    (it "Tests if Speclj works properly."
        (should specljTest)))
        
(describe "Initialise compiler"
    (it "-main: With 0 arguments"
        (should-throw (-main))))
    (it "-main: With 1 argument and a file that does not exist"
        (should-throw (-main "does-not-exist.ra")))
    (it "-main: With 1 argument and a file that exists"
        (should= nil (-main "testcases/factorial.ra")))
        
(describe "Read source file"
    (it "readSource: Reads a file that exists"
        (should= nil (readSource "testcases/factorial.ra"))))

(describe "Remove empty line or comment"
    (it "removeEmptyLineOrComment: A comment"
        (should= "" (removeEmptyLineOrComment "# Comment")))
    (it "removeEmptyLineOrComment: Code and a comment"
        (def sampleCode "print 5")
        (should= sampleCode
            (removeEmptyLineOrComment (str sampleCode "# Comment"))))
    (it "removeEmptyLineOrComment: 1 space"
        (should= "" (removeEmptyLineOrComment " ")))
    (it "removeEmptyLineOrComment: Empty string"
        (should= "" (removeEmptyLineOrComment ""))))
        
(describe "Parsing code and generating tokens"
    (it "parser: An integer"
        (should= [:token [:keyword [:expr [:num [:int "5"]]]]] (parser "5")))
    (it "parser: A float"
        (should= [:token [:keyword [:expr [:num [:float "3.14"]]]]] (parser "3.14")))
    (it "parser: Print integer"
        (should= [:token [:keyword [:print [:expr [:num [:int "20"]]]]]] (parser "print 20")))
    (it "parser: Print float"
        (should= [:token [:keyword [:print [:expr [:num [:float "129.34"]]]]]] (parser "print 129.34")))
    (it "parser: Print string"
        (should= [:token [:keyword [:print [:string "\"Hello World\""]]]] (parser "print \"Hello World\"")))
    (it "parser: Print result of adding two integer"
        (should= [:token [:keyword [:print [:expr [:add [:num [:int "50"]] [:num [:int "25"]]]]]]] (parser "print 50+25")))
    (it "parser: Print result of subtracting two integer"
        (should= [:token [:keyword [:print [:expr [:sub [:num [:int "50"]] [:num [:int "25"]]]]]]] (parser "print 50-25")))
    (it "parser: Print result of multiplying two integer"
        (should= [:token [:keyword [:print [:expr [:mul [:num [:int "50"]] [:num [:int "25"]]]]]]] (parser "print 50*25")))
    (it "parser: Print result of dividing two integer"
        (should= [:token [:keyword [:print [:expr [:div [:num [:int "50"]] [:num [:int "2"]]]]]]] (parser "print 50/2")))
    (it "parser: Print result of adding and multiplying"
        (def e [:token [:keyword [:print [:expr [:add [:num [:int "50"]] [:mul [:num [:int "10"]] [:num [:int "2"]]]]]]]])
        (should= e (parser "print 50+10*2")))
    (it "parser: Print result of adding, multiplying and subtracting"
        (def e [:token
               [:keyword
               [:print
               [:expr
                    [:sub
                        [:add
                            [:num [:int "50"]]
                            [:mul
                                [:num [:int "10"]]
                                [:num [:int "2"]]]]
                        [:num [:int "30"]]]]]]])
        (should= e (parser "print 50+10*2-30")))
    (it "parser: Print result of adding, multiplying, subtracting and dividing"
        (def e [:token
               [:keyword
               [:print
               [:expr
                    [:sub
                        [:add
                            [:div
                                [:num [:int "50"]]
                                [:num [:int "2"]]]
                            [:mul
                                [:num [:int "10"]]
                                [:num [:int "2"]]]]
                        [:num [:int "30"]]]]]]])
        (should= e (parser "print 50/2+10*2-30")))
    (it "parser: Print result of adding, multiplying, subtracting, dividing and parentheses"
        (def e [:token
               [:keyword
               [:print
               [:expr
                    [:sub
                        [:div
                            [:mul
                                [:num [:int "2"]]
                                [:add
                                    [:num [:int "20"]]
                                    [:num [:int "30"]]]]
                            [:num [:int "5"]]]
                        [:num [:int "5"]]]]]]])
        (should= e (parser "print 2*(20+30)/5-5")))
    (it "parser: New module"
        (should= [:token [:module [:modulename [:id "NewModule"]]]] (parser "module NewModule")))
    (it "parser: Int variable"
        (def e [:token [:variable [:varID [:datatype "Int"] [:id "age"]] [:expr [:num [:int "55"]]]]])
        (should= e (parser "Int age = 55")))
    (it "parser: Float variable"
        (def e [:token [:variable [:varID [:datatype "Float"] [:id "speed"]] [:expr [:num [:float "32.5"]]]]])
        (should= e (parser "Float speed = 32.5")))
    (it "parser: String variable"
        (def e [:token [:variable [:varID [:datatype "String"] [:id "name"]] [:expr [:num [:string "Daniel"]]]]])
        (should= e (parser "String name = \"Daniel\"")))
    (it "parser: New function with 0 args and returns Void"
        (def e [:token [:function [:funcname [:id "leFunction"]] [:tuple] [:datatype "Void"]]])
        (should= e (parser "function leFunction() -> Void:")))
    (it "parser: New function with 0 args and returns Int"
        (def e [:token [:function [:funcname [:id "leFunction"]] [:tuple] [:datatype "Int"]]])
        (should= e (parser "function leFunction() -> Int:")))
    (it "parser: New function with 0 args and returns Float"
        (def e [:token [:function [:funcname [:id "leFunction"]] [:tuple] [:datatype "Float"]]])
        (should= e (parser "function leFunction() -> Float:")))
    (it "parser: New function with 0 args and returns String"
        (def e [:token [:function [:funcname [:id "leFunction"]] [:tuple] [:datatype "String"]]])
        (should= e (parser "function leFunction() -> String:")))
    (it "parser: New function with 1 arg and returns Void"
        (def e [:token
               [:function
               [:funcname
                    [:id "leFunction"]]
                    [:tuple
                        [:varID [:datatype "Int"] [:id "a"]]]
                    [:datatype "Void"]]])
        (should= e (parser "function leFunction(Int a) -> Void:")))
    (it "parser: New function with 1 arg and returns Int"
        (def e [:token
               [:function
               [:funcname
                    [:id "leFunction"]]
                    [:tuple
                        [:varID [:datatype "Int"] [:id "a"]]]
                    [:datatype "Int"]]])
        (should= e (parser "function leFunction(Int a) -> Int:")))
    (it "parser: New function with 1 arg and returns Float"
        (def e [:token
               [:function
               [:funcname
                    [:id "leFunction"]]
                    [:tuple
                        [:varID [:datatype "Int"] [:id "a"]]]
               [:datatype "Float"]]])
        (should= e (parser "function leFunction(Int a) -> Float:")))
    (it "parser: New function with 1 arg and returns String"
        (def e [:token
               [:function
               [:funcname
                    [:id "leFunction"]]
                    [:tuple
                        [:varID [:datatype "Int"] [:id "a"]]]
                    [:datatype "String"]]])
        (should= e (parser "function leFunction(Int a) -> String:")))
    (it "parser: New function with 2 args and returns Void"
        (def e [:token
               [:function
               [:funcname
                    [:id "leFunction"]]
                    [:tuple
                        [:varID [:datatype "Int"] [:id "a"]]
                        [:varID [:datatype "Int"] [:id "b"]]]
                    [:datatype "Void"]]])
        (should= e (parser "function leFunction(Int a, Int b) -> Void:")))
    (it "parser: New function with 2 args and returns Int"
        (def e [:token
               [:function
               [:funcname
                    [:id "leFunction"]]
                    [:tuple
                        [:varID [:datatype "Int"] [:id "a"]]
                        [:varID [:datatype "Int"] [:id "b"]]]
                    [:datatype "Int"]]])
        (should= e (parser "function leFunction(Int a, Int b) -> Int:")))
    (it "parser: New function with 2 args and returns Float"
        (def e [:token
               [:function
               [:funcname
                    [:id "leFunction"]]
                    [:tuple
                        [:varID [:datatype "Int"] [:id "a"]]
                        [:varID [:datatype "Int"] [:id "b"]]]
                    [:datatype "Float"]]])
        (should= e (parser "function leFunction(Int a, Int b) -> Float:")))
    (it "parser: New function with 2 args and returns String"
        (def e [:token
               [:function
               [:funcname
                    [:id "leFunction"]]
                    [:tuple
                        [:varID [:datatype "Int"] [:id "a"]]
                        [:varID [:datatype "Int"] [:id "b"]]]
                    [:datatype "String"]]])
        (should= e (parser "function leFunction(Int a, Int b) -> String:"))))

(run-specs)