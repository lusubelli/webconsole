
if [ ! -d "${REPOSITORIES}/npm" ]; then
     mkdir -p "${REPOSITORIES}/npm"
fi

if [ ! -d "$TOOLS/${NPM_VERSION}-win-x64" ] && [ ! -d "$TOOLS/${NPM_VERSION}-linux-x64" ]; then

    case "$OSTYPE" in
        linux-gnu)
            ARCHIVE=${NPM_VERSION}-linux-x64.tar.xz
            ;;
        msys)
            ARCHIVE=${NPM_VERSION}-win-x64.zip
            ;;
        *)
            echo "Sorry, I don't understand"
            ;;
      esac

    if [ ! -f "$TOOLS/$ARCHIVE" ]; then
        wget ${NPM_REPOSITORY}/${ARCHIVE} -P $TOOLS
    fi

    if [ "$OSTYPE" == "linux-gnu" ]; then
        tar xvf $TOOLS/$ARCHIVE -C $TOOLS
    else
        unzip $TOOLS/$ARCHIVE -d $TOOLS
    fi

    rm -rf $TOOLS/$ARCHIVE

fi
