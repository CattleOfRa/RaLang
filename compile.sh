echo "File name: "
read file
lein run $file
java -jar ../jasmin/jasmin.jar output-2.ra
echo "Class name: "
read class
rm output-2.ra
rm output-1.ra
java $class
