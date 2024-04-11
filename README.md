# Distributed Processing System for Industrial Protocols 🏭

**Info**  📚  
Thesis by Skourtsidis Giorgos, National Technical University of Athens, School of Electrical and Computer Engineering (ECE-NTUA), 2022     
Associate Professor: Verena Kantere  
Supervisor: Paraskevas Kerasiotis    

- [PDF (Greek)](http://artemis.cslab.ece.ntua.gr:8080/jspui/bitstream/123456789/18714/1/%ce%a3%ce%9a%ce%9f%ce%a5%ce%a1%ce%a4%ce%a3%ce%99%ce%94%ce%97%ce%a3-%ce%94%ce%99%ce%a0%ce%9b%ce%a9%ce%9c%ce%91%ce%a4%ce%99%ce%9a%ce%97-03114307.pdf) 
- [PPTX (Greek)](https://github.com/SkourtsidisGiorgos/IIoT-Data-Processing-System/blob/main/doc/thesis_presentation_greek.pptx) 

**Description** 📝

This repository contains the source code of the diploma thesis titled 'Distributed Processing System for Industrial Protocols' developed at the National Technical University of Athens (NTUA). The project implements a robust, scalable, and real-time data processing system designed to handle and process multi-source and heterogeneous data originating from various industrial protocols.

The system provides features like fault tolerance, real-time data processing, scalability, performance monitoring, and a timely alert system for potential issues. Additionally, it can automatically trigger actions based on incoming data to prevent potential industrial accidents.  

**Project Components**  🛠️

- **Data Simulation**: 🌡️ Temperature, 🗜️ Pressure, ⚡ Power sensors' data simulated using Python.
- **Data Transfer**: 🔄 Utilizes the Modbus protocol and PLC4X library for transferring sensor data to Kafka.
- **Data Processing**: 💻 Employs Kafka Streams for real-time processing.
- **Data Storage**: 🏬 Stores processed data in InfluxDB; 💾 recent data values in-memory via Redis.
- **Application**: 🌐 Implemented in Java with the Spring Boot framework.
- **Data Visualization**: 📊 Achieved through Grafana.
- **Performance Monitoring**: 📈 Monitored using Prometheus, cAdvisor, and Node Exporter.

![image](https://github.com/SkourtsidisGiorgos/IIoT-Data-Processing-System/blob/main/doc/screenshots/app-components.png)
This project serves as a comprehensive example of an end-to-end solution for industrial data processing systems. It provides a solid foundation for further development and improvements in the realm of real-time data processing and management systems in industrial settings.

## Videos 🎥
- [Mock thesis presentation (Greek)](https://www.youtube.com/watch?v=ENjL1Sam0As)
- [Quick app/code walkthrough (Greek)](https://www.youtube.com/watch?v=Ah-X_SbiWYM)

## Features 🌟

- Multiple producers/consumers
- Multiple measurement types (temperature, power, pressure)
- Spring profiles for environment shift (dev,test,prod)
- Exports metrics to JMX, Prometheus, Graphite
- Enabled Log compression and retention by default
- Status endpoints for app: `/app/info/status`, `/app/info/health`, `/app/info/buildinfo` etc
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

## Extra info 📘
This is a demo app for a thesis. It is not meant to be used in production as is, needs further development and testing.  
All the components are running in docker containers. Data are simulated using python scripts. Currently, the app is configured to run in a single node, but it can be easily configured to run in a cluster.
For communication between the app and the sensors, the Modbus protocol is used. The PLC4X library is used for communication, so we can easily add more protocols in the future or replace Modbus protocol with another one.

## Pre-requisites 📋

- Docker 20.0.0 or higher
- Docker-compose 2.0.0 or higher

## How to run 🚀

- Go to project's parent folder. Then:
- `mvn clean install` (first time only)
- `docker-compose up -d`
or 
- `COMPOSE_DOCKER_CLI_BUILD=1 DOCKER_BUILDKIT=1 docker compose up -d` (uses caching for the builds)

The first time you run the app, it will take some time to download the images and build the containers.

To rebuild the containers, use `docker-compose build --no-cache streaming_iot` or `docker-compose up -d --build`  
To stop the app, use `docker-compose down` or `docker-compose down -v` to remove volumes as well (e.g. kafka data or influxdb data)

## How to use 🖥️

After that, the app will start and you can visit the following urls (all are links in localhost):
- [Swagger UI](http://localhost:10000/streaming-iot/swagger-ui/index.html). Use the endpoints to send data to the app.
- [App UI](http://localhost:10000/streaming-iot/overview). Navigate to multiple pages to see the functionality. Use the side menu to navigate.
- [Grafana](http://localhost:3000) (username: admin, password: admin). Got to `Dashboards -> Manage` to see the available dashboards.
- [Prometheus](http://localhost:9090). Explore the available metrics.
- [Kafka Control Center](http://localhost:9021)
- [Spring Boot Admin](http://localhost:10000)
- [InfluxDB](http://localhost:8086). Use `admin` as username and `mySecurePassword` as password. Load the `./doc/influx_db_dashboard_export/ntua_streaming_iot_measurements.json` file to see real time ingested data in a dashboard.

## Screenshots 📸 

### App 📲
![image](https://github.com/SkourtsidisGiorgos/IIoT-Data-Processing-System/assets/60469956/6cce1c5d-c2f3-46cb-894f-f1e0d46636de)
![image](https://github.com/SkourtsidisGiorgos/IIoT-Data-Processing-System/assets/60469956/2099db4d-18af-4dfc-b6ca-400ba996c936)
![image](https://github.com/SkourtsidisGiorgos/IIoT-Data-Processing-System/assets/60469956/cc13ca7a-9482-471d-9a7d-03fd1bc6644e)
![image](https://github.com/SkourtsidisGiorgos/IIoT-Data-Processing-System/assets/60469956/f919ffbe-af76-41de-9f6c-d5621c5a2625)
![Screenshot from 2023-05-23 01-55-53](https://github.com/SkourtsidisGiorgos/IIoT-Data-Processing-System/assets/60469956/8a8da831-ddcc-4a7f-a137-ff609b780c30)


### Monitoring 🖥️📈
![image](https://github.com/SkourtsidisGiorgos/IIoT-Data-Processing-System/assets/60469956/54d22789-f9f3-4c74-9a09-52a63f6aad37)
![image](https://github.com/SkourtsidisGiorgos/IIoT-Data-Processing-System/assets/60469956/813c5b1d-a3f2-46e0-97d2-40e19a97ac05)
![image](https://github.com/SkourtsidisGiorgos/IIoT-Data-Processing-System/assets/60469956/eb3de3bd-c49a-4079-9aca-b0de317d45b0)
![image](https://github.com/SkourtsidisGiorgos/IIoT-Data-Processing-System/assets/60469956/44ddb06d-a777-4257-b5ad-20c195985351)
![image](https://github.com/SkourtsidisGiorgos/IIoT-Data-Processing-System/assets/60469956/577a0f08-b33e-487e-afd3-2aa0589d067f)
![image](https://github.com/SkourtsidisGiorgos/IIoT-Data-Processing-System/assets/60469956/d7cdf0ac-da7a-48d2-8d87-061fd210dac7)
![image](https://github.com/SkourtsidisGiorgos/IIoT-Data-Processing-System/assets/60469956/a221beeb-8939-4732-9a00-67523ddfc674)
![image](https://github.com/SkourtsidisGiorgos/IIoT-Data-Processing-System/assets/60469956/39521d6f-3b14-49a1-9097-c30ea01c3ba8)

### Alerting 🚨
![Screenshot from 2023-05-24 02-55-42](https://github.com/SkourtsidisGiorgos/IIoT-Data-Processing-System/assets/60469956/9f1c96d4-86cd-48c8-8e8e-ac198ac5b510)
![image](https://github.com/SkourtsidisGiorgos/IIoT-Data-Processing-System/assets/60469956/492dbec2-8437-4ef7-bc6d-0448946c1b33)
![image](https://github.com/SkourtsidisGiorgos/IIoT-Data-Processing-System/blob/main/doc/screenshots/kafka_noti.png?raw=true)

### Topic organization 📊

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
