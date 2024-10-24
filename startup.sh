#!/bin/bash

ABSOLUTE_PATH="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
profile=$1
image_name="55g-front-server"

#container_name_prefix="55g-front"
container_name="55g-front-live"
spring_env="live,docker"
server_port=(8080 8081)
network_bridge="55g-live"
container_prefix="live"

if [ "$profile" == "--dev" ]; then
	container_name="55g-front-dev"
	spring_env="dev,docker"
	server_port=(8090)
	network_bridge="55g-dev"
	container_prefix="dev"
fi

cd $ABSOLUTE_PATH

docker_ps=$(docker ps --all --filter "name=${container_name}" | awk '{ print $1 }')

docker_network_live_ps=$(docker network ls | grep '55g-live')
if [ -z "$docker_network_live_ps" ]; then
  docker network create 55g-live
fi

docker_network_dev_ps=$(docker network ls | grep '55g-dev')
if [ -z "$docker_network_dev_ps" ]; then
  docker network create 55g-dev
fi

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

docker build -t $image_name-$container_prefix .

for ((i=0; i<${#server_port[@]}; i++)); do
    docker run -d --name $container_name-$i \
     --network $network_bridge \
     --env SPRING_PROFILE=$spring_env \
     --env SERVER_PORT=${server_port[i]} \
     -p ${server_port[i]}:${server_port[i]} \
     -v /logs:/logs \
     -v /var/55g/static:/static \
     $image_name-$container_prefix
done

docker image prune --force
