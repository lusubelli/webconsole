
$TOOLS/$MAVEN_VERSION/bin/mvn clean package

if [ ! -d $HOME/bin ]; then
     mkdir -p $HOME/bin
fi

cp target/webconsole-1.0-SNAPSHOT.jar $HOME/bin/webconsole-1.0-SNAPSHOT.jar
cp ./install/application.conf $HOME/bin/application.conf
cp -R target/libs $HOME/bin/libs
cp -R target/ftlh $HOME/bin/ftlh

echo $TOOLS/$JDK/bin/java -jar bin/webconsole-1.0-SNAPSHOT.jar --config bin/application.conf -cp bin/libs/* >> $HOME/webconsole.sh

chmod +x $HOME/webconsole.sh
