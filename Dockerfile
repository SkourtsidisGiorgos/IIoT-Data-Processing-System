FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY config/ config/
ENTRYPOINT ["java","-Dspring.profiles.active=dev,docker", "-jar","/app.jar"]