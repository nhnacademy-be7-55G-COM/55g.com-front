#!/bin/bash

profile=$1
image_name="55g-front-server"

container_name_prefix="55g-front-live"
spring_env="default"
server_port=(8080 8081)

if [ "$profile" == "--dev"]; then
	container_name_prefix="55g-front-dev"
	spring_env="dev"
	server_port=(8090)
fi

for ((i=0; i<${#server_port[@]}; i++)); do
    docker stop $container_name_prefix-$i
    docker rm $container_name_prefix-$i
done

docker build -t $image_name .

for ((i=0; i<${#server_port[@]}; i++)); do
    docker run -d --name $container_name_prefix-$i --env SPRING_PROFILE=$spring_env $image_name -p ${server_port[i]}:${server_port[i]}
done
