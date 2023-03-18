# Create working directory
mkdir target;
# Compile *.java files to target director
javac -d target src/java/edu/school21/printer/app/Program.java src/java/edu/school21/printer/logic/ImageConverter.java;
# Execute program
java -classpath target edu.school21.printer.app.Program --black=. --white=# --image=[FILE_PATH];
