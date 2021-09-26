# docker
* image: image file, may be built from tar file
* container: running image. may be saved or exported.

## basic commands:
* start docker engine
`open -g -a Docker.app  # mac`

* pull base alpine linux image
`docker pull frolvlad/alpine-glibc`

* build
`docker build - < docker_file`
`docker build -t helloapp:v1`

* docker clean leftover containers
`docker rm -v $(docker ps -a -q -f status=exited)`

* delete all images
`docker rmi -f <image id or image name>`

* list images
`docker images`

* save running container
`docker commit <container id>  <tag>`

* export
`docker export <container id> -o <tar file>`

* import
`docker import <alpine_base.tar> - [REPOSITORY[:TAG]]`

* update image tag
`docker tag c701823c6748 alpine:latest`

* docker command
```
docker run -i -t alpine:latest /bin/sh
docker rm -f CONTAINER_ID   # force stop and clean up container
docker ps   # list running container
docker exec -it <container id> /bin/bash  # start a new shell
```

* docker network
```
docker network ls
docker network create nkk --driver bridge
docker network rm nkk
```

* exit container: `exit`


## Dockerfile: docker build...
* build context: current working directory
* RUN, COPY, ADD create layers.

### instructions
* RUN
* CMD
* EXPOSE: port
* ENV: environment variables
* ADD / COPY: prefer COPY
* ENTRYPOINT
* VOLUME: mutable and/or user-serviceable parts of image.
* USER
* WORKDIR