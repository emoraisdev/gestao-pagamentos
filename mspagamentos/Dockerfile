FROM maven:3.8.4-openjdk-17-slim AS build

WORKDIR /app

COPY . /app

RUN mvn package -DskipTests=true

FROM openjdk:17-jdk-slim

COPY --from=build /app/target/*.jar /app/app.jar

ENV DB_URL url_db
ENV DB_NAME nome_db
ENV DB_USERNAME seu_usuario
ENV DB_PASSWORD sua_senha

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./unrandom", "-jar", "/app/app.jar"]