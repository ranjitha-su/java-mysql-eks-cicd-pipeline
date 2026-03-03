# ---------- Stage 1: Build ----------
FROM gradle:8.5-jdk17-alpine AS builder

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle/
RUN ./gradlew dependencies

COPY src ./src
RUN ./gradlew clean build -x test

# ---------- Stage 2: Runtime ----------
FROM amazoncorretto:17-alpine
WORKDIR /usr/app

# Copy only the jar from builder stage
COPY --from=builder /app/build/libs/java-mysql-app-1.0.jar /usr/app


EXPOSE 8080

CMD java -jar java-mysql-app-1.0.jar
