# Use a small JDK image
FROM eclipse-temurin:21-jdk-jammy as builder

# Set a working directory inside the container
WORKDIR /app

# Copy only the built JAR file
COPY target/shosetsuya-0.0.1-SNAPSHOT.jar app.jar

# Run the JAR when container starts
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
