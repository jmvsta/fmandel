FROM openjdk:17-oracle
COPY build/libs/fmandel-backend-0.0.1-SNAPSHOT.jar /fmandel-backend.jar
ENTRYPOINT ["java","-Dfile.encoding=UTF-8","-XshowSettings","-XX:+PrintFlagsFinal","-jar","/fmandel-backend.jar"]
EXPOSE 8080/tcp
