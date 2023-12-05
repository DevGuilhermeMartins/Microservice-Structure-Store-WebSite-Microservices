FROM eclipse-temurin:17-jdk-alpine
ENV APP_NAME catalog-service
COPY ./target/${APP_NAME}.jar  /app/${APP_NAME}.jar
WORKDIR /app
EXPOSE 9000
CMD java -jar ${APP_NAME}.jar