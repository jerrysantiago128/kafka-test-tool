package com.test.kafka;


//imports
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.json.*;
import javax.xml.*;

import java.util.Properties;
import java.util.ArrayList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.File;



//defining the class
public class ByteArrayProducer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class.getSimpleName());
    //private static final String ApplicationName = "Producer";
    private static KafkaProducer<String, byte[]> producer;
    
    public static void main(String[] args) {

        //validate test case 1 
        // check for proper system argument list
        if (args.length < 3) {
            logger.error("You have only provided {} of the 3 minimum arguments.\n\n\n"+ 
                "Usage: Producer <bootstrap-servers> <topic-name> [FLAG] <message or file path> \n" +
                "Where <bootstrap-servers> is a individual string or path to a file.\n" +
                "Where <topic-name> is a individual string or path to a file.\n" +
                "Where [FLAG] is [-f] or [--file].\n" +
                "Where <message> is a individual string or path to a file.\n", args.length);
                //System.exit(1);
        }
        
        // initialize application variables with system arguments 
        final String BOOTSTRAP_SERVERS = args[0];
    	final String TOPIC_NAME = args[1];
        boolean shouldReadFile= args[2].equals("-f") || args[2].equals("--file");

        //initialize messages ArrayList
        ArrayList<byte[]> messages = new ArrayList<>();


    // add each command line argument to messages ArrayList
    

        if (shouldReadFile && args.length == 4){

            String filePath = args[3];
            
            if(validateFilePath(filePath)){
                messages.add(readFile(filePath));
            }
        }
        else if (shouldReadFile && args.length < 4){
            
            logger.error("Flag {} specified but no file path given.", args[2]);
        }
        else {
            for (int i = 2; i < args.length; i++ ){
                try{
                    messages.add(args[i].getBytes("UTF-8"));
                }
                catch (IOException e) {
                    logger.error("Unable to read message in byte(s).", e);
                    System.exit(1); 
                }
            }
        }

        // create a single producer rather than one for each message
        KafkaProducer producer = construct_producer(BOOTSTRAP_SERVERS);

        // while messages in ArrayList exist create a producer and send a message
        for( byte[] message : messages){
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
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        // Create the Producer
        producer = new KafkaProducer<>(properties);

        return producer;
    }

    private static void produce(KafkaProducer producer, String TOPIC_NAME, byte[] messages){ 

        ProducerRecord<String, byte[]> record = new ProducerRecord<>(TOPIC_NAME, messages); //add the 'objectKey' string???

        // Send data - asynchronous
        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                logger.info("Byte Message Sent on: \n" +
                        "Topic: " + metadata.topic() + "\n" +
                        "Partition: " + metadata.partition() + "\n" +
                        "Offset: " + metadata.offset() + "\n" +
                        "Timestamp: " + metadata.timestamp() + "\n");
                logger.info("Byte Message successfully sent! \n");
            } else {
                logger.error("Error while producing", exception);
            }
        });
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

        // String validPath = filePath;
        return true;
    }

    // read the contents of the file provided, and return the contents as a String
    private static byte[] readFile(String filePath) {
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(filePath));

            // Check for JSON and XML using content sniffing (not just extension)
            if (isJSON(fileContent)) {
                logger.info("File identified as JSON.");
                // You can add specific JSON validation here if needed
            } else if (isXML(fileContent)) {
                logger.info("File identified as XML.");
                // You can add specific XML validation here if needed
            } else {
                logger.info("File not of type JSON OR XML. File treated as generic binary data.");
            }

            return fileContent;

        } catch (IOException e) {
            logger.error("Unable to read file into message.", e);
            return null; 
        }
    }

    // Helper functions to check for JSON and XML content
    private static boolean isJSON(byte[] content) {
        try {
            new org.json.JSONObject(new String(content)); // Use a JSON library for parsing
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isXML(byte[] content) {
        try {
            javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new org.xml.sax.InputSource(new java.io.ByteArrayInputStream(content)));
            return true;
        } catch (Exception e) { 
            return false;
        }
    }


    private static void cleanup(){
        // Flush data
        producer.flush();

        // Flush and close producer
        producer.close();
    }
}

