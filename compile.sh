echo "File name: "
read file
lein run $file
java -jar ../jasmin/jasmin.jar output2
rm output2
rm output1
echo "Class name: "
read class
java $class
