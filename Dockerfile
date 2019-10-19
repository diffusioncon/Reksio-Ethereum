FROM openjdk:11-jre-slim
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} secretary.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/secretary.jar"]
