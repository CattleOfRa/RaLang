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
      <li>[+] Generate print statement</li>
        <ul>
          <li>[X] A string</li>
          <li>[ ] A number</li>
          <li>[ ] A variable</li>
        </ul>
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
    </ul>
  </li>
  <li>[+] Testing compiler
    <ul>
      <li>[X] Program prints "Hello World" to the screen</li>
      <li>[ ] Program adds two numbers and prints result</li>
      <li>[ ] Compile simple factorial function</li>
    </ul>
  </li>
</ul>

# Example
Last updated: 25/04/2016

#####hello.ra
```javascript
module hello

function main:
    print "Hello World!"
```

We can compile this code to Jasmin using `lein.bat run hello.ra`, this will generate `output.ra`. Then we can compile Jasmin Bytecode to JVM Bytecode using `java -jar jasmin/jasmin.jar output.ra`. To run our first application in Ralang we can type `java hello`, this will output 'Hello World!' to the console.