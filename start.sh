#!/usr/bin/env bash
mvn clean
mvn package -Dmaven.test.skip=true
docker-compose up -d

#mongod --config /usr/local/etc/mongod.conf