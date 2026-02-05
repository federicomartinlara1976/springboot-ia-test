# Usa una imagen base con Java (versión compatible con tu Spring Boot)
FROM docker.io/eclipse-temurin:21

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copia el JAR construido (ajusta el nombre si usas Maven/Gradle)
COPY target/springboot-ia-test.war app.war

# Puerto expuesto (el mismo que usa tu Spring Boot)
EXPOSE 8094

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.war"]