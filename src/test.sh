#!/bin/sh

# Compiling
javac -cp ".;../lib/json.jar" unsw/venues/*.java

# Comparing sample
java -ea -cp ".;../lib/json.jar" unsw/venues/VenueHireSystem < ../test/sample_input.json > out
diff out ../test/sample_output.json

# Testing Room Functionality
java -ea -cp ".;../lib/json.jar" unsw/venues/VenueHireSystem < ../test/input1.json > out
diff out ../test/output1.json

# Testing Request Functionality
java -ea -cp ".;../lib/json.jar" unsw/venues/VenueHireSystem < ../test/input2.json > out
diff out ../test/output2.json

# Testing Change Functionality
java -ea -cp ".;../lib/json.jar" unsw/venues/VenueHireSystem < ../test/input3.json > out
diff out ../test/output3.json

# Testing Cancel Functionality
java -ea -cp ".;../lib/json.jar" unsw/venues/VenueHireSystem < ../test/input4.json > out
diff out ../test/output4.json

# Testing List Functionality
java -ea -cp ".;../lib/json.jar" unsw/venues/VenueHireSystem < ../test/input5.json > out
diff out ../test/output5.json

# Integration Testing
java -ea -cp ".;../lib/json.jar" unsw/venues/VenueHireSystem < ../test/input6.json > out
diff out ../test/output6.json

# Delete temporary file
rm out
