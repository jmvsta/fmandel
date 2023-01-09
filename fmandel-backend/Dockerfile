#FROM gradle:jdk17 as builder
##WORKDIR /
#COPY src ./src
#COPY build.gradle.kts ./build.gradle.kts
#COPY settings.gradle.kts ./settings.gradle.kts
##COPY gradlew ./gradlewd
#RUN gradle clean build

FROM openjdk:17-oracle
COPY build/libs/fmandel-backend-0.0.1-SNAPSHOT.jar /fmandel-backend.jar
ENTRYPOINT ["java","-Dfile.encoding=UTF-8","-XshowSettings","-XX:+PrintFlagsFinal","-jar","/fmandel-backend.jar"]
EXPOSE 8080/tcp
