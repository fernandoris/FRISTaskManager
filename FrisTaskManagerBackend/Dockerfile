FROM openjdk:11-jre

COPY ./target/FrisTaskManagerBackend-0.0.1-SNAPSHOT.jar ~/app.jar
COPY ./properties.yml ~/properties.yml
COPY ./wait-for-it.sh ./wait-for-it.sh

RUN chmod +x ./wait-for-it.sh

EXPOSE 8080
EXPOSE 8081

CMD ["./wait-for-it.sh", "db:3306", "--", "java", "-jar", "~/app.jar", "server", "~/properties.yml"]