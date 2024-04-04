FROM openjdk:17-jdk-slim as builder
WORKDIR /app
COPY gradlew build.gradle settings.gradle  ./
COPY gradle ./gradle
COPY src/main ./src/main
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT java -jar app.jar