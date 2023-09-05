FROM debian:buster-slim

RUN apt-get update
RUN apt-get install -y openjdk-11-jre curl

ADD target/server-1.0.0.jar /srv/server-1.0.0.jar

WORKDIR /srv
EXPOSE 5000
CMD ["java", "-jar", "server-1.0.0.jar"]

