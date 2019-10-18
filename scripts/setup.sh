#!/usr/bin/env bash

# Defines the domain name of all 5 nodes here.
SERVER_NODES=("xcnd25" "xcnd26" "xcnd27" "xcnd28" "xcnd29")

# Runs a given command on a given machine (the machineID is 0-based index).
execute_command() {
  machineID=$1
  command=$2

  ssh ${SERVER_NODES[machineID]} "$command"
}

execute_command_on_all() {
  command=$1

  for i in {0..4}; do
    execute_command $i $command
  done
}

setup_mongo() {
  # Removes the previous setup.
  command="cd /temp/cs4224f/"
  command+=" && rm -rf mongodb-linux-x86_64-rhel70-4.2.0/"
  command+=" && rm -rf Wholesale-MongoDB/"
  execute_command_on_all $command

  # Download mongo package and clone the repository.
  command="cd /temp/cs4224f/"
  command+=" && wget https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel70-4.2.0.tgz"
  command+=" && tar -zxvf mongodb-linux-x86_64-rhel70-4.2.0.tgz"
  command+=" && rm mongodb-linux-x86_64-rhel70-4.2.0.tgz"
  command+=" && git clone git@github.com:yunpengn/Wholesale-MongoDB.git"
  execute_command_on_all $command

  # Create folders to store data and log.
  command="mkdir /temp/cs4224f/mongodb-linux-x86_64-rhel70-4.2.0/data/"
  command+=" && mkdir /temp/cs4224f/mongodb-linux-x86_64-rhel70-4.2.0/data/s0/"
  command+=" && mkdir /temp/cs4224f/mongodb-linux-x86_64-rhel70-4.2.0/data/s1/"
  command+=" && mkdir /temp/cs4224f/mongodb-linux-x86_64-rhel70-4.2.0/data/s2/"
  command+=" && mkdir /temp/cs4224f/mongodb-linux-x86_64-rhel70-4.2.0/data/s3/"
  command+=" && mkdir /temp/cs4224f/mongodb-linux-x86_64-rhel70-4.2.0/data/s4/"
  command+=" && mkdir /temp/cs4224f/mongodb-linux-x86_64-rhel70-4.2.0/data/s5/"
  command+=" && mkdir /temp/cs4224f/mongodb-linux-x86_64-rhel70-4.2.0/log/"
  execute_command_on_all $command
}

# Driver part.
if [[ "$1" == "setup" ]]; then
  echo "Begins to setup the 5 machines."
  setup_mongo
elif [[ "$1" == "create_cluster" ]]; then
  echo "Begins to create MongoDB cluster."
else
    echo "Unknown command"
fi
