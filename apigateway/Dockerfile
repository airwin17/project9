FROM openjdk:21

WORKDIR /app

COPY target/apigateway-spring-boot.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app/app.jar"]
