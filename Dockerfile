# Build stage (Java + Tailwind)
FROM --palatform=linux/amd64 eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app

# Install Node.js and Tailwind
RUN apt-get update && \
    apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_18.x | bash - && \
    apt-get install -y nodejs

# Copy files
COPY . .

# Build Tailwind CSS
RUN npm install -g tailwindcss && \
    tailwindcss -i ./src/main/resources/static/css/input.css \
               -o ./src/main/resources/static/css/output.css \
               --minify





# Build Spring Boot
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM --platform=linux/amd64 eclipse-temurin:21-jre-jammy
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
#FROM openjdk:21
#
#WORKDIR /app
#
#COPY dist/scm2.0-0.0.1-SNAPSHOT.jar /app/scm2.0-0.0.1-SNAPSHOT.jar
#
#EXPOSE 8080
#
#ENTRYPOINT ["java", "-jar", "scm2.0-0.0.1-SNAPSHOT.jar"]
