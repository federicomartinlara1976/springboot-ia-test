# Usa una imagen base con Java (versión compatible con tu Spring Boot)
FROM docker.io/eclipse-temurin:21

# Directorio de trabajo en el contenedor
WORKDIR /app

# Copia el JAR construido (ajusta el nombre si usas Maven/Gradle)
COPY target/springboot-ia-test.war app.war

# Puerto expuesto (el mismo que usa tu Spring Boot)
EXPOSE 8094

# --------------------------------------------------------------
# OPTIMIZACIONES JVM PARA CONTENEDORES
# --------------------------------------------------------------
# 1. MEMORIA: Usa el 70% de la RAM del contenedor para el Heap
#    (Ajusta este valor según tu app: 50.0, 60.0, 70.0...)
# 2. GC: G1GC es el mejor equilibrio Latencia/Rendimiento
# 3. METASPACE: Evita fugas de memoria, pon un límite realista
# 4. REGISTRO: Activa logs de GC en STDOUT (útil para debugging)
# --------------------------------------------------------------
ENTRYPOINT ["java", \
            "-XX:+UseG1GC", \
            "-XX:MaxRAMPercentage=70.0", \
            "-XX:InitialRAMPercentage=70.0", \
            "-XX:MaxMetaspaceSize=256m", \
            "-XX:+PrintCommandLineFlags", \
            "-Xlog:gc*:stdout:time", \
            "-jar", "app.war"]