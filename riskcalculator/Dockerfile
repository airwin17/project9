FROM openjdk:21
COPY target/RiskCalculator-spring-boot.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prod","app.jar"]