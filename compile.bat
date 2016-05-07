@echo off
set /p file="File name: "
call lein.bat run %file%
java -jar ../jasmin/jasmin.jar output-2.ra
set /p class="Class name: "
java %class%
call del output-2.ra
call del output-1.ra
pause
