export PROJECT_NAME="$1"
export JOB_NAME="$2"
export BUILD_NUMBER="$3"

export POM_FILE=pom.xml
export NEXUS_REPOSITORY_URL=http://localhost:8081
export NEXUS_REPOSITORY="snapshots-maven"
export NEXUS_USERNAME="admin"
export NEXUS_PASSWORD="admin123"

export JAVA_HOME=$WEBCONSOLE/tools/jdk-13.0.2
export MAVEN_HOME=$WEBCONSOLE/tools/apache-maven-3.6.3

export SOURCES_FOLDER="$WEBCONSOLE/builds/$PROJECT_NAME/$JOB_NAME/sources"
export LOGFILE_PATH="$WEBCONSOLE/builds/$PROJECT_NAME/$JOB_NAME/#$BUILD_NUMBER/logs.log"

echo "------> Maven build" >> $LOGFILE_PATH
$MAVEN_HOME/bin/mvn --version >> $LOGFILE_PATH
echo "<settings>
    <localRepository>\${user.home}/webconsole/repositories/.m2/repository</localRepository>
    <servers>
        <server>
            <id>$NEXUS_REPOSITORY</id>
            <username>$NEXUS_USERNAME</username>
            <password>$NEXUS_PASSWORD</password>
        </server>
    </servers>
</settings>" >> $SOURCES_FOLDER/settings.xml
$MAVEN_HOME/bin/mvn -DaltDeploymentRepository=$NEXUS_REPOSITORY::default::$NEXUS_REPOSITORY_URL/repository/$NEXUS_REPOSITORY -s $SOURCES_FOLDER/settings.xml clean deploy -f $SOURCES_FOLDER/$POM_FILE >> $LOGFILE_PATH


