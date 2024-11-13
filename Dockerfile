FROM eclipse-temurin:21

ENV SPRING_PROFILE="default"
ENV SERVER_PORT=8080

RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

RUN mkdir /opt/app
COPY target/front.jar /opt/app
CMD ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-Dserver.port=${SERVER_PORT}", "-Xms256m", "-Xmx256m", "-jar", "-Duser.timezone=Asia/Seoul", "/opt/app/front.jar"]
