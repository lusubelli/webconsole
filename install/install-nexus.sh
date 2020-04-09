
if [ ! -d "${REPOSITORIES}/nexus" ]; then
    mkdir -p "${REPOSITORIES}/nexus" && sudo chown -R 200 "${REPOSITORIES}/nexus"
fi

if [ -x "$(command -v docker)" ]; then
    docker system prune -f --volumes
    docker container stop $(docker container ls -aq --filter name=nexus)
    docker container rm $(docker container ls -aq --filter name=nexus)
    docker run -d -p 8081:8081 --name nexus -v ${REPOSITORIES}/nexus:/nexus-data sonatype/nexus3
else
    echo docker not installed
fi


