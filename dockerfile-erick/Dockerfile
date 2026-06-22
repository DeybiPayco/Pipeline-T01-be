# ====== FASE DE CONSTRUCCIÓN ======
# AHORA USA TU IMAGEN DE MAVEN
FROM erickramoss/maven:3.9-eclipse-temurin-25-alpine AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ====== FASE DE EJECUCIÓN ======
# AHORA USA TU IMAGEN DE JDK
FROM erickramoss/eclipse-temurin:25-jre-alpine

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]