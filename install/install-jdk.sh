


if [ ! -d "$TOOLS/$JDK" ]; then

    case "$OSTYPE" in
        linux-gnu)
            ARCHIVE=${JDK_VERSION}_linux-x64_bin.tar.gz
            ;;
        msys)
            ARCHIVE=${JDK_VERSION}_windows-x64_bin.zip
            ;;
        *)
            echo "Sorry, I don't understand"
            ;;
      esac

    if [ ! -f "$TOOLS/$ARCHIVE" ]; then
        wget ${JDK_REPOSITORY}/${ARCHIVE} -P $TOOLS
    fi

    if [ "$OS" == "linux" ]; then
        tar -zxvf $TOOLS/$ARCHIVE -C $TOOLS
    else
        unzip $TOOLS/$ARCHIVE -d $TOOLS
    fi

    rm -rf $TOOLS/$ARCHIVE

fi
