package com.test.kafka;

//imports
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

//defining that class
public class Consumer {

    private static final Logger logger = LoggerFactory.getLogger(Consumer.class.getSimpleName());
    
    public static void main(String[] args) {


        //validate test case 1 
        // check for proper system argument list
        if (args.length < 3) {
            logger.error("You have only provided {} of the 3 minimum arguments.\n\n\n"+ 
                "Usage: Producer <bootstrap-servers> <topic-name> <group-id> \n" +
                "Where <bootstrap-servers> is a individual string or path to a file.\n" +
                "Where <topic-name> is a individual string or path to a file.\n" +
                "Where <group-id> is a individual string or path to a file.\n", args.length);
                //System.exit(1);
        }
        
        final String BOOTSTRAP_SERVERS = args[0];
        final String TOPIC_NAME = args[1];
        final String GROUP_ID = args[2];

        consume(BOOTSTRAP_SERVERS, TOPIC_NAME, GROUP_ID);

    }

    private static void consume(String BOOTSTRAP_SERVERS, String TOPIC_NAME, String GROUP_ID){

        // Create Consumer based on Properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Create the Consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // Subscribe to the topic
        consumer.subscribe(Collections.singleton(TOPIC_NAME));

        //start consumer and poll for new data
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            for (ConsumerRecord<String, String> record : records) {
                logger.info("Key: " + record.key() + ", Value: " + record.value());
                logger.info("Partition: " + record.partition() + ", Offset: " + record.offset());
            }
        }
    }
}

