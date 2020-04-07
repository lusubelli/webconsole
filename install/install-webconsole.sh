
$TOOLS/$MAVEN_VERSION/bin/mvn clean package

tar -xzf target/webconsole-1.0-SNAPSHOT-bin.tar.gz -C $HOME/

echo $TOOLS/$JDK/bin/java -jar webconsole-1.0-SNAPSHOT.jar --config "config/application.conf" -cp libs/* &> $HOME/webconsole-1.0-SNAPSHOT/webconsole.sh

yes | cp -rf ./install/application.conf $HOME/webconsole-1.0-SNAPSHOT/config/application.conf

chmod +x $HOME/webconsole-1.0-SNAPSHOT/webconsole.sh
