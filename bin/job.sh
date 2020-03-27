
export COMPONENT_VERSION=1.0.0
export COMPONENT_ENVIRONMENT=dev
export COMPONENT_NAME=user-api

export GIT_REPOSITORY=https://github.com/lusubelli/$COMPONENT_NAME.git
export GIT_BRANCH=master

export DOCKER_LOGIN=developer
export DOCKER_PASSWORD=$(oc whoami -t)
export DOCKER_REGISTRY=127.0.0.1:5000
export DOCKER_REPOSITORY=accounting-dev

export DOCKER_IMAGE_NAME=user-api
export DOCKER_IMAGE_VERSION=1.0.0

export TOOLS=./tools

./bin/install-jdk.sh
export JAVA_HOME=./tools/jdk-13.0.2
$JAVA_HOME/bin/java -version

./bin/install-maven.sh
export MAVEN_HOME=./tools/apache-maven-3.6.3
$MAVEN_HOME/bin/mvn -v

# ./bin/install-docker.sh
# docker -v
# docker-compose -v

# ./bin/install-nexus.sh
# http://localhost:9000/nexus
# user: "admin" password: "admin123"

# ./bin/install-registry.sh
# http://localhost:9001/v2/_catalog

if ./bin/clone.sh; then
    if ./bin/maven.sh; then
        ./bin/docker.sh
    fi
fi



