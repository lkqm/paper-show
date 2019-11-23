FROM openjdk:8-alpine
ADD target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar", "--paper-show.work-dir=/data"]
