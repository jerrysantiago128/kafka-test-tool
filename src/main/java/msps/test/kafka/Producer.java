package msps.test.kafka;


//imports
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.File;



//defining the class
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class.getSimpleName());
    //private static final String ApplicationName = "Producer";
    private static KafkaProducer<String, String> producer;
    
    public static void main(String[] args) throws InterruptedException, ExecutionException{

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
        ArrayList<String> messages = new ArrayList<>();


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
                messages.add(args[i]);
            }
        }

        // wait for Kafka Broker
        waitForKafkaBroker(BOOTSTRAP_SERVERS);
        // create a single producer rather than one for each message
        KafkaProducer producer = construct_producer(BOOTSTRAP_SERVERS);

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
                    // return;
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

    private static void waitForKafkaBroker(String bootstrapServers) throws InterruptedException, ExecutionException {
        Properties adminClientProps = new Properties();
        adminClientProps.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        try (AdminClient adminClient = AdminClient.create(adminClientProps)) {
            int maxAttempts = 10;
            int attempt = 1;
            while (attempt <= maxAttempts) {
                try {
                    DescribeClusterResult describeClusterResult = adminClient.describeCluster();
                    describeClusterResult.clusterId().get(); // This will throw an exception if the broker isn't available
                    logger.info("Successfully connected to Kafka broker!");
                    return; // Exit the loop if connection is successful
                } catch (ExecutionException e) {
                    logger.warn("Failed to connect to Kafka broker (attempt {}/{}), retrying in 5 seconds...", attempt, maxAttempts);
                    Thread.sleep(5000);
                    attempt++;
                }
            }
            logger.error("Failed to connect to Kafka broker after {} attempts. Exiting.", maxAttempts);
            System.exit(1); // Or throw an exception if you prefer
        }
    }

    private static void cleanup(){
        // Flush data
        producer.flush();

        // Flush and close producer
        producer.close();
    }
}

