#FROM gradle:7.5.1-jdk11-alpine AS buildBot
#COPY --chown=gradle:gradle . /home/gradle/src
#
#WORKDIR /home/gradle/src
#RUN gradle :entry:discord-bot:fatJar --no-daemon

#FROM amazoncorretto:11 as runBot
#RUN mkdir /app
#COPY --from=buildBot /home/gradle/src/entry/discord-bot/build/libs/lurkerbot-bot-fat.jar /app
#ENTRYPOINT ["java", "-jar","/app/lurkerbot-bot-fat.jar"]

FROM gradle:8.2.1-jdk11-alpine AS buildWeb
COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src
RUN gradle :entry:web:fatJar --no-daemon

FROM amazoncorretto:11 as runWeb
RUN mkdir /app
COPY --from=buildWeb /home/gradle/src/entry/web/build/libs/lurkerbot-web-fat.jar /app/lurkerbot-web.jar
ENTRYPOINT ["java","-jar","/app/lurkerbot-web.jar"]
