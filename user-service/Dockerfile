
# ==========================================
# Stage 1: Build User Service using Maven
# ==========================================

FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy Maven configuration
COPY pom.xml .

# Copy application source code
COPY src ./src

# Build Spring Boot application
RUN mvn -B clean package -DskipTests

# ==========================================
# Stage 2: Run User Service using Java 21
# ==========================================

FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy the built JAR from Maven stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
