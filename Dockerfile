FROM openjdk:17

WORKDIR /app

COPY target/moviesdb-1.0.jar .

CMD ["java", "-jar", "moviesdb-1.0.jar"]
