
export WEBCONSOLE=$HOME/webconsole
export TOOLS=$WEBCONSOLE/tools
export REPOSITORIES=$WEBCONSOLE/repositories
export BIN=$WEBCONSOLE/bin
export BUILDS=$WEBCONSOLE/builds

export JDK=jdk-13.0.2
export JDK_VERSION=openjdk-13.0.2
export JDK_REPOSITORY=https://download.java.net/java/GA/jdk13.0.2/d4173c853231432d94f001e99d882ca7/8/GPL

export MAVEN_VERSION=apache-maven-3.6.3
export MAVEN_REPOSITORY=http://mirrors.standaloneinstaller.com/apache/maven/maven-3/3.6.3/binaries

export NPM_VERSION=node-v12.16.1
export NPM_REPOSITORY=https://nodejs.org/dist/v12.16.1

chmod +x ./install/install-jdk.sh
chmod +x ./install/install-maven.sh
chmod +x ./install/install-docker.sh
chmod +x ./install/install-nexus.sh

echo ===================================
echo $JDK_VERSION
echo $JDK_REPOSITORY
echo $MAVEN_VERSION
echo $MAVEN_REPOSITORY
echo $OSTYPE
echo ===================================
echo "------------- JDK -------------"

./install/install-jdk.sh
export JAVA_HOME=$TOOLS/jdk-13.0.2
$JAVA_HOME/bin/java -version

echo ===================================
echo "------------- MAVEN -------------"

./install/install-maven.sh
export MAVEN_HOME=$TOOLS/apache-maven-3.6.3
$MAVEN_HOME/bin/mvn -v

echo ===================================
echo "------------- NODE -------------"

./install/install-node.sh
export NPM_HOME=$TOOLS/node-v12.16.1-win-x64
$NPM_HOME/node -v

echo ===================================
echo "------------- DOCKER -------------"

./install/install-docker.sh
if command -v docker 2>/dev/null; then
    docker -v
    docker-compose -v
else
    echo "Docker not installed !!!"
fi

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
