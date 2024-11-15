FROM openjdk:17-jdk
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

#FROM gradle:8.10.2-jdk17-alpine AS build
#WORKDIR /app
#COPY build.gradle settings.gradle ./
#RUN gradle dependencies --no-daemon
#COPY . /app
#RUN gradle clean build --no-daemon
#
#FROM openjdk:24-jdk-slim
#WORKDIR /app
#COPY --from=build /app/build/libs/*.jar /app/spliito.jar
#EXPOSE 8080
#ENTRYPOINT ["java"]
#CMD ["-jar", "spliito.jar"]