# RaLang

Author:	 		Daniel Pacheco<br />
Start Date:	 	18/03/2016<br />
Description:	A compiler for RaLang written in clojure to compile source code into JVM bytecode.<br />

# To-do
Last updated: 30/04/2016

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
          <li>[X] Calling functions with arguments</li>
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
            </ul>
        </ul>
      <li>[X] Store and load arguments from functions</li>
      <li>[X] Support for variables</li>
      <li>[ ] Generate conditions/if branches</li>
        <ul>
          <li>[ ] Single if and else statement</li>
          <li>[ ] Multiple if statements</li>
          <li>[ ] Boolean operations</li>
            <ul>
              <li>[ ] Equal</li>
              <li>[ ] Not equal</li>
              <li>[ ] Greater than</li>
              <li>[ ] Less than</li>
              <li>[ ] Greater than or equal to</li>
              <li>[ ] Less than or equal to</li>
            </ul>
        </ul>
    </ul>
  </li>
  <li>[ ] Testing compiler
    <ul>
      <li>[X] Program prints "Hello World" to the screen</li>
      <li>[X] Print multiple lines to the screen</li>
      <li>[X] Program adds two numbers and prints result</li>
      <li>[X] Print the output of a function which takes a string as an input and returns another string</li>
      <li>[ ] Compile simple factorial function</li>
    </ul>
  </li>
</ul>

# Example
Last updated: 30/04/2016

#####mulhello.ra
```python
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
Last updated: 29/04/2016

#####multimaths.ra
```python
module multimaths

function main([String] args) -> Void:
    print "Addition, subtraction, multiplication and division."
    print 10+10*(6-2)/2
    # 30
```

#####funccall.ra
```python
module funccall

function main([String] args) -> Void:
    print sum()

function sum() -> Int:
    return 5+5
```

#####callhello.ra
```python
module callhello

function main([String] args) -> Void:
    print message("Hello World") # Call 'message' and print output

# A function that takes a string and returns a string
function message(String text) -> String:
    return text
```

#####mulvars.ra
```python
module mulvars

function main([String] args) -> Void:
    Int a = 30
    print sum(a, 30)

function sum(Int a, Int b) -> Int:
    Int c = a+b*2
    return c
```