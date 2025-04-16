# Use a Maven image with Java 23 to build the Spring Boot application
FROM eclipse-temurin:23-jdk AS build

# Set the working directory
WORKDIR /app

# Copy the Maven Wrapper and configuration files
COPY mvnw ./
COPY .mvn/ .mvn/

# Ensure the Maven Wrapper script is executable
RUN chmod +x mvnw

# Copy the pom.xml and download dependencies
COPY pom.xml ./
RUN ./mvnw dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Use a Java 23 Runtime image to run the application
FROM eclipse-temurin:23-jre

# Set the working directory
WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port 8080
EXPOSE 8080

# Specify the command to run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
