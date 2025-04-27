# 🛠 Build Stage
FROM --platform=linux/amd64 eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app

# Install Node.js & Tailwind CSS (Node 20 LTS)
RUN apt-get update && \
    apt-get install -y curl gnupg && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    npm install -g tailwindcss

# Copy build files
COPY .mvn/ .mvn/
COPY mvnw .
COPY pom.xml .

# Get dependencies
RUN ./mvnw dependency:go-offline

# Copy application source
COPY src/ src/
COPY tailwind.config.js .
COPY package.json .
COPY other-resources-if-any/ ./

# Build Tailwind CSS
RUN tailwindcss -i src/main/resources/static/css/input.css \
                -o src/main/resources/static/css/output.css \
                --minify

# Package application
RUN ./mvnw clean package -DskipTests

# 🛠 Runtime Stage
FROM --platform=linux/amd64 eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy built application
COPY --from=builder /app/target/*.jar app.jar

# Port configuration for Railway (using SERVER_PORT)
ENV SERVER_PORT=${PORT:-8080}
EXPOSE ${SERVER_PORT:-8080}

# Simple entrypoint
ENTRYPOINT ["java", "-jar", "app.jar"]