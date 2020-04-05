if [ ! -d "$TOOLS/${MAVEN_VERSION}" ]; then
    if [ ! -f "$TOOLS/${MAVEN_VERSION}-bin.tar.gz" ];  then
        wget $MAVEN_REPOSITORY/$MAVEN_VERSION-bin.tar.gz -P $TOOLS
    fi
    mkdir ./.m2/
    tar -zxvf $TOOLS/$MAVEN_VERSION-bin.tar.gz -C $TOOLS
    cp -rf ./install/conf/settings.xml $TOOLS/$MAVEN_VERSION/conf/settings.xml
    rm -rf $TOOLS/$MAVEN_VERSION-bin.tar.gz
fi
