FROM openjdk:11

ARG JAR_FILE=./build/libs/*-SNAPSHOT.jar

COPY ${JAR_FILE} suite-user.jar


ENTRYPOINT ["java","-jar","/suite-user.jar"]
