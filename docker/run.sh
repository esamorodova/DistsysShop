#!/bin/bash
set -e
export SPRING_DATASOURCE_URL="jdbc:postgresql://${DB_HOST}:${DB_PORT:-5432}/${DB_DATABASE:-postgres}"
wait-for-it "${DB_HOST}":"${DB_PORT:-5432}" -- echo 'Wait for DB complete'
exec java -jar /boot.jar