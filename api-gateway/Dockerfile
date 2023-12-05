FROM eclipse-temurin:17-jdk-alpine
ENV APP_NAME api-gateway
COPY ./target/${APP_NAME}.jar  /app/${APP_NAME}.jar
WORKDIR /app
EXPOSE 8765
CMD java -jar ${APP_NAME}.jar