@echo off
set /p file="File name: "
call lein.bat run %file%
java -jar ../jasmin/jasmin.jar output.ra
set /p class="Class name: "
java %class%
pause