FROM openjdk:17-oracle
ARG JAR_FILE=build/libs/fmandel-backend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /fmandel-backend.jar
#ENTRYPOINT ["java","-Dfile.encoding=UTF-8","-XshowSettings","-XX:+PrintFlagsFinal","-jar","/fmandel-backend.jar"]
ENTRYPOINT ["java","-Dfile.encoding=UTF-8","-XshowSettings","-XX:+PrintFlagsFinal","-jar","/fmandel-backend.jar"]
# syntax = docker/dockerfile:experimental
#FROM gradle:jdk17 as builder
#WORKDIR /app
#COPY src ./src
#COPY build.gradle.kts ./build.gradle.kts
#RUN --mount=type=cache,target=./.gradle gradle clean test install
#
#FROM openjdk:17-oracle as backend
#WORKDIR /root
#COPY --from=builder /app/build/install/app ./
