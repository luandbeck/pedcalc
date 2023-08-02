FROM openjdk:17-oracle

COPY build/libs/pedcalc*.jar /app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app.jar"]