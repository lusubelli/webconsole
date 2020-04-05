export PROJECT_NAME="$1"
export JOB_NAME="$2"
export BUILD_NUMBER="$3"
export GIT_REPOSITORY="$4"
export GIT_BRANCH="$5"
export STAGING="$6"
export BUILD_PATH="$7"

export JAVA_HOME=./tools/jdk-13.0.2
export MAVEN_HOME=./tools/apache-maven-3.6.3

export NEXUS_REPOSITORY_URL=http://localhost:8081
export NEXUS_REPOSITORY="snapshots"
export NEXUS_USERNAME="admin"
export NEXUS_PASSWORD="admin123"

export VERSION="1.0-SNAPSHOT"

export MAVEN_BUILD_PATH="$BUILD_PATH/$PROJECT_NAME/$JOB_NAME/maven"
export DOCKER_BUILD_PATH="$BUILD_PATH/$PROJECT_NAME/$JOB_NAME/docker"
export LOGFILE_PATH="$BUILD_PATH/$PROJECT_NAME/$JOB_NAME/#$BUILD_NUMBER/logs.log"

export POM_FILE=pom.xml

export DOCKER_IMAGE_VERSION=1.0.0
export DOCKER_FILE_NAME=Dockerfile

ARTIFACT="$JOB_NAME-$VERSION"
ARCHIVE="${ARTIFACT}-bin.zip"

rm -rf $MAVEN_BUILD_PATH
rm -rf $DOCKER_BUILD_PATH
mkdir $DOCKER_BUILD_PATH

echo "------> Cloning the sources" &> $LOGFILE_PATH
git clone --progress -b $GIT_BRANCH $GIT_REPOSITORY $MAVEN_BUILD_PATH >> $LOGFILE_PATH



echo "------> Building the application from sources and push it into repository" >> $LOGFILE_PATH
$MAVEN_HOME/bin/mvn --version >> $LOGFILE_PATH
$MAVEN_HOME/bin/mvn -DaltDeploymentRepository=$NEXUS_REPOSITORY::default::$NEXUS_REPOSITORY_URL/repository/$NEXUS_REPOSITORY -s ./install/settings.xml clean deploy -f $MAVEN_BUILD_PATH/$POM_FILE >> $LOGFILE_PATH






echo "------> Downloading the application from the repository" >> $LOGFILE_PATH

if curl -X GET -u $NEXUS_USERNAME:$NEXUS_PASSWORD --silent -o "$DOCKER_BUILD_PATH/${ARCHIVE}" -L "$NEXUS_REPOSITORY_URL/service/rest/v1/search/assets/download?sort=version&repository=$NEXUS_REPOSITORY&maven.extension=zip&maven.artifactId=$JOB_NAME&maven.baseVersion=$VERSION" --output $DOCKER_BUILD_PATH/${ARCHIVE}; then
    unzip "$DOCKER_BUILD_PATH/${ARCHIVE}" -d "$DOCKER_BUILD_PATH" >> $LOGFILE_PATH
    if [[ -f "$DOCKER_BUILD_PATH/${ARCHIVE}" ]]; then
        echo "Removing the file.."  >> $LOGFILE_PATH
        rm -f "$DOCKER_BUILD_PATH/${ARCHIVE}"
    fi
else
    echo "Something went wrong" >> $LOGFILE_PATH
fi
echo "Done!" >> $LOGFILE_PATH

docker login -u $NEXUS_USERNAME -p $NEXUS_PASSWORD $NEXUS_REPOSITORY_URL
docker build -t $NEXUS_REPOSITORY_URL/$NEXUS_REPOSITORY/$JOB_NAME:$DOCKER_IMAGE_VERSION $DOCKER_BUILD_PATH
docker push $NEXUS_REPOSITORY_URL/$NEXUS_REPOSITORY/$JOB_NAME:$DOCKER_IMAGE_VERSION
docker logout
