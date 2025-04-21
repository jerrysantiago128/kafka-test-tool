#!/bin/bash





# test case for less than 3 arguments; should fail
./Produce.sh kafka-main:9092 dynamic 
sleep 2

# test case for 3 or more valid arguments; should send X messages
./Produce.sh kafka-main:9092 dynamic testMessage messageTest testMessage
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


########################## ADDING SECURITY TESTING ##################################

#### testing  "-s" flag with no security string

# gets security flag but no string ("-s"); should fail -> no security string specified
./Produce.sh kafka-main:9092 dynamic -s
sleep 2

# gets file flag but not string ("--security"); should fail -> no security string specified
./Produce.sh kafka-main:9092 dynamic --security
sleep 2

# attempts to set security protocol but not valid security string; should fail -> not valid security string and list out the options for security sting
./Produce.sh kafka-main:9092 dynamic -s test 
sleep 2

# attempts to set security protocol as PLAINTEXT -> should fail bc no message sent
./Produce.sh kafka-main:9092 dynamic -s PLAINTEXT 
sleep 2

# attempts to set security protocol as SSL; should fail bc its missing the truststore and password
./Produce.sh kafka-main:9092 dynamic -s SSL 
sleep 2

# attempts to set security protocol as SASL_PLAINTEXT -> should work
./Produce.sh kafka-main:9092 dynamic -s SASL_PLAINTEXT 
sleep 2

# attempts to set security protocol as SSL; should fail bc its missing the truststore and password
./Produce.sh kafka-main:9092 dynamic -s SASL_SSL 
sleep 2

############### 
#testing with "-s" flag for SSL and SASL_SSL


# attempts to set security protocol as SSL; should work bc of  truststore and password
#./Produce.sh kafka-main:9092 dynamic -s SSL /path/to/file.jks password
#sleep 2

# attempts to set security protocol as SASL_SSL; should work bc of  truststore and password
#./Produce.sh kafka-main:9092 dynamic -s SASL_SSL /path/to/file.jks password
#sleep 2


#########################
#### testing  "--security" flag with no security string

# attempts to set security protocol but not valid security string; should fail -> not valid security string and list out the options for security sting
./Produce.sh kafka-main:9092 dynamic --security test 
sleep 2

# attempts to set security protocol as PLAINTEXT -> should fails bc its missing messages
./Produce.sh kafka-main:9092 dynamic --security PLAINTEXT 
sleep 2

# attempts to set security protocol as SSL; should fail bc its missing the truststor and password
./Produce.sh kafka-main:9092 dynamic --security SSL 
sleep 2

# attempts to set security protocol as SASL_PLAINTEXT -> should fail bs its missing messages
./Produce.sh kafka-main:9092 dynamic --security SASL_PLAINTEXT 
sleep 2
# attempts to set security protocol as SSL; should fail bc its missing the truststor and password
./Produce.sh kafka-main:9092 dynamic --security SASL_SSL 
sleep 2

############### 
#testing with "--security" flag for SSL and SASL_SSL


# attempts to set security protocol as SSL; should work bc of  truststore and password but fail bc of messages
#./Produce.sh kafka-main:9092 dynamic -s SSL /path/to/file.jks password
#sleep 2

# attempts to set security protocol as SASL_SSL; should work bc of  truststore and password but fail bc of messages
#./Produce.sh kafka-main:9092 dynamic -s SASL_SSL /path/to/file.jks password
#sleep 2


####################
#### testing with security and file

# testing with invalid security string and json file
./Produce.sh kafka-main:9092 dynamic -s test -f jsonObject.json
sleep 2

# attempts to set security protocol as PLAINTEXT -> should work
./Produce.sh kafka-main:9092 dynamic -s PLAINTEXT -f jsonObject.json
sleep 2

# attempts to set security protocol as SSL; should fail bc its missing the truststore and password
./Produce.sh kafka-main:9092 dynamic -s SSL -f jsonObject.json
sleep 2

# attempts to set security protocol as SASL_PLAINTEXT -> should work
./Produce.sh kafka-main:9092 dynamic -s SASL_PLAINTEXT -f jsonObject.json
sleep 2

# attempts to set security protocol as SSL; should fail bc its missing the truststore and password
./Produce.sh kafka-main:9092 dynamic -s SASL_SSL -f jsonObject.json
sleep 2

# attempts to set security protocol as SSL; should work bc of  truststore and password and valid file
#./Produce.sh kafka-main:9092 dynamic -s SSL /path/to/file.jks password -f jsonObject.json
#sleep 2

# attempts to set security protocol as SASL_SSL; should work bc of  truststore and password and valid file
#./Produce.sh kafka-main:9092 dynamic -s SASL_SSL /path/to/file.jks password -f jsonObject.json
#sleep 2




#### testing security with message(s)
./Produce.sh kafka-main:9092 dynamic -s PLAINTEXT testMessage messageTest testMessage
sleep 2

#### testing security with message(s)
./Produce.sh kafka-main:9092 dynamic -s SASL_SSL testMessage messageTest testMessage
sleep 2

# attempts to set security protocol as SSL; should work bc of  truststore and password
#./Produce.sh kafka-main:9092 dynamic -s SSL /path/to/file.jks password testMessage messageTest testMessage
#sleep 2

# attempts to set security protocol as SSL; should work bc of  truststore and password
#./Produce.sh kafka-main:9092 dynamic -s SSL /path/to/file.jks password testMessage messageTest testMessage
#sleep 2