export MAVEN_VERSION=apache-maven-3.6.3
export MAVEN_REPOSITORY=http://mirrors.standaloneinstaller.com/apache/maven/maven-3/3.6.3/binaries

echo $MAVEN_VERSION
echo $MAVEN_REPOSITORY

if [ ! -d "$TOOLS/${MAVEN_VERSION}" ]; then
    if [ ! -f "$TOOLS/${MAVEN_VERSION}-bin.tar.gz" ];  then
        wget $MAVEN_REPOSITORY/$MAVEN_VERSION-bin.tar.gz -P $TOOLS
    fi
    tar -zxvf $TOOLS/$MAVEN_VERSION-bin.tar.gz -C $TOOLS
    cp -rf ./bin/conf/settings.xml $TOOLS/$MAVEN_VERSION/conf/settings.xml
fi
