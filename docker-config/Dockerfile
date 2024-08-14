FROM openjdk:21
VOLUME /tmp
EXPOSE 8081
ARG JAR_FILE=../build/libs/artist-service-1.0.jar
ADD ${JAR_FILE} artist-service-1.0.jar
ENTRYPOINT ["java","-jar","/artist-service-1.0.jar"]
