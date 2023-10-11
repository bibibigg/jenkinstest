FROM openjdk:17

VOLUME /tmp


COPY app/build/libs/app-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]