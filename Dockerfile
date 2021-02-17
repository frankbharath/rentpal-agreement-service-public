FROM openjdk:11
EXPOSE 8082
ADD target/agreement-service.jar agreement-service.jar
ENTRYPOINT ["java", "-jar", "/agreement-service.jar"]