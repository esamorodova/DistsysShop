#!/bin/bash
set -e
export SPRING_ACTIVEMQ_BROKER_URL="tcp://${MQ_HOST}:${MQ_PORT:-61616}"
wait-for-it "${MQ_HOST}":"${MQ_PORT:-61616}" -- echo 'Wait for MQ complete'
export SPRING_DATASOURCE_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT:-5432}/${DB_DATABASE:-postgres}"
wait-for-it "${DB_HOST}":"${DB_PORT:-5432}" -- echo 'Wait for DB complete'
exec java -jar /boot.jar