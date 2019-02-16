# docker
* image: image file, may be built from tar file
* container: running image. may be saved or exported.

## basic commands:
    open -g -a Docker.app  # mac

[comment]: #(pull base alpine linux image)
    docker pull frolvlad/alpine-glibc

[comment]: #(docker clean leftover containers)
    docker rm -v $(docker ps -a -q -f status=exited)

[comment]: #(delete all images)
    docker rmi -f <image id or image name>

[comment]: #(list images)
    docker images

[comment]: #(save running container)
    docker commit <container id>  <tag>

[comment]: #(export)
    docker export <container id> -o <tar file>

[comment]: #(import)
    docker import <alpine_base.tar> - [REPOSITORY[:TAG]]

[comment]: #(update image tag)
    docker tag c701823c6748 alpine:latest

[comment]: #(docker command)
    docker run -i -t alpine:latest /bin/sh
    docker rm -f CONTAINER_ID   # force stop and clean up container          
    docker ps   # list running container
    docker exec -it <container id> /bin/bash  # start a new shell

[comment]: #(exit container)
    exit

