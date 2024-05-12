FROM openjdk:21
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=build/libs/artist-service-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} artist-service-1.0.jar
ENTRYPOINT ["java","-jar","/artist-service-1.0.jar"]
