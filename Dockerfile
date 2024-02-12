FROM openjdk:17-jdk-alpine

EXPOSE 8080

ADD build/libs/MoneyTransfer-0.0.1.jar myapp.jar

ENTRYPOINT ["java", "-jar", "myapp.jar"]