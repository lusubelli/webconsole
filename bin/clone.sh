export COMPONENT_NAME="$1"
export GIT_REPOSITORY="$2"
export GIT_BRANCH="$3"
export BUILD_PATH="$4"
export LOGFILE_PATH="$5"

git clone --progress -b $GIT_BRANCH $GIT_REPOSITORY $BUILD_PATH &> $LOGFILE_PATH
