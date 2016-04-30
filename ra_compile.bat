@echo off
set /p file="File name: "
call lein.bat run test/%file%
java -jar ../jasmin/jasmin.jar output-2.ra
ra_run.bat