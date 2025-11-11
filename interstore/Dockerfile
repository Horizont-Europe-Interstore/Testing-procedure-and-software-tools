FROM gradle:8.0.2-jdk17 AS builder
WORKDIR /home/gradle/project
COPY . .
RUN gradle clean app:bootJar -x test --no-daemon
FROM eclipse-temurin:17-jre
WORKDIR /app
RUN apt-get update \
  && apt-get install -y --no-install-recommends ca-certificates \
  && rm -rf /var/lib/apt/lists/* \
  && mkdir -p /app/certs
COPY --from=builder /home/gradle/project/app/build/libs/*.jar app.jar
COPY --from=builder /home/gradle/project/app/src/test/resources/ /app/src/test/resources/

RUN mkdir -p /app/build/reports/cucumber

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.main.class=interstore.App", "-Dserver.port=${SERVER_PORT:-8080}", "-Dserver.address=0.0.0.0", "-Dlogging.level.interstore=INFO", "-jar", "app.jar"]

















