FROM gradle:7.5.1-jdk11-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src
RUN gradle entry/discord-bot/build --no-daemon

FROM amazoncorretto:11
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/entry/discord-bot/build/libs/*.jar /app/lurkerbot-bot.jar
ENTRYPOINT ["java","-jar","/app/lurkerbot-bot.jar"]