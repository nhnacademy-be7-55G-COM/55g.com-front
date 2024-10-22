#!/bin/bash

ABSOLUTE_PATH="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
profile=$1
image_name="55g-front-server"

container_name_prefix="55g-front"
container_name="55g-front-live"
spring_env="live"
server_port=(8080 8081)

if [ "$profile" == "--dev" ]; then
	container_name="55g-front-dev"
	spring_env="dev"
	server_port=(8090)
fi

cd $ABSOLUTE_PATH

docker_ps=$(docker ps --all --filter "name=${container_name_prefix}" | awk '{ print $1 }')

i=0
for line in $docker_ps; do
  ps_arr[i]=$line
  i=$((i+1))
done

for ((i=1; i<${#ps_arr[@]}; i++)); do
    echo "Removing container ${ps_arr[i]}..."
    docker stop ${ps_arr[i]}
    docker rm ${ps_arr[i]}
done

docker build -t $image_name .

for ((i=0; i<${#server_port[@]}; i++)); do
    docker run -d --name $container_name-$i --env SPRING_PROFILE=$spring_env -p ${server_port[i]}:${server_port[i]} $image_name
done
