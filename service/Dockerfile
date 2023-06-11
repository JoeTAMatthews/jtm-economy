#FROM openjdk:11-buster
#VOLUME /tmp
#EXPOSE 8095
#ARG JAR_FILE
#COPY ${JAR_FILE} app.jar
#CMD java -Xms256m -Xmx512m -jar app.jar

FROM gradle:8.0-jdk-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src/producer
WORKDIR /home/gradle/src/producer
RUN gradle bootJar --no-daemon --stacktrace

FROM openjdk:16.0-buster
VOLUME /tmp

ARG JAR_FILE=build/libs/*.jar
EXPOSE 8095
COPY --from=build /home/gradle/src/producer/build/libs/*.jar app.jar
CMD java -Xms256m -Xmx512m -jar /app.jar