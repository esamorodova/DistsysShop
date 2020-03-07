FROM openjdk:11.0-slim AS builder
COPY . /data
RUN /data/gradlew -p /data bootJar

FROM openjdk:11.0-slim
COPY --from=builder data/build/libs/shop-0.0.1-SNAPSHOT.jar /boot.jar
CMD java -jar /boot.jar
EXPOSE 8080