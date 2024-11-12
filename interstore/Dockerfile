FROM openjdk:17-slim
WORKDIR /app
RUN apt-get update && apt-get install 
COPY . .
EXPOSE 8080

















#CMD ["sh", "-c", "find src | entr -r ./gradlew run"]
#CMD ["./gradlew", "build"]
#CMD ["./gradlew", "bootRun"]
#CMD ["./gradlew", "run"]
# Run the application
#CMD ["/app/wait-for-nats.sh", "nats", "4222", "java", "-jar", "app/build/libs/app.jar"]
#CMD ["java", "-jar", "app/build/libs/app.jar"]

# docker build -f devjavadockerfile.Dockerfile -t interstorejava.
# docker run --name javabackend -p 8080:8080 interstorejava 
