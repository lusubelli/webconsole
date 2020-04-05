if [ ! -d ./.docker ]; then
    mkdir ./.docker && chown -R 200 ./.docker
fi

if command -v docker 2>/dev/null; then
    docker run -d -p 9001:5000 --restart=always --name registry -v ./.docker:/var/lib/registry registry:2
fi
