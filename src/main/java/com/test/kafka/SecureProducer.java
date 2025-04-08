package com.test.kafka;


//imports
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.ArrayList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.File;



//defining the class
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class.getSimpleName());
    
    private int NumOfBasicConfigProps = 2;
    private int NumOfSecurityConfigProps = 2;
    private int NumOfFileMessageProps = 2;
    private static KafkaProducer<String, String> producer;
    
    public static void main(String[] args) {

        //validate test case 1 
        // check for proper system argument list
        if (args.length < 3) {
            logger.error("You have only provided {} of the 3 minimum arguments.\n\n\n"+ 
                "Usage: Producer <bootstrap-servers> <topic-name> [FLAG] <message or file path> \n" +
                "Where <bootstrap-servers> is a individual string or path to a file.\n" +
                "Where <topic-name> is a individual string or path to a file.\n" +
                "Where [FLAG] is [-s] or [--security] to signify a security protocol the default value is PLAINTEXT.\n" +
                "Where [FLAG] is [-f] or [--file] to signify sending a file.\n" +
                "Where <message> is a individual string or path to a file.\n", args.length);
                //System.exit(1);
        }
        
        // initialize application variables with system arguments 
        final String BOOTSTRAP_SERVERS = args[0];
    	final String TOPIC_NAME = args[1];
        // check for security flag
        boolean shouldSecure =(args[2].equals("-s") || args[2].equals("--security")) && (!args[4].equals("-f") || !args[4].equals("--file")); // overengineered??
        boolean shouldReadFile = (args[2].equals("-f") || args[2].equals("--file"));
        boolean shouldSecureAndReadFile = (args[2].equals("-s") || args[2].equals("--security")) && (args[4].equals("-f") || args[4].equals("--file"));
        final String SECURITY_STRING;

        //initialize messages ArrayList
        ArrayList<String> messages = new ArrayList<>();

        if (shouldSecureAndReadFile){
            if(validateSecurity(args[3])){
                // some logic for security; maybe return the security string
                SECURITY_STRING = args[3];
                // additional logic for truststore??
            }
            else {
                // security string invalid
            }
            String filePath = args[5];
            if(validateFilePath(filePath)){
                // some logic for the file path
            }
            else {
                // file path not valid
            }
        }
        else if (shouldSecure && args.length == 4){
            //String security = args[3];
            if(validateSecurity(args[3])){
                // some logic for security; proabably just set the security string to pass to the properties function
                SECURITY_STRING = args[3];
                // additional logic for truststore??
            }
            else{
                logger.error("Security string {} is not a valid string", args[3])
            }
        }
        else if (shouldSecure && args.length < 4){
                // security string invalid
                logger.error("Flag {} specified but no security string given.", args[2]);
            }
        else if (shouldReadFile && args.length == 4){
            String filePath = args[3];
            if(validateFilePath(filePath)){
                messages.add(readFile(filePath));
            }
            else{
                // file not valid
            }
        }
        else if (shouldReadFile && args.length < 4){
            logger.error("Flag {} specified but no file path given.", args[2]);
        }
        else { // no security or file
            for (int i = 2; i < args.length; i++ ){
                messages.add(args[i]);
            }
        }

        // create a produce depending on the security configuration
        if(SECURITY_STRING == null){
            // create a single producer rather than one for each message
            KafkaProducer producer = construct_producer(BOOTSTRAP_SERVERS);
        }
        else {
            // create a single secure producer rather than one for each message
            KafkaProducer producer = construct_producer(BOOTSTRAP_SERVERS, SECURITY_STRING);

        }
        
        // while messages in ArrayList exist create a producer and send a message
        for( String message : messages){
            // create a producer and send a messages of messages[i] over the topic
            produce(producer, TOPIC_NAME, message);
        }
        // clean up producer after sending messages
        cleanup();
    }

    private static KafkaProducer construct_producer(String BOOTSTRAP_SERVERS){

        // Create Producer Properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // Create the Producer
        producer = new KafkaProducer<>(properties);

        return producer;
    }

    private static KafkaProducer construct_producer(String BOOTSTRAP_SERVERS, String SECURITY_STRING){

        // Create Producer Properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        
        if(SECURITY_STRING == "PLAINTEXT"){
            logger.info("Setting Security string  to {}", SECURITY_STRING);
        }
        else if(SECURITY_STRING == "SSL"){
            producerProps.setProperty("security.protocol", SECURITY_STRING);
            producerProps.setProperty("ssl.truststore.location", "/path/to/kafka.truststore.jks"); // Replace
            producerProps.setProperty("ssl.truststore.password", "your-truststore-password"); // Replace
        }
        else if(SECURITY_STRING == "SASL_PLAINTEXT"){
            producerProps.setProperty("security.protocol", SECURITY_STRING);
            producerProps.setProperty("sasl.mechanism", "PLAIN"); // Or SCRAM-SHA-256, SCRAM-SHA-512, GSSAPI
            producerProps.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"your-username\" password=\"your-password\";"); // Replace
        }
        else if(SECURITY_STRING == "SASL_SSL"){
            producerProps.setProperty("security.protocol", SECURITY_STRING);
            producerProps.setProperty("sasl.mechanism", "PLAIN"); // Or SCRAM-SHA-256, SCRAM-SHA-512, GSSAPI
            producerProps.setProperty("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"your-username\" password=\"your-password\";"); // Replace
            producerProps.setProperty("ssl.truststore.location", "/path/to/kafka.truststore.jks"); // Replace
            producerProps.setProperty("ssl.truststore.password", "your-truststore-password"); // Replace


        }
        // Create the Producer
        producer = new KafkaProducer<>(properties);

        return producer;
    }

    private static void produce(KafkaProducer producer, String TOPIC_NAME, String messages){ 

        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, messages); //add the 'objectKey' string???

        // Send data - asynchronous
        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                logger.info("Message Sent on: \n" +
                        "Topic: " + metadata.topic() + "\n" +
                        "Partition: " + metadata.partition() + "\n" +
                        "Offset: " + metadata.offset() + "\n" +
                        "Timestamp: " + metadata.timestamp() + "\n");
                logger.info("Message successfully sent! \n");
            } else {
                logger.error("Error while producing", exception);
            }
        });
    }

    //FIX THIS; change logic 
    private static boolean validateSecurity(String securityArg){
        if(securityArg == null || securityArg.trim().isEmpty()){
            logger.error("Security string {} is null or empty.", securityArg);
            return false;
        }

        if(securityArg == "PLAINTEXT"){
            logger.info("Setting Security string  to {}", securityArg);
            return true;
        }
        else if(securityArg == "SSL"){
            logger.info("Setting Security string  to {}", securityArg);
            return true;
        }
        else if(securityArg == "SASL_PLAINTEXT"){
            logger.info("Setting Security string  to {}", securityArg);
            return true;
        }
        else if(securityArg == "SASL_SSL"){
            logger.info("Setting Security string  to {}", securityArg);
            return true;
        }
        else {
            logger.error("Security string {} is not valid", securityArg);
            // return bool
            return false;
        }

    }
    private static boolean validateFilePath(String filePath){

        if(filePath == null || filePath.trim().isEmpty()){
            logger.error("File path {} is null or empty.", filePath);
            return false;
        }

        Path path = Paths.get(filePath);

        if(!Files.exists(path)){
            logger.error("File {} does not exist", path);
            return false;
        }

        if(!Files.isRegularFile(path)){
            logger.error("File {} is not a regular file", path);
            return false;
        }

        if(!Files.isReadable(path)){
            logger.error("File {} is not readable", path);
            return false;
        }
        // return bool
        return true;
    }

    // read the contents of the file provided, and return the contents as a String
    private static String readFile(String filePath){
        // check the file type
        String contentString = "";
        String message = "";
        if(filePath.toLowerCase().endsWith(".json")){
            // READ THE JSON FILE
            try{
                contentString =  new String(Files.readAllBytes(Paths.get(filePath)));
                if((contentString.trim().startsWith("{") && contentString.trim().endsWith("}")) || (contentString.trim().startsWith("[") && contentString.trim().endsWith("]"))){
                    message = contentString;   
                }
                else {
                    logger.error("File {} does not appear to be a valid JSON object or array. \n" +
                        "It may be invalid or null.", filePath);
                    System.exit(1);
                }
            } catch (Exception e){
                logger.error("Unable to read JSON file into message.");
            }
        }
        else if (filePath.toLowerCase().endsWith(".xml")){
            // READ THE XML FILE
            try{
                contentString = new String(Files.readAllBytes(Paths.get(filePath)));

                if(contentString.trim().startsWith("<") && contentString.trim().endsWith(">")){
                    message = contentString;
                }
                else {
                    logger.error("File {} does not appear to be a valid XML document.", filePath);
                    System.exit(1);
                }
            } catch (Exception e){
                logger.error("Unable to read XML file into message.");
            }
        }
        return message;
    }

    private static void cleanup(){
        // Flush data
        producer.flush();
        // Flush and close producer
        producer.close();
    }
}

