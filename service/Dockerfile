FROM java:8
VOLUME /tmp
EXPOSE 8095
ADD /build/libs/*.jar app.jar
CMD java -Xms256m -Xmx512m -jar app.jar