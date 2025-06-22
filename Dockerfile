# --- Fase 1: Construcción con Maven ---
# Usamos una imagen de Maven con Java 8 para compilar el proyecto
FROM maven:3.8.5-openjdk-8 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos primero el pom.xml para aprovechar la caché de capas de Docker
COPY pom.xml .

# Copiamos todo el código fuente del proyecto
COPY src ./src

# Ejecutamos el comando de Maven para compilar y empaquetar el proyecto como .war
# El -DskipTests omite la ejecución de pruebas para acelerar la construcción
RUN mvn clean package -DskipTests


# --- Fase 2: Ejecución con Tomcat ---
# Usamos una imagen oficial de Tomcat 8.5 que ya tiene Java 8
FROM tomcat:8.5-jdk8-openjdk

# Eliminamos la aplicación por defecto de Tomcat para limpiar
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiamos el archivo .war que generamos en la fase anterior
# desde la fase 'build' a la carpeta 'webapps' de Tomcat.
# Tomcat desplegará automáticamente cualquier .war en esta carpeta.
# Lo renombramos a ROOT.war para que la aplicación se ejecute en la raíz del dominio.
COPY --from=build /app/target/RedSocialAcademica.war /usr/local/tomcat/webapps/ROOT.war

# Exponemos el puerto 8080, que es el puerto por defecto de Tomcat
EXPOSE 8080

# El comando para iniciar el servidor Tomcat (esto es parte de la imagen base de Tomcat)
CMD ["catalina.sh", "run"]
