FROM eclipse-temurin:21

ENV SPRING_PROFILE="default"
ENV SERVER_PORT=8080

RUN mkdir /opt/app
COPY target/front.jar /opt/app
CMD ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-Dserver.port=${SERVER_PORT}","-jar", "/opt/app/front.jar"]
