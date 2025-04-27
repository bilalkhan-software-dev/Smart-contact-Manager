# 🛠 Build Stage: Java + Tailwind CSS
FROM --platform=linux/amd64 eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

# Install Node.js & Tailwind CSS
RUN apt-get update && \
    apt-get install -y curl gnupg && \
    curl -fsSL https://deb.nodesource.com/setup_18.x | bash - && \
    apt-get install -y nodejs && \
    npm install -g tailwindcss

# Copy only Maven files first (for caching dependencies)
COPY .mvn/ .mvn/
COPY mvnw .
COPY pom.xml .

# Pre-fetch Maven dependencies
RUN ./mvnw dependency:go-offline

# Now copy the full project
COPY src/ src/
COPY tailwind.config.js .
COPY package.json .
COPY other-resources-if-any/ ./  # optional if you have static folders etc.

# Build Tailwind CSS
RUN tailwindcss -i src/main/resources/static/css/input.css \
                -o src/main/resources/static/css/output.css \
                --minify

# Build Spring Boot app
RUN ./mvnw clean package -DskipTests

# 🛠 Runtime Stage
FROM --platform=linux/amd64 eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy jar from builder
COPY --from=builder /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
