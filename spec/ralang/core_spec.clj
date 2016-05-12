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

(run-specs)