FROM openjdk:11

WORKDIR /app

COPY target/*.jar app.jar
COPY src/main/resources/data.sql /docker-entrypoint-initdb.d/

ENTRYPOINT [ "java", "-Dspring.profiles.active=container", "-jar", "app.jar" ]
