FROM adoptopenjdk/openjdk11:alpine-jre

ENV SPRING_PROFILES_ACTIVE container

WORKDIR /opt/action-monitor

COPY action-monitor-service.jar service.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","service.jar"]
