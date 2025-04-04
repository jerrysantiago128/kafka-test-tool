# Kafka Producer and Consumer Example

This project demonstrates a simple Apache Kafka producer and consumer implemented in Java. The Kafka services (Zookeeper and Kafka broker) run in Docker containers using Docker Compose, while the Java source code for the producer and consumer runs outside.

## Prerequisites

- [Docker](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/) installed.
- Java JDK (version 8 or above) installed.
- [Maven](https://maven.apache.org/install.html) installed.

## Files Overview

- **docker-compose.yml**: Docker Compose file to run Zookeeper and Kafka.
- **pom.xml**: Maven configuration with the Kafka client dependency.
- **src/ProducerApp.java**: Java application to produce messages.
- **src/ConsumerApp.java**: Java application to consume messages.
- **README.md**: This file.

## Steps to Run

### 1. Start Kafka Services with Docker Compose

Navigate to the project directory and run:

```bash
docker-compose up -d

