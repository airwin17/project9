FROM openjdk:21

WORKDIR /app

COPY target/apigateway-spring-boot.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]
