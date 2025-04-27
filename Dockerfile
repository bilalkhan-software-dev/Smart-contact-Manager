# 🛠 Build stage (Java + Tailwind)
FROM --platform=linux/amd64 eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

# Install Node.js and Tailwind
RUN apt-get update && \
    apt-get install -y curl gnupg && \
    curl -fsSL https://deb.nodesource.com/setup_18.x | bash - && \
    apt-get install -y nodejs && \
    npm install -g tailwindcss

# --- Maven Dependency Cache Optimization ---

# Copy Maven wrapper and pom.xml first
COPY .mvn/ .mvn/
COPY mvnw .
COPY pom.xml .

# Pre-download Maven dependencies (cache dependencies layer)
RUN ./mvnw dependency:go-offline

# --- Now copy the rest of the code ---
COPY . .

# Build Tailwind CSS
RUN tailwindcss -i src/main/resources/static/css/input.css \
                -o src/main/resources/static/css/output.css \
                --minify

# Build Spring Boot App
RUN ./mvnw clean package -DskipTests

# 🛠 Runtime stage
FROM --platform=linux/amd64 eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Start app
ENTRYPOINT ["java", "-jar", "app.jar"]
