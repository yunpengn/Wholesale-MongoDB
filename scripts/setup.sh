#!/usr/bin/env bash

# Defines the domain name of all 5 nodes here.
SERVER_NODES=("xcnd25" "xcnd26" "xcnd27" "xcnd28" "xcnd29")

# Runs a given command on a given machine (the machineID is 0-based index).
execute_command() {
  machineID=$1
  current_command=$2
  real_command="source /home/stuproj/cs4224f/.bash_profile && $current_command"

  ssh ${SERVER_NODES[machineID]} $real_command
}

# Runs a given command on all machines.
execute_command_on_all() {
  all_command=$1

  for i in {0..4}; do
    execute_command $i "$all_command"
  done
}

# Runs the initial setup on all machines.
setup_mongo() {
  # Removes the previous setup.
  command="echo 'Will remove files & folders created previously ...'"
  command+=" && cd /temp/cs4224f/"
  command+=" && rm -f mongodb-linux-x86_64-rhel70-4.2.0.tgz*"
  command+=" && rm -rf mongodb-linux-x86_64-rhel70-4.2.0/"
  command+=" && rm -rf Wholesale-MongoDB/"

  # Download mongo package and clone the repository.
  command+=" && echo 'Will download MongoDB and clone from GitHub ...'"
  command+=" && wget --quiet https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel70-4.2.0.tgz"
  command+=" && tar -zxf mongodb-linux-x86_64-rhel70-4.2.0.tgz"
  command+=" && rm mongodb-linux-x86_64-rhel70-4.2.0.tgz"
  command+=" && git clone --quiet git@github.com:yunpengn/Wholesale-MongoDB.git"

  # Create folders to store data and log.
  command+=" && echo 'Will create folders ...'"
  command+=" && mkdir /temp/cs4224f/mongodb-linux-x86_64-rhel70-4.2.0/data/"
  command+=" && mkdir /temp/cs4224f/mongodb-linux-x86_64-rhel70-4.2.0/data/s0/"
  command+=" && mkdir /temp/cs4224f/mongodb-linux-x86_64-rhel70-4.2.0/data/s1/"
  command+=" && mkdir /temp/cs4224f/mongodb-linux-x86_64-rhel70-4.2.0/data/s2/"
  command+=" && mkdir /temp/cs4224f/mongodb-linux-x86_64-rhel70-4.2.0/data/s3/"
  command+=" && mkdir /temp/cs4224f/mongodb-linux-x86_64-rhel70-4.2.0/data/s4/"
  command+=" && mkdir /temp/cs4224f/mongodb-linux-x86_64-rhel70-4.2.0/data/s5/"
  command+=" && mkdir /temp/cs4224f/mongodb-linux-x86_64-rhel70-4.2.0/log/"

  # Executes the command.
  execute_command_on_all "$command"
}

# Creates the replica set for config server.
create_config_server() {
  # Pulls the latest update from GitHub.
  command="echo 'Will pull from GitHub ...'"
  command+=" && cd /temp/cs4224f/Wholesale-MongoDB"
  command+=" && git pull --quiet"

  # Starts the config server with the provided configurations.
  command+=" && mongod --config /temp/cs4224f/Wholesale-MongoDB/scripts/mongod-config/s0.yml"

  # Executes the command.
  for i in {0..2}; do
    execute_command $i "$command"
  done

  # Initiates the replica set.
  command="echo 'Will initiate the replica set ...'"
  command+=" && mongo 127.0.0.1:28000 < /temp/cs4224f/Wholesale-MongoDB/scripts/mongo-scripts/init-s0.js"
  execute_command 0 "$command"
}

# Creates the replica sets for 5 shards.
create_all_shards() {
  # Performs for each of the 5 shards.
  for shardID in {1..5}; do
    # Starts the server with the provided configurations.
    command="mongod --config /temp/cs4224f/Wholesale-MongoDB/scripts/mongod-config/s$shardID.yml"

    # Executes the command for create 3 instances.
    for i in {0..2}; do
      machineID=$((($shardID + $i - 1) % 5))
      execute_command $machineID "$command"
    done

    # Initiates the replica set.
    port=$(( 28000 + $shardID ))
    command="echo 'Will initiate the replica set ...'"
    command+=" && mongo 127.0.0.1:$port < /temp/cs4224f/Wholesale-MongoDB/scripts/mongo-scripts/init-s$shardID.js"
    machindID=$(( $shardID - 1 ))
    execute_command $machineID "$command"
  done
}

# Creates the query routers on all machines.
create_query_router() {
  # Starts query router on each machine.
  command="mongos --config /temp/cs4224f/Wholesale-MongoDB/scripts/mongod-config/router.yml"
  execute_command_on_all "$command"

  # Adds 5 shards to the cluster.
  command="echo 'Will add all shards to the cluster ...'"
  command+=" && mongo 127.0.0.1:29000 < /temp/cs4224f/Wholesale-MongoDB/scripts/mongo-scripts/add-shards.js"

  # Enables sharding on the database & collections.
  command+=" && echo 'Will enable sharding on the database & collections ...'"
  command+=" && mongo 127.0.0.1:29000 < /temp/cs4224f/Wholesale-MongoDB/scripts/mongo-scripts/enable-shard.jsm"
  execute_command 0 "$command"
}

force_kill_all() {
  command="pkill -f mongo && pkill -f mongod"
  execute_command_on_all "$command"
}

# Driver part.
if [[ "$1" == "setup" ]]; then
  echo "Begins to setup the 5 machines."
  setup_mongo
elif [[ "$1" == "create_cluster" ]]; then
  echo "Begins to create MongoDB cluster."

  echo "Starts with the replica set for config server."
  create_config_server

  echo "Continues with the replica sets for 5 shards."
  create_all_shards

  echo "Ends with the query routers."
  create_query_router
elif [[ "$1" == "force_kill_all" ]]; then
  echo "Will force kill all MongoDB instances"
  force_kill_all
else
    echo "Unknown command"
fi
