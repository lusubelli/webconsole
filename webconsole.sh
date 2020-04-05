
./tools/apache-maven-3.6.3/bin/mvn clean package
./tools/jdk-13.0.2/bin/java -jar target/webconsole-1.0-SNAPSHOT.jar --config src/main/resources/application.conf -cp target/libs/*
