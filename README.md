# Distributed Processing System for Industrial Protocols

Description:

This repository contains the source code of the diploma thesis titled 'Distributed Processing System for Industrial Protocols' developed at the National Technical University of Athens (NTUA). The project implements a robust, scalable, and real-time data processing system designed to handle and process multi-source and heterogeneous data originating from various industrial protocols.

The system provides features like fault tolerance, real-time data processing, scalability, performance monitoring, and a timely alert system for potential issues. Additionally, it can automatically trigger actions based on incoming data to prevent potential industrial accidents.
Project Components:

- Data Simulation: Temperature, pressure, and power sensors' data is simulated using Python.
- Data Transfer: The Modbus protocol and the PLC4X library are utilized for transferring the sensor data to the Kafka system.
- Data Processing: Kafka Streams are employed for real-time data processing.
- Data Storage: Processed data is stored in an InfluxDB database, while the most recent data values are stored in-memory using the Redis system.
- Application: The entire application is implemented in Java using the Spring Boot framework.
- Data Visualization: Data visualization is achieved via Grafana.
- Performance Monitoring: Performance metrics are monitored using Prometheus, cAdvisor, and Node Exporter.

This project serves as a comprehensive example of an end-to-end solution for industrial data processing systems. It provides a solid foundation for further development and improvements in the realm of real-time data processing and management systems in industrial settings.

## Features

- Multiple producers/consumers using Spring's Beans
- Multiple measurement types (temperature, power, pressure)
- Spring profiles for environment shift (dev,test,prod)
- Exports metrics to JMX, Prometheus, Graphite
- Enabled Log compression and retention by default
- Status endpoints for app: `/app/info/status`, `/app/info/healch`, `/app/info/buildinfo` etc
- gc logs are enabled and written to file
- springdoc-openapi-ui for swagger-ui
- spring-boot-admin for monitoring and managing the app
- Dead letter queue (DLQ) for failed messages
- Processes messages in real-time using Kafka Streams
- Stores latest values of aggregation in Redis
- Sends alerts using smtp mail server
- Stores processed data in InfluxDB
- Grafana for data visualization
- Prometheus for performance monitoring
- cAdvisor for container monitoring
- Node Exporter for host monitoring
- Kafka Connect for data transfer
- Confluent Kafka Control Center for monitoring and managing the Kafka cluster
- Confluent Schema Registry for storing and retrieving Avro schemas
- User Interface for managing the app and getting extra info
- Real-time data visualization using Grafana, InfluxDB and custom javascript with websockets protocol
- Use docker-compose to run the app


## Pre-requisites

- Docker 20.0.0 or higher
- Docker-compose 2.0.0 or higher

## How to run

- Go to project's parent folder. Then:
- `mvn clean install` (first time only)
- `docker-compose up -d`
or 
- `COMPOSE_DOCKER_CLI_BUILD=1 DOCKER_BUILDKIT=1 docker compose up -d` (uses caching for the builds)

The first time you run the app, it will take some time to download the images and build the containers.

To rebuild the containers, use `docker-compose build --no-cache streaming_iot` or `docker-compose up -d --build`  
To stop the app, use `docker-compose down` or `docker-compose down -v` to remove volumes as well (e.g. kafka data or influxdb data)

## How to use 

After that, the app will start and you can visit the following urls (all are links in localhost):
- [Swagger UI](http://localhost:10000/streaming-iot/swagger-ui/index.html). Use the endpoints to send data to the app.
- [App UI](http://localhost:10000/streaming-iot/overview). Navigate to multiple pages to see the functionality. Use the side menu to navigate.
- [Grafana](http://localhost:3000) (username: admin, password: admin). Got to `Dashboards -> Manage` to see the available dashboards.
- [Prometheus](http://localhost:9090). Explore the available metrics.
- [Kafka Control Center](http://localhost:9021)
- [Spring Boot Admin](http://localhost:10000)
- [InfluxDB](http://localhost:8086). Use `admin` as username and `mySecurePassword` as password. Load the `./doc/influx_db_dashboard_export/ntua_streaming_iot_measurements.json` file to see real time ingested data in a dashboard.

## Info

Thesis by Skourtsidis Giorgos, National Technical University of Athens, School of Electrical and Computer Engineering (ECE-NTUA), 2022     
Associate Professor: Verena Kantere
Supervisor: Paraskevas Kerasiotis  
Project: Distributed Processing System for Industrial Protocols  

### Topic organization

Topic per measurement type. This is just an example. The exact number of topics and partitions can be configured in `application.yml` file.
Each sensor has a unique id. The id is used as a key for the message. The key is used to partition the messages. 
This way, all messages from the same sensor will be stored in the same partition. This is useful for aggregations.
```bash
  temperature(0)   |   power(1)   |   pressure(2)
  -----------------|--------------|---------------
   0  |  1  |  2   |    0  |  1   |  0  |  1  |  2
  -----------------|--------------|---------------
  27.1| 155.4| 90.3|    638|  722 | 22.1| 12.8| 21.5
  27.3| 157.9| 89.6|    652|  713 | 21.9| 12.6| 21.8
  27.2| 156.2| 89.9|    640|  720 | 22.0| 12.7| 21.7
  27.1| 155.4| 90.3|    638|  722 | 22.1| 12.8| 21.5
  ```

### InfluxDB connector

`curl -X POST -H "Content-Type: application/json" --data @connect-influxdb-sink-temperature.json http://localhost:8083/connectors`
