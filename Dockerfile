FROM openjdk:8-alpine
VOLUME /tmp
ADD target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
