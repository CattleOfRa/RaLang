# RaLang

Author:	 		Daniel Pacheco<br />
Start Date:	 	18/03/2016<br />
Description:	A compiler for RaLang written in clojure to compile source code into JVM bytecode.<br />

# To-do
Last updated: 25/04/2016

<ul>
  <li>[X] Check if source file exists</li>
  <li>[X] Reading source file</li>
  <li>[X] Implement lexer and parser</li>
  <li>[+] Code generation
    <ul>
      <li>[X] Generate class</li>
      <li>[+] Generate functions</li>
        <ul>
          <li>[X] Generate main function</li>
          <li>[X] Generate .endmethod to end function</li>
          <li>[ ] Generate basic function (takes args and returns values)</li>
          <li>[ ] Determine stack limit and local variables limit</li>
        </ul>
      <li>[X] Generate print statement</li>
      <li>[ ] Perform basic calculations</li>
        <ul>
          <li>[ ] Add, sub, mul, div 2 numbers</li>
            <ul>
              <li>[ ] 2 numbers</li>
              <li>[ ] 3 numbers</li>
              <li>[ ] 1 number and a variable</li>
              <li>[ ] 2 variables</li>
            </ul>
        </ul>
      <li>[ ] Generate conditions/if branches</li>
      <li>[ ] Support for variables</li>
      <li>[ ] Convert number, expression or variable to string</li>
    </ul>
  </li>
  <li>[+] Testing compiler
    <ul>
      <li>[X] Program prints "Hello World" to the screen</li>
      <li>[X] Print multiple lines to the screen</li>
      <li>[ ] Print the output of a function which takes a string as an input and returns another string</li>
      <li>[ ] Program adds two numbers and prints result</li>
      <li>[ ] Compile simple factorial function</li>
    </ul>
  </li>
</ul>

# Example
Last updated: 25/04/2016

#####hello.ra
```python
module hello

function main:
    print "Hello World!"
```

We can compile this code to Jasmin using `lein.bat run hello.ra`, this will generate `output.ra`. Then we can compile Jasmin Bytecode to JVM Bytecode using `java -jar ../jasmin/jasmin.jar output.ra` (in your case you might have to provide the full path to your Jasmin installation). To run our first application in Ralang we can type `java hello`, this will output 'Hello World!' to the console.

# More sample code
Last updated: 25/04/2016

#####mulhello.ra
```python
module mulhello

function main:
    print "Multi line hello world!"
    print "Hi there."
```

#####callhello.ra
```python
module callhello

function main:
    print message("Hello World") # Call 'message' and print output

# A function that takes a string and returns a string
function message(string text, string):
    return text
```