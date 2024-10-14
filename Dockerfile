# Use an official Maven image to build the application
# Use an official Maven image to build the application
FROM maven:3.8.2-amazoncorretto-17 AS build

WORKDIR /app
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
COPY src ./src
RUN chmod +x mvnw
RUN ./mvnw clean install -DskipTests

# Use an official OpenJDK image to run the application
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]