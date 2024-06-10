# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-slim

# Set the working directory in the container
WORKDIR /app
RUN apt-get update && apt-get install -y netcat wget unzip entr
COPY gradlew /app/
COPY gradle /app/gradle
COPY app/build.gradle /app/
RUN chmod +x ./gradlew
COPY . /app
#RUN ./gradlew build --no-daemon || return 0
COPY . /app
#COPY . .

#RUN ./gradlew build --no-daemon

# Expose the port the app runs on
EXPOSE 8080
CMD ["sh", "-c", "find src -type f | entr -r ./gradlew bootRun"]
















#CMD ["sh", "-c", "find src | entr -r ./gradlew run"]
#CMD ["./gradlew", "build"]
#CMD ["./gradlew", "bootRun"]
#CMD ["./gradlew", "run"]
# Run the application
#CMD ["/app/wait-for-nats.sh", "nats", "4222", "java", "-jar", "app/build/libs/app.jar"]
#CMD ["java", "-jar", "app/build/libs/app.jar"]

# docker build -f devjavadockerfile.Dockerfile -t interstorejava.
# docker run --name javabackend -p 8080:8080 interstorejava 
