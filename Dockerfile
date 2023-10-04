FROM openjdk:11

ARG JAR_FILE=./build/libs/*-SNAPSHOT.jar

COPY ${JAR_FILE} suite-user-service-app.jar


ENTRYPOINT ["java","-jar","suite-user-service-app.jar"]
