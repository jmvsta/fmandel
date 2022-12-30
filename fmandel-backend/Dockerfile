#FROM openjdk:17-oracle
#ARG JAR_FILE=build/libs/fmandel-backend-0.0.1-SNAPSHOT.jar
#COPY ${JAR_FILE} /fmandel-backend.jar
##ENTRYPOINT ["java","-Dfile.encoding=UTF-8","-XshowSettings","-XX:+PrintFlagsFinal","-jar","/fmandel-backend.jar"]
#ENTRYPOINT ["java","-Dfile.encoding=UTF-8","-XshowSettings","-XX:+PrintFlagsFinal","-jar","/fmandel-backend.jar"]
## syntax = docker/dockerfile:experimental
FROM gradle:jdk17 as builder
#WORKDIR /
COPY src ./src
COPY build.gradle.kts ./build.gradle.kts
COPY settings.gradle.kts ./settings.gradle.kts
#COPY gradlew ./gradlew
RUN gradle clean build

FROM openjdk:17-oracle
COPY --from=builder / ./
COPY build/libs/fmandel-backend-0.0.1-SNAPSHOT.jar /fmandel-backend.jar
ENTRYPOINT ["java","-Dfile.encoding=UTF-8","-XshowSettings","-XX:+PrintFlagsFinal","-jar","/fmandel-backend.jar"]
