export PROJECT_NAME="$1"
export JOB_NAME="$2"
export BUILD_NUMBER="$3"

export NEXUS_REPOSITORY_URL=http://localhost:8081
export NEXUS_REPOSITORY="snapshots-maven"
# export NEXUS_REPOSITORY="snapshots-node"
export NEXUS_USERNAME="admin"
export NEXUS_PASSWORD="admin123"
export VERSION="1.0-SNAPSHOT"
# export VERSION="0.0.0"

export DOCKER_FILE_NAME=Dockerfile

export DOCKER_BUILD_PATH="$WEBCONSOLE/builds/$PROJECT_NAME/$JOB_NAME/docker"
export LOGFILE_PATH="$WEBCONSOLE/builds/$PROJECT_NAME/$JOB_NAME/#$BUILD_NUMBER/logs.log"

rm -rf $DOCKER_BUILD_PATH
mkdir $DOCKER_BUILD_PATH

export ARTIFACT_URL=$NEXUS_REPOSITORY_URL/service/rest/v1/search/assets/download?sort=version&repository=$NEXUS_REPOSITORY&maven.extension=zip&maven.artifactId=$JOB_NAME&maven.baseVersion=$VERSION
# export ARTIFACT_URL=$NEXUS_REPOSITORY_URL/repository/$NEXUS_REPOSITORY/$JOB_NAME/-/$JOB_NAME-$VERSION.tgz

echo "------> Docker build" >> $LOGFILE_PATH

if curl -X GET -u $NEXUS_USERNAME:$NEXUS_PASSWORD --silent -o "$DOCKER_BUILD_PATH/$JOB_NAME-$VERSION-bin.tar.gz" -L "$ARTIFACT_URL" --output $DOCKER_BUILD_PATH/$JOB_NAME-$VERSION-bin.tar.gz; then
    unzip "$DOCKER_BUILD_PATH/$JOB_NAME-$VERSION-bin.tar.gz" -d "$DOCKER_BUILD_PATH" >> $LOGFILE_PATH
    if [[ -f "$DOCKER_BUILD_PATH/$JOB_NAME-$VERSION-bin.tar.gz" ]]; then
        echo "Removing the file.."  >> $LOGFILE_PATH
        rm -f "$DOCKER_BUILD_PATH/$JOB_NAME-$VERSION-bin.tar.gz"
    fi
	docker login -u $NEXUS_USERNAME -p $NEXUS_PASSWORD $NEXUS_REPOSITORY_URL
	docker build -t $NEXUS_REPOSITORY_URL/$NEXUS_REPOSITORY/$JOB_NAME:$VERSION $DOCKER_BUILD_PATH
	docker push $NEXUS_REPOSITORY_URL/$NEXUS_REPOSITORY/$JOB_NAME:$VERSION
	docker logout
else
    echo "Something went wrong" >> $LOGFILE_PATH
fi

echo "Done!" >> $LOGFILE_PATH
