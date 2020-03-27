FROM openjdk:11.0-slim AS builder
COPY . /data
RUN /data/gradlew -p /data bootJar

FROM openjdk:11.0-slim
RUN apt-get update && apt-get install -y wait-for-it && rm -rf /var/cache/apt /var/lib/apt/lists
COPY docker/run.sh /run.sh
COPY --from=builder data/build/libs/shop-0.0.1-SNAPSHOT.jar /boot.jar
CMD /run.sh
EXPOSE 8080