# Use a imagem do OpenJDK 17 como base
FROM openjdk:17-oracle

# Copie o arquivo JAR da sua aplicação para a imagem
COPY build/libs/pedcalc*.jar /app.jar

# Expõe a porta em que sua aplicação está ouvindo (caso necessário)
EXPOSE 8080

# Comando para iniciar sua aplicação quando o contêiner for executado
CMD ["java", "-jar", "/app.jar"]