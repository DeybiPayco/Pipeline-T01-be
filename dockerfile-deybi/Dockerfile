# ====== FASE DE CONSTRUCCIÓN ======
FROM maven:3.9-eclipse-temurin-25-alpine AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ====== FASE DE EJECUCIÓN ======
FROM eclipse-temurin:25-jdk-alpine

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]