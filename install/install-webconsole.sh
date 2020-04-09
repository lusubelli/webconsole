
$TOOLS/$MAVEN_VERSION/bin/mvn clean package

tar -xzf target/webconsole-1.0-SNAPSHOT-bin.tar.gz -C $HOME/webconsole

yes | cp -rf ./install/application.conf $HOME/webconsole/webconsole-1.0-SNAPSHOT/config/application.conf

echo export WEBCONSOLE=$HOME/webconsole &> $HOME/webconsole/webconsole-1.0-SNAPSHOT/webconsole.sh
echo $TOOLS/$JDK/bin/java -jar webconsole-1.0-SNAPSHOT.jar --config "config/application.conf" -cp libs/* >> $HOME/webconsole/webconsole-1.0-SNAPSHOT/webconsole.sh

chmod +x $HOME/webconsole/webconsole-1.0-SNAPSHOT/webconsole.sh
