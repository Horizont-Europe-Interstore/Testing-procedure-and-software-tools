@echo off
setlocal

set SERVICE_NAME=%1
set COMPOSE_FILE=docker-compose-dev.yml

if "%SERVICE_NAME%"=="" (
    echo Please provide the service name as an argument.
    exit /b 1
)

echo Stopping %SERVICE_NAME% container...
docker compose -f %COMPOSE_FILE% stop %SERVICE_NAME%

echo Removing %SERVICE_NAME% container...
docker compose -f %COMPOSE_FILE% rm -f %SERVICE_NAME%

echo Rebuilding and starting %SERVICE_NAME% container...
docker compose -f %COMPOSE_FILE% up -d --build %SERVICE_NAME%

echo Running Spring Boot server inside %SERVICE_NAME% container in interactive mode...
docker compose -f %COMPOSE_FILE% exec -it %SERVICE_NAME% ./gradlew run

echo %SERVICE_NAME% container and Spring Boot server started successfully!
endlocal