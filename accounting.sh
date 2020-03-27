#!bin/bash

docker-compose -f accounting-infrastructure.yml down
docker-compose -f accounting-infrastructure.yml rm -f
docker-compose -f accounting-infrastructure.yml up -d
docker-compose -f accounting-infrastructure.yml logs -f user-api organization-api backend frontend webconsole mongodb 2>&1 | tee logs/logs.log &
