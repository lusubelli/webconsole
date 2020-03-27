mkdir $TOOLS/nexus && chown -R 200 $TOOLS/nexus
docker run -d -p 9000:8081 --name nexus -v $TOOLS/nexus:/nexus-data sonatype/nexus3
