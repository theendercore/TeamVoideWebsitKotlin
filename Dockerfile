FROM eclipse-temurin:17-jdk-alpine as build

WORKDIR /src
COPY . /src

RUN gradlew buildDependents --no-daemon

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /home/gradle/src/build/libs/TeamvoidedWebsite-all.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]