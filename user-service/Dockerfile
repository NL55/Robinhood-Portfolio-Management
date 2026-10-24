
# Use Java 21 runtime
FROM eclipse-temurin:21-jre

# Set working directory
WORKDIR /app

# Copy compiled Spring Boot JAR
COPY target/*.jar app.jar

# User Service port
EXPOSE 8081

# Start Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
