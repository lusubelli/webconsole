#!bin/bash

docker-compose -f accounting-infrastructure.yml rm -f
docker-compose -f accounting-infrastructure.yml up
