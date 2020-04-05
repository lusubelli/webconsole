if [ "$OSTYPE" == "linux-gnu" ]; then
    if [ ! -d "$TOOLS/jdk-13.0.2" ]; then
        if [ ! -f "$TOOLS/${JDK_VERSION}_linux-x64_bin.tar.gz" ]; then
            wget ${JDK_REPOSITORY}/${JDK_VERSION}_linux-x64_bin.tar.gz -P $TOOLS
        fi
        tar -zxvf $TOOLS/${JDK_VERSION}_linux-x64_bin.tar.gz -C $TOOLS
        rm -rf $TOOLS/${JDK_VERSION}_linux-x64_bin.tar.gz
    fi
else
    if [ ! -d "$TOOLS/jdk-13.0.2" ]; then
        if [ ! -f "$TOOLS/${JDK_VERSION}_windows-x64_bin.zip" ]; then
            wget ${JDK_REPOSITORY}/${JDK_VERSION}_windows-x64_bin.zip -P $TOOLS
        fi
        unzip $TOOLS/${JDK_VERSION}_windows-x64_bin.zip -d $TOOLS
        rm -rf $TOOLS/${JDK_VERSION}_windows-x64_bin.zip
    fi
fi
