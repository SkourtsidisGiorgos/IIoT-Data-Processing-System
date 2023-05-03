# ntua-streaming-iot

NTUA Thesis by Skourtsidis Giorgos.  
Supervisor: Paraskevas Kerasiotis

## Pre-requisites

- Java 17
- Apache Maven 3.8.0 or higher
- Docker 20.0.0 or higher
- Docker-compose 2.0.0 or higher

## How to run

Go to project's root directory.  

Configure prometheus to read metrics from our app (optional):  

- Get Your IP:  
  - Linux: `ip route get 8.8.8.8 | grep -Po '(?<=src )(\d{1,3}\.){3}\d{1,3}'`
  - Windows: `ipconfig | findstr /i "IPv4 Address"`
- Open file `./monitoring/prometheus/prometheus_dev.yml`. Replace `10.0.2.15` in line 22 with your IP.

Set up external systems:
- `cd custom_images; docker build --file connect_Dockerfile -t my_kafka_connect .`
- Go to project's parent folder. Then: `docker-compose build`
- `docker-compose up -d`
or 
- `COMPOSE_DOCKER_CLI_BUILD=1 DOCKER_BUILDKIT=1 docker compose up -d`

Run the app:

- `mvn clean install  -Dmaven.test.skip=true`
- `java -jar target/ntua-streaming-iot-0.0.1-SNAPSHOT.jar` or `./start-app.sh dev`

## Features

- Multiple producers/consumers using Spring's Beans
- Use spring profiles to change environment. (dev,test,prod)
- Export metrics to JMX, Prometheus, Graphite
- Start-up script
- Log compression and retention
- Status endpoints for app: `/app/info/status`, `/app/info/healch`, `/app/info/buildinfo` etc
- Write gc logs

## Swagger-ui
- visit: `http://localhost:10000/swagger-ui/` (when using springfox-swagger2)
- `http://localhost:10000/streaming-iot/swagger-ui/index.html` (when using springdoc-openapi-ui)

## Topic organization

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
