@echo off
set /p id="File name: "
lein.bat run %id%
java -jar jasmin/jasmin.jar output.ra