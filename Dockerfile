# Etapa 1: Compilar la aplicación con Maven
FROM maven:3.8.2-amazoncorretto-17 AS build

WORKDIR /app

# Copiar los archivos de Maven y descargar las dependencias para evitar descargas repetidas en cada build
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el resto del código fuente y compilar
COPY . .
RUN chmod +x mvnw
RUN ./mvnw clean install -DskipTests -X

# Etapa 2: Crear la imagen final basada en una imagen ligera de OpenJDK
FROM openjdk:17-jdk-alpine

WORKDIR /app

# Copiar el archivo .jar generado desde la etapa de build
COPY --from=build /app/target/*.jar /app/app.jar

# Exponer el puerto 8080
EXPOSE 8080

# Health check opcional para verificar si la aplicación está funcionando
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s CMD curl -f http://localhost:8080/ || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
