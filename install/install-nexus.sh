if [ ! -d ./.nexus ]; then
    mkdir ./.nexus && chown -R 200 ./.nexus
fi

if command -v docker 2>/dev/null; then
    docker run -d -p 9000:8081 --name nexus -v ./.nexus:/nexus-data sonatype/nexus3;
fi


