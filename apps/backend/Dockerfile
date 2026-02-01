FROM maven:3.9-amazoncorretto-21-alpine AS build

WORKDIR /app

COPY pom.xml .
COPY ../.. .

RUN mvn clean package -DskipTests

FROM amazoncorretto:21.0.4-alpine3.18
WORKDIR /app

COPY --from=build /app/server/target/*.jar ./takitransfer.jar
RUN mkdir -p /var/takitransfer/plugins

EXPOSE 8080

CMD ["java", "-Dloader.path=/var/takitransfer/plugins/", "-jar", "takitransfer.jar"]