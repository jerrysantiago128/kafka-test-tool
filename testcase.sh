#!/bin/bash





# test case for less than 3 arguments; should fail
./Produce.sh kafka-main:9092 dynamic 
sleep 2

# test case for 3 or more valid arguments; should send X messages
./Produce.sh kafka-main:9092 dynamic test 
sleep 2

# gets file flag but not file ("-f"); should fail -> no file specified
./Produce.sh kafka-main:9092 dynamic -f 
sleep 2
# gets file flag but not file ("--file"); should fail -> no file specified
./Produce.sh kafka-main:9092 dynamic --file 
sleep 2
# attempts to send file but no valid file specified; should fail -> not valid file
./Produce.sh kafka-main:9092 dynamic -f test 
sleep 2
# attempts to send json file; if valid file, should work; if not valid json file, should fail
./Produce.sh kafka-main:9092 dynamic -f jsonObject.json 
sleep 2
# attempts to send json file; if valid file, should work; if not valid json file, should fail
./Produce.sh kafka-main:9092 dynamic -f jsonArray.json 
sleep 2
# attempts to send xml file; should work, if not valid xml file should fail;
./Produce.sh kafka-main:9092 dynamic -f xmlFile.xml 
sleep 2
# attempts to send json file with "--file"; if valid file, should work; if not valid json file, should fail
./Produce.sh kafka-main:9092 dynamic --file jsonObject.json 
sleep 2
# attempts to send json file; if valid file, should work; if not valid json file, should fail
./Produce.sh kafka-main:9092 dynamic --file jsonArray.json 
sleep 2
# attempts to send xml file; should work, if not valid xml file should fail;
./Produce.sh kafka-main:9092 dynamic --file xmlFile.xml 
sleep 2
