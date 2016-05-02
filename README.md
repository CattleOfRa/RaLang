# RaLang

Author:	 		Daniel Pacheco<br />
Start Date:	 	02/05/2016<br />
Description:	A compiler for RaLang written in clojure to compile source code into Jasmin (.j) which then compiles to JVM
                bytecode. As stated in Jasmin's about page: "Generating a binary Java .class file is pretty fiddly. Its like
                creating an a.out (or .exe) file by hand. Even using a Java package like JAS (a Java API for creating class
                files, used internally by Jasmin and written by KB Sriram), you need to know a lot about the philosophy of the
                Java Virtual Machine before you can write something at the Virtual Machine level and generate a Java class."
                and "We wanted something that made it very easy for a student or programmer to explore the Java Virtual Machine,
                or write a new language which targets the VM, without getting into the details of constant pool indices,
                attribute tables".<br />

# To-do
Last updated: 02/05/2016

<ul>
  <li>[X] Check if source file exists</li>
  <li>[X] Reading source file</li>
  <li>[X] Implement lexer and parser</li>
  <li>[ ] Code generation
    <ul>
      <li>[X] Generate class</li>
      <li>[X] Generate functions</li>
        <ul>
          <li>[X] Generate main function</li>
          <li>[X] Generate .endmethod to end function</li>
          <li>[X] Generate basic function (takes args and returns values)</li>
          <li>[X] Enable calling functions</li>
          <li>[X] Calling functions with 1 argument</li>
          <li>[X] Expressions within function arguments</li>
          <li>[X] Calling functions with more than 1 argument</li>
        </ul>
      <li>[ ] Determine stack limit and local variables limit</li>
      <li>[X] Generate print statement</li>
      <li>[X] Perform basic calculations</li>
        <ul>
          <li>[X] Add, sub, mul, div</li>
            <ul>
              <li>[X] 2 numbers</li>
              <li>[X] 3 numbers</li>
              <li>[X] 1 number and a variable</li>
              <li>[X] 2 variables</li>
              <li>[X] 1 number and a function call</li>
            </ul>
        </ul>
      <li>[X] Store and load arguments from functions</li>
      <li>[X] Support for variables</li>
      <li>[ ] Generate conditions/if branches</li>
        <ul>
          <li>[X] Single if and else statement</li>
          <li>[ ] Multiple if statements</li>
          <li>[X] Boolean operations</li>
            <ul>
              <li>[X] Equal</li>
              <li>[X] Not equal</li>
              <li>[X] Greater than/Greater than or equal to</li>
              <li>[X] Less than/Less than or equal to</li>
            </ul>
        </ul>
    </ul>
  </li>
  <li>[X] Testing compiler
    <ul>
      <li>[X] Program prints "Hello World" to the screen</li>
      <li>[X] Print multiple lines to the screen</li>
      <li>[X] Program adds two numbers and prints result</li>
      <li>[X] Print the output of a function which takes a string as an input and returns another string</li>
      <li>[X] Compile simple factorial function</li>
      <li>[X] Test all different conditionals</li>
    </ul>
  </li>
</ul>

# Example
Last updated: 02/05/2016

#####mulhello.ra
```ruby
module mulhello

function main([String] args) -> Void:
    print "Multi line hello world!"
    print "Hi there."
```

We can compile this code to Jasmin using `lein.bat run test/mulhello.ra`, this will generate `output.ra`. Then we can compile Jasmin Bytecode to JVM Bytecode using `java -jar ../jasmin/jasmin.jar output.ra` (in your case you might have to provide the full path to your Jasmin installation). To run our first application in Ralang we can type `java mulhello`, this will output 2 lines to the console:

```
Multi line hello world!
Hi there.
```

# More sample code
Last updated: 02/05/2016

#####multimaths.ra
```ruby
module multimaths

function main([String] args) -> Void:
    print "Addition, subtraction, multiplication and division."
    print 10+10*(6-2)/2
    # 30
```

#####funccall.ra
```ruby
module funccall

function main([String] args) -> Void:
    print sum()

function sum() -> Int:
    return 5+5
```

#####callhello.ra
```ruby
module callhello

function main([String] args) -> Void:
    print message("Hello World") # Call 'message' and print output

# A function that takes a string and returns a string
function message(String text) -> String:
    return text
```

#####mulvars.ra
```ruby
module mulvars

function main([String] args) -> Void:
    Int a = 30
    print sum(a, 30)

function sum(Int a, Int b) -> Int:
    Int c = a+b*2
    return c
```

#####mulcompare.ra
```ruby
module mulcomp

function main([String] args) -> Void:
    print compare(2016, 2016)
    # "Not same year."

    print compare(2015, 2017)
    # "Same year."

    print notCompare(2016, 2016)
    # "Not same year."

    print notCompare(2015, 2017)
    # "Same year."

function notCompare(Int year1, Int year2) -> String:
    if year1 != year2:
        return "Not same year."
    else:
        return "Same year."

function compare(Int year1, Int year2) -> String:
    if year1 == year2:
        return "Same year."
    else:
        return "Not same year."
```

#####factorial.ra
```ruby
module factorial

function main([String] args) -> Void:
    print "Calculating factorial of 5."
    print factorial(5)
    # 120

function factorial(Int n) -> Int:
    # Calculates the factorial of n
    if n < 2:
        return 1
    else:
        return n*factorial(n-1)
```