# Create working directory
mkdir target;
# Compile *.java files to target director
javac -d target src/java/edu/school21/printer/app/Program.java src/java/edu/school21/printer/logic/ImageConverter.java;
# Copy recoursec and manifest to target directory
cp -r src/resources target/.;
cp src/manifest.txt target/.;
# Build executable jar
jar cmf target/manifest.txt target/Program.jar -C target edu -C target resources;
# Execute program
java -jar target/Program.jar --black=. --white=#;
