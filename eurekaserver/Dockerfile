FROM openjdk:21
WORKDIR /app
COPY target/eurekaServer-1.0-spring-boot.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java","-jar","/app/app.jar"]