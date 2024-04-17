FROM gradle:8.7.0-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildDependents --no-daemon

FROM eclipse-temurin:17-jre-alpine
EXPOSE 8080
COPY --from=build /home/gradle/src/build/libs/TeamvoidedWebsite-all.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]