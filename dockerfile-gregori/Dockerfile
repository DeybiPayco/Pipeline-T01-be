# ====== FASE DE CONSTRUCCIÓN ======
# AHORA USA TU IMAGEN DE MAVEN
FROM gregoriormenosanchez/maven:3.9-amazoncorretto-25-alpine AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ====== FASE DE EJECUCIÓN ======
# AHORA USA TU IMAGEN DE JDK
FROM maven:3.9-amazoncorretto-25-alpine

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# docker run -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=Admin12345" -p 1433:1433 --name sqlserver -d gregoriormenosanchez/sql-server:2022


# docker build -t gregoriormenosanchez/springboot-sqlserver:1.0 .

# docker run -d --name springboot-sqlserver -p 8085:8085 gregoriormenosanchez/springboot-sqlserver:1.0

# docker push gregoriormenosanchez/springboot-sqlserver:1.0