FROM gradle:8.0.2-jdk17 AS builder
WORKDIR /home/gradle/project
COPY . .
RUN gradle clean app:bootJar -x test --no-daemon
FROM openjdk:17-slim
WORKDIR /app
RUN apt-get update \
  && apt-get install -y --no-install-recommends ca-certificates \
  && rm -rf /var/lib/apt/lists/*
COPY --from=builder /home/gradle/project/app/build/libs/*.jar app.jar
COPY --from=builder /home/gradle/project/app/src/test/resources/ /app/src/test/resources/
RUN mkdir -p /app/build/reports/cucumber
EXPOSE 1900
EXPOSE 8443
ENTRYPOINT ["java", "-Dspring.main.class=interstore.App", "-Dserver.port=1900", "-Dserver.address=0.0.0.0", "-Dlogging.level.interstore=INFO", "-jar", "app.jar"]


















