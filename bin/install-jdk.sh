export JDK_VERSION=openjdk-13.0.2
export JDK_REPOSITORY=https://download.java.net/java/GA/jdk13.0.2/d4173c853231432d94f001e99d882ca7/8/GPL/

echo $JDK_VERSION
echo $JDK_REPOSITORY

if [ "$OSTYPE" == "linux-gnu" ]; then
    if [ ! -d "$TOOLS/jdk-13.0.2" ]; then
        if [ ! -f "$TOOLS/${JDK_VERSION}_linux-x64_bin.tar.gz" ]; then
            wget ${JDK_REPOSITORY}/${JDK_VERSION}_linux-x64_bin.tar.gz -P $TOOLS
        fi
        tar -zxvf $TOOLS/${JDK_VERSION}_linux-x64_bin.tar.gz -C $TOOLS
    fi
else
    if [ ! -d "$TOOLS/jdk-13.0.2" ]; then
        if [ ! -f "$TOOLS/${JDK_VERSION}_windows-x64_bin.zip" ]; then
            wget ${JDK_REPOSITORY}/${JDK_VERSION}_windows-x64_bin.zip -P $TOOLS
        fi
        unzip $TOOLS/${JDK_VERSION}_windows-x64_bin.zip -d $TOOLS
    fi
fi



