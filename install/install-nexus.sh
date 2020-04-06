
if [ ! -d ${REPOSITORIES}/.nexus ]; then
    mkdir -p ${REPOSITORIES}/.nexus && chown -R 200 ${REPOSITORIES}/.nexus
fi

if command -v docker 2>/dev/null; then
    docker run -d -p 8081:8081 --name nexus -v ${REPOSITORIES}/.nexus:/nexus-data sonatype/nexus3
fi


