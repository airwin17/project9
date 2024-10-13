FROM openjdk:21
COPY target/Patientmanager-spring-boot.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","/app.jar"]