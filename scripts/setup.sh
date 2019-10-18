#!/usr/bin/env bash

# Defines the domain name of all 5 nodes here.
SERVER_NODES=("xcnd25" "xcnd26" "xcnd27" "xcnd28" "xcnd29")

# Runs a given command on a given machine (the machineID is 0-based index).
execute_command() {
  machineID=$1
  command=$2

  ssh ${SERVER_NODES[machineID]} "$command"
}
