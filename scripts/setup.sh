#!/usr/bin/env bash

# Defines the domain name of all 5 nodes here.
SERVER_NODES=("xcnd25" "xcnd26" "xcnd27" "xcnd28" "xcnd29")

# Runs a given command on a given machine (the machineID is 0-based index).
execute_command() {
  machineID=$1
  current_command=$2

  ssh ${SERVER_NODES[machineID]} $current_command
}

execute_command_on_all() {
  all_command=$1

  for i in {0..4}; do
    execute_command $i "$all_command"
  done
}

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

# Driver part.
if [[ "$1" == "setup" ]]; then
  echo "Begins to setup the 5 machines."
  setup_mongo
elif [[ "$1" == "create_cluster" ]]; then
  echo "Begins to create MongoDB cluster."
else
    echo "Unknown command"
fi
