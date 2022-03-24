FROM openjdk:11-buster
VOLUME /tmp
EXPOSE 8095
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
CMD java -Xms256m -Xmx512m -jar app.jar