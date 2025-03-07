FROM eclipse-temurin:21-jre
MAINTAINER npasic
COPY ./target/*.jar /tmp/application.jar
WORKDIR /tmp
ENTRYPOINT ["java","-jar", "application.jar"]
