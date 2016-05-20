@echo off
set /p file="File name: "
call lein run %file%
java -jar ../jasmin/jasmin.jar output2
del output1
del output2
set /p class="Class name: "
java %class%
pause