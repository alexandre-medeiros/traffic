FROM eclipse-temurin:17.0.8.1_1-jre-ubi9-minimal

EXPOSE 8080

ARG JAR_FILE

RUN mkdir /app

COPY target/${JAR_FILE} /app/traffic-api.jar

COPY wait-for-it.sh /wait-for-it.sh

RUN chmod +x /wait-for-it.sh

CMD ["java", "-jar", "/app/traffic-api.jar"]