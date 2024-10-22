FROM eclipse-temurin:21

ENV SPRING_PROFILE "default"

RUN mkdir /opt/app
COPY front.jar /opt/app
CMD ["java", "-Dspring.profiles.active=${SPRING_PROFILE}","-jar", "/opt/app/front.jar"]