
# ==========================================
# Stage 1: Build Trade Execution Service
# ==========================================

FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copy Maven configuration
COPY pom.xml .

# Copy application source
COPY src ./src

# Build Spring Boot application
RUN mvn -B clean package -DskipTests

# ==========================================
# Stage 2: Run Trade Execution Service
# ==========================================

FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy executable JAR from build stage
COPY --from=build /app/target/trade-execution-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
