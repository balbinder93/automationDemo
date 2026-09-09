FROM maven:3.9.11-eclipse-temurin-21

WORKDIR /workspace
COPY pom.xml ./
RUN mvn --batch-mode --fail-never dependency:go-offline
COPY src ./src
RUN mvn --batch-mode clean test
