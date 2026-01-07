FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM openjdk:21-jdk
ENV PROJECT_HOME /opt/app
WORKDIR $PROJECT_HOME
COPY --from=build /app/target/gateway-0.0.1-SNAPSHOT.jar $PROJECT_HOME/gateway.jar
EXPOSE 8080
CMD ["java","-jar","/opt/app/gateway.jar"]

