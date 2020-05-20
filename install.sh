#!/usr/bin/env bash

export WEBCONSOLE=$HOME/webconsole
export TOOLS=$WEBCONSOLE/tools
export REPOSITORIES=$WEBCONSOLE/repositories
export BIN=$WEBCONSOLE/bin
export BUILDS=$WEBCONSOLE/builds

chmod +x ./install/install-docker.sh
chmod +x ./install/install-nexus.sh
chmod +x ./install/install-webconsole.sh

echo "------------- DOCKER -------------"
./install/install-docker.sh
docker -v
docker-compose -v
echo ===================================
echo "------------- NEXUS -------------"
./install/install-nexus.sh
# http://localhost:8081/nexus
# user: "admin" password: "admin123"
if ! command -v docker 2>/dev/null; then
    echo "Nexus not installed !!!"
fi
echo ===================================
echo "------------- WEBCONSOLE -------------"
./install/install-webconsole.sh

${HOME}/webconsole/tools/jdk-14/bin/java -version
${HOME}/webconsole/tools/maven-3/bin/mvn -v
if [[ "$PLATEFORM" == "linux-gnu" ]]; then
    ${HOME}/webconsole/tools/node-v12/bin/node -v
elif  [[ "$PLATEFORM" == "msys" ]]; then
    ${HOME}/webconsole/tools/node-v12/node -v
fi
