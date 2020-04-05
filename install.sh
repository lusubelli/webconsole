export TOOLS=./tools
export BIN=./bin

export JDK_VERSION=openjdk-13.0.2
export JDK_REPOSITORY=https://download.java.net/java/GA/jdk13.0.2/d4173c853231432d94f001e99d882ca7/8/GPL
export MAVEN_VERSION=apache-maven-3.6.3
export MAVEN_REPOSITORY=http://mirrors.standaloneinstaller.com/apache/maven/maven-3/3.6.3/binaries

echo ===================================
echo $JDK_VERSION
echo $JDK_REPOSITORY
echo $MAVEN_VERSION
echo $MAVEN_REPOSITORY
echo $OSTYPE
echo ===================================
echo "------------- JDK -------------"

./install/install-jdk.sh
export JAVA_HOME=./tools/jdk-13.0.2
$JAVA_HOME/bin/java -version

echo ===================================
echo "------------- MAVEN -------------"

./install/install-maven.sh
export MAVEN_HOME=./tools/apache-maven-3.6.3
$MAVEN_HOME/bin/mvn -v

echo ===================================
echo "------------- DOCKER -------------"

./install/install-docker.sh
if command -v sudo 2>/dev/null; then
    docker -v
    docker-compose -v
else
    echo "Docker not installed !!!"
fi

echo ===================================
echo "------------- NEXUS -------------"

./install/install-nexus.sh
# http://localhost:9000/nexus
# user: "admin" password: "admin123"
if ! command -v docker 2>/dev/null; then
    echo "Nexus not installed !!!"
fi

echo ===================================
echo "------------- DOCKER REGISTRY -------------"

./install/install-registry.sh
# http://localhost:9001/v2/_catalog
if ! command -v docker 2>/dev/null; then
    echo "Docker Registry not installed !!!"
fi
