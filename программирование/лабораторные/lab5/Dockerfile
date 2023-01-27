FROM gradle:7.5.1-jdk17-alpine AS build

ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

COPY settings.gradle $APP_HOME
COPY app/build.gradle $APP_HOME/app/build.gradle
COPY gradle $APP_HOME/gradle
COPY --chown=gradle:gradle . .

RUN gradle fatJar

FROM eclipse-temurin:17-jre-alpine

ENV APP_HOME=/usr/app
WORKDIR $APP_HOME

COPY --from=build $APP_HOME/app/build/libs/app-fat.jar .
CMD ["java", "-jar", "app-fat.jar"]
