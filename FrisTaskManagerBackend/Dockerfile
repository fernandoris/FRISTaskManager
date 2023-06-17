FROM openjdk:11-jre

COPY ./target/FrisTaskManagerBackend-0.0.1-SNAPSHOT.jar ~/app.jar
COPY ./properties.yml ~/properties.yml

CMD ["java", "-jar", "~/app.jar", "server", "~/properties.yml"]

EXPOSE 8080
EXPOSE 8081