
export COMPONENT_NAME="$1"
export JAVA_HOME=./tools/jdk-13.0.2
export MAVEN_HOME=./tools/apache-maven-3.6.3
export BUILD_PATH="$2"
export LOGFILE_PATH="$3"

$MAVEN_HOME/bin/mvn clean install -f $BUILD_PATH/pom.xml >> $LOGFILE_PATH

