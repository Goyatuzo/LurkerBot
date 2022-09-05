FROM gradle:7.5.1-jdk11-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src
RUN gradle :entry:discord-bot:fatJar --no-daemon

FROM amazoncorretto:11
RUN mkdir /app
COPY --from=build /home/gradle/src/entry/discord-bot/build/libs/*.jar /app/lurkerbot-bot.jar
ENTRYPOINT ["java","-jar","/app/lurkerbot-bot.jar"]