export PROJECT_NAME="$1"
export JOB_NAME="$2"
export BUILD_NUMBER="$3"

export GIT_REPOSITORY="$4"
export GIT_BRANCH="$5"

export LOGFILE_PATH="$WEBCONSOLE/builds/$PROJECT_NAME/$JOB_NAME/#$BUILD_NUMBER/logs.log"
export SOURCES_FOLDER="$WEBCONSOLE/builds/$PROJECT_NAME/$JOB_NAME/sources"

rm -rf $SOURCES_FOLDER
echo "------> Cloning the sources" &> $LOGFILE_PATH
git clone --progress -b $GIT_BRANCH $GIT_REPOSITORY $SOURCES_FOLDER >> $LOGFILE_PATH
