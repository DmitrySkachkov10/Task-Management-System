FROM eclipse-temurin
WORKDIR /app
VOLUME /app
COPY target/task-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
