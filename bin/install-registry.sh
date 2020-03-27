
mkdir $TOOLS/registry && chown -R 200 $TOOLS/registry
docker run -d -p 9001:5000 --restart=always --name registry -v $TOOLS/registry:/var/lib/registry registry:2
