FROM maven:3.8.6-amazoncorretto-19 AS build

COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package -Dmaven.test.skip

FROM openjdk:21-ea-5
COPY --from=build /usr/src/app/target/testingProject-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]