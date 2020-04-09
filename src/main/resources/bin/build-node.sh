export PROJECT_NAME="$1"
export JOB_NAME="$2"
export BUILD_NUMBER="$3"

export NEXUS_REPOSITORY_URL=http://localhost:8081
export NEXUS_REPOSITORY="snapshots-node"
export NEXUS_USERNAME="admin"
export NEXUS_PASSWORD="admin123"

export NODE=$HOME/webconsole/tools/node-v12.16.1-win-x64
export NODE_MODULES=$HOME/webconsole/repositories/.npm/node_modules

export SOURCES_FOLDER="$WEBCONSOLE/builds/$PROJECT_NAME/$JOB_NAME/sources"
export LOGFILE_PATH="$WEBCONSOLE/builds/$PROJECT_NAME/$JOB_NAME/#$BUILD_NUMBER/logs.log"

export AUTH=`printf '%s' "$NEXUS_USERNAME:$NEXUS_PASSWORD" | base64`

cd $SOURCES_FOLDER
echo "------> Node build" >> $LOGFILE_PATH
$NODE/node -v 2>&1 | tee $LOGFILE_PATH
$NODE/npm -v 2>&1 | tee $LOGFILE_PATH
$NODE/npm install -g @angular/cli
$NODE/npm install 2>&1 | tee $LOGFILE_PATH
$NODE/npm run build --production 2>&1 | tee $LOGFILE_PATH
# echo _auth=YWRtaW46YWRtaW4xMjM= >> .npmrc
echo _auth=$AUTH >> .npmrc
$NODE/npm publish --tag snapshot --registry $NEXUS_REPOSITORY_URL/repository/$NEXUS_REPOSITORY 2>&1 | tee $LOGFILE_PATH
