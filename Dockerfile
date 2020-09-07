FROM gradle:6.6.1-jdk11 AS builder

WORKDIR /app
COPY ./app /app
RUN cd /app
RUN gradle clean build

FROM openjdk:11.0.8-slim

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/service.jar

CMD java -Xmx256M -jar /app/service.jar