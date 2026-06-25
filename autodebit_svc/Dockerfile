FROM eclipse-temurin:17.0.6_10-jre-alpine

LABEL author="Ibank information service"

ENV TZ=Asia/Bangkok

RUN mkdir /app
COPY ./target/*.jar /app/run.jar

EXPOSE 2100

ENTRYPOINT ["java", "-Duser.timezone=GMT+7", "-jar", "/app/run.jar"]
