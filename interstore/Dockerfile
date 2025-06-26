# ┌────────────────────────────────────────────────────────────┐
# │ 1) Builder: compile your app with Gradle                  │
# └────────────────────────────────────────────────────────────┘
FROM gradle:8.0.2-jdk17 AS builder
WORKDIR /home/gradle/project

# Copy everything (including gradlew, build.gradle, settings.gradle, src/, etc)
COPY . .

# Build the fat‐JAR for the `app` subproject (skipping tests for speed)
RUN gradle clean app:bootJar -x test --no-daemon

# ┌────────────────────────────────────────────────────────────────┐
# │ 2) Runtime: small, slim JRE image with just your fat JAR      │
# └────────────────────────────────────────────────────────────────┘
FROM openjdk:17-slim
WORKDIR /app

# (Optional) install any OS‐level deps you need, e.g. CA certs:
RUN apt-get update \
  && apt-get install -y --no-install-recommends ca-certificates \
  && rm -rf /var/lib/apt/lists/*

# Copy the one fat‐JAR from the builder stage (from the `app` subproject)
COPY --from=builder /home/gradle/project/app/build/libs/*.jar app.jar

# Expose your HTTPS port
EXPOSE 8443

# Run it with your Spring profile and server.port
ENTRYPOINT ["java","-jar","app.jar","--spring.profiles.active=dev","--server.port=8443"]







#FROM openjdk:17-slim
#WORKDIR /app
#RUN apt-get update \
 # && apt-get install -y --no-install-recommends unzip \
  #&& rm -rf /var/lib/apt/lists/*
#COPY . .
#RUN chmod +x gradlew \
#  && ./gradlew clean build -x test --no-daemon
#EXPOSE 8443
#ENTRYPOINT ["sh", "-c", "java -jar build/libs/*.jar"]
















#CMD ["sh", "-c", "find src | entr -r ./gradlew run"]
#CMD ["./gradlew", "build"]
#CMD ["./gradlew", "bootRun"]
#CMD ["./gradlew", "run"]
# Run the application
#CMD ["/app/wait-for-nats.sh", "nats", "4222", "java", "-jar", "app/build/libs/app.jar"]
#CMD ["java", "-jar", "app/build/libs/app.jar"]

# docker build -f devjavadockerfile.Dockerfile -t interstorejava.
# docker run --name javabackend -p 8080:8080 interstorejava 
