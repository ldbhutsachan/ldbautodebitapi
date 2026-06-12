
FROM eclipse-temurin:17.0.6_10-jre-alpine

# Labels
LABEL author="Ibank infomation service"

# Set the timezone to GMT+7
ENV TZ=Asia/Bangkok

# Copy the application JAR file to the container
RUN mkdir /app
COPY ./target/*.jar /app/run.jar

# Expose port 4400 for the container
EXPOSE 2100

# Set the command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "-Duser.timezone=GMT+7","/app/run.jar"]