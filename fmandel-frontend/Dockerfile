FROM openjdk:17-oracle
COPY gradle gradle
COPY src src
COPY Dockerfile Dockerfile
COPY build.gradle.kts build.gradle.kts
COPY gradle.properties gradle.properties
COPY gradlew gradlew
COPY gradlew.bat gradlew.bat
COPY settings.gradle.kts settings.gradle.kts
ENTRYPOINT ["./gradlew","run"]
EXPOSE 3000/tcp
