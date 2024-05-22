# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-slim

# Set the working directory in the container
WORKDIR /app
RUN apt-get update && apt-get install -y netcat
# Copy the local code and gradle wrapper to the container
COPY . .
#COPY ../wait-for-nats.sh /app/wait-for-nats.sh
#RUN chmod +x /app/wait-for-nats.sh 
# Give execution permission to Gradle wrapper scripts
RUN chmod +x ./gradlew

# Use the Gradle wrapper to build the application
RUN ./gradlew build --no-daemon

# Expose the port the app runs on
EXPOSE 8080

# Run the application
#CMD ["/app/wait-for-nats.sh", "nats", "4222", "java", "-jar", "app/build/libs/app.jar"]
#CMD ["java", "-jar", "app/build/libs/app.jar"]

# docker build -f devjavadockerfile.Dockerfile -t interstorejava.
# docker run --name javabackend -p 8080:8080 interstorejava 
