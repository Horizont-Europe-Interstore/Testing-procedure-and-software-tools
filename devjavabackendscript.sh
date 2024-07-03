#!/bin/bash

SERVICE_NAME=$1

if [ -z "$SERVICE_NAME" ]; then
  echo "Please provide the service name as an argument."
  exit 1
fi

echo "Stopping $SERVICE_NAME container..."
docker compose stop $SERVICE_NAME

echo "Removing $SERVICE_NAME container..."
docker compose rm -f $SERVICE_NAME

echo "Rebuilding $SERVICE_NAME container..."
docker compose build $SERVICE_NAME

echo "Starting $SERVICE_NAME container in interactive mode..."
docker compose up -d $SERVICE_NAME

echo "Running Spring Boot server inside $SERVICE_NAME container..."
docker compose exec $SERVICE_NAME ./gradlew run

echo "$SERVICE_NAME container and Spring Boot server started successfully!"


