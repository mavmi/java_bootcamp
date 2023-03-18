# Create working directory
mkdir target;
# Compile *.java files with external libraries to target director
javac -classpath ".:./lib/JColor-5.5.1.jar:./lib/jcommander-1.82.jar" -d target src/java/edu/school21/printer/app/Program.java src/java/edu/school21/printer/logic/ImageConverter.java;
# Copy recoursec and manifest to target directory
cp -r src/resources target/.;
cp src/manifest.txt target/.;
# Extract libraries' files and move them to target directory
jar xf lib/JColor-5.5.1.jar com;
jar xf lib/jcommander-1.82.jar com;
mv com target/com;
# Build executable jar
jar cmf target/manifest.txt target/Program.jar -C target edu -C target com -C target resources;
# Execute program
java -jar target/Program.jar --white=red --black=green;
