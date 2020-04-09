
if [ ! -d "${REPOSITORIES}/m2" ]; then
     mkdir -p "${REPOSITORIES}/m2"
fi

if [ ! -d "$TOOLS/${MAVEN_VERSION}" ]; then

    if [ ! -f "$TOOLS/${MAVEN_VERSION}-bin.tar.gz" ];  then
        wget $MAVEN_REPOSITORY/$MAVEN_VERSION-bin.tar.gz -P $TOOLS
    fi

    tar -zxvf $TOOLS/$MAVEN_VERSION-bin.tar.gz -C $TOOLS
    rm -rf $TOOLS/$MAVEN_VERSION-bin.tar.gz

fi
