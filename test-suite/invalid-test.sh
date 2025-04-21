# valid test cases

######## TESTING BASIC PRDOCUER ########
# test case for 3 or more valid arguments; should send X messages
./Produce.sh kafka-main:9092 dynamic testMessage messageTest testMessage
sleep 2


######## TESTING BASIC PRDOCUER WITH VALID FILE ########
# testing "-f" and "--file" flags
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


######## TESTING SECURE PRDOCUER  WITH MESSAGES ########

######## USING "-s" FLAG ########

# attempts to set security protocol as PLAINTEXT -> should work bc no message sent
./Produce.sh kafka-main:9092 dynamic -s PLAINTEXT testMessage messageTest testMessage
sleep 2

# attempts to set security protocol as SASL_PLAINTEXT -> should work
./Produce.sh kafka-main:9092 dynamic -s SASL_PLAINTEXT testMessage messageTest testMessage
sleep 2

# attempts to set security protocol as SSL; should work bc of  truststore and password
#./Produce.sh kafka-main:9092 dynamic -s SSL /path/to/file.jks password testMessage messageTest testMessage
#sleep 2

# attempts to set security protocol as SASL_SSL; should work bc of  truststore and password
#./Produce.sh kafka-main:9092 dynamic -s SASL_SSL /path/to/file.jks password testMessage messageTest testMessage
#sleep 2


######## USING "--security" FLAG ########

# attempts to set security protocol as PLAINTEXT -> should work bc no message sent
./Produce.sh kafka-main:9092 dynamic --security PLAINTEXT testMessage messageTest testMessage
sleep 2

# attempts to set security protocol as SASL_PLAINTEXT -> should work
./Produce.sh kafka-main:9092 dynamic --security SASL_PLAINTEXT testMessage messageTest testMessage
sleep 2

# attempts to set security protocol as SSL; should work bc of  truststore and password
#./Produce.sh kafka-main:9092 dynamic --security SSL /path/to/file.jks password testMessage messageTest testMessage
#sleep 2

# attempts to set security protocol as SASL_SSL; should work bc of  truststore and password
#./Produce.sh kafka-main:9092 dynamic --security SASL_SSL /path/to/file.jks password testMessage messageTest testMessage
#sleep 2


######## TESTING SECURE PRDOCUER  WITH JSON FILES ########

######## USING "-s"  and "-f" FLAG ########

# attempts to set security protocol as PLAINTEXT -> should work bc no message sent
./Produce.sh kafka-main:9092 dynamic -s PLAINTEXT -f jsonObject.json
sleep 2

# attempts to set security protocol as SASL_PLAINTEXT -> should work
./Produce.sh kafka-main:9092 dynamic -s SASL_PLAINTEXT -f jsonObject.json
sleep 2

# attempts to set security protocol as SSL; should work bc of  truststore and password
#./Produce.sh kafka-main:9092 dynamic -s SSL /path/to/file.jks password -f jsonObject.json
#sleep 2

# attempts to set security protocol as SASL_SSL; should work bc of  truststore and password
#./Produce.sh kafka-main:9092 dynamic -s SASL_SSL /path/to/file.jks password -f jsonObject.json
#sleep 2


######## USING "--security" and "--file" FLAG ########

# attempts to set security protocol as PLAINTEXT -> should work bc no message sent
./Produce.sh kafka-main:9092 dynamic --security PLAINTEXT --file jsonObject.json
sleep 2

# attempts to set security protocol as SASL_PLAINTEXT -> should work
./Produce.sh kafka-main:9092 dynamic --security SASL_PLAINTEXT --file jsonObject.json
sleep 2

# attempts to set security protocol as SSL; should work bc of  truststore and password
#./Produce.sh kafka-main:9092 dynamic --security SSL /path/to/file.jks password --file jsonObject.json
#sleep 2

# attempts to set security protocol as SASL_SSL; should work bc of  truststore and password
#./Produce.sh kafka-main:9092 dynamic --security SASL_SSL /path/to/file.jks password --file jsonObject.json
#sleep 2


######## TESTING SECURE PRDOCUER  WITH XML FILES ########

######## USING "-s" FLAG ########

# attempts to set security protocol as PLAINTEXT -> should work bc no message sent
./Produce.sh kafka-main:9092 dynamic -s PLAINTEXT -f xmlFile.xml
sleep 2

# attempts to set security protocol as SASL_PLAINTEXT -> should work
./Produce.sh kafka-main:9092 dynamic -s SASL_PLAINTEXT -f xmlFile.xml
sleep 2

# attempts to set security protocol as SSL; should work bc of  truststore and password
#./Produce.sh kafka-main:9092 dynamic -s SSL /path/to/file.jks password -f xmlFile.xml
#sleep 2

# attempts to set security protocol as SASL_SSL; should work bc of  truststore and password
#./Produce.sh kafka-main:9092 dynamic -s SASL_SSL /path/to/file.jks password -f xmlFile.xml
#sleep 2


######## USING "--security" FLAG ########

# attempts to set security protocol as PLAINTEXT -> should work bc no message sent
./Produce.sh kafka-main:9092 dynamic --security PLAINTEXT --file xmlFile.xml
sleep 2

# attempts to set security protocol as SASL_PLAINTEXT -> should work
./Produce.sh kafka-main:9092 dynamic --security SASL_PLAINTEXT --file xmlFile.xml
sleep 2

# attempts to set security protocol as SSL; should work bc of  truststore and password
#./Produce.sh kafka-main:9092 dynamic --security SSL /path/to/file.jks password --file xmlFile.xml
#sleep 2

# attempts to set security protocol as SASL_SSL; should work bc of  truststore and password
#./Produce.sh kafka-main:9092 dynamic --security SASL_SSL /path/to/file.jks password --file xmlFile.xml
#sleep 2
