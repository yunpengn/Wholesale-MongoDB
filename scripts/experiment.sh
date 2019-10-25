#!/bin/bash

# Schedules a job on a certain machine.
schedule_job() {
  jobID=$1
  consistencyLevel=$2
  echo "Begins to schedule job with job ID=$((jobID + 1)):"

  machineID="xcnd$((25 + $jobID % 5))"
  echo "The job will be scheduled on machine IP=${machineID}."

  input_file="data/xact-files/$((jobID + 1)).txt"
  stdout_file="log/$((jobID + 1)).out.log"
  stderr_file="log/$((jobID + 1)).err.log"
  ssh $machineID "cd /temp/cs4224f/Wholesale-MongoDB && java -jar build/libs/Wholesale-MongoDB-1.0-SNAPSHOT-all.jar run ${consistencyLevel} < ${input_file} > ${stdout_file} 2> ${stderr_file} &" > /dev/null 2>&1 &
  echo "Have runned job ID=$((jobID + 1)) with input from ${input_file}."
}

# Schedules an experiment by running a certain number of jobs.
schedule_experiment() {
  # Cleans on each machine.
  for ((c=0; c<5; c++)); do
    machineID="xcnd$((25 + $c % 5))"
    ssh $machineID "cd /temp/cs4224f/Wholesale-MongoDB && rm -rf log && mkdir log"
    echo "Have built on machine ID=${machineID}."
  done

  # Schedules job one-by-one.
  for ((c=0; c<$1; c++)); do
    schedule_job $c $2
  done
}

schedule_build() {
  # Builds on each machine.
  for ((c=0; c<5; c++)); do
    machineID="xcnd$((25 + $c % 5))"
    ssh $machineID "cd /temp/cs4224f/Wholesale-MongoDB && git pull && ./gradlew shadowJar"
    echo "Have built on machine ID=${machineID}."
  done
}

schedule_kill() {
  # Kills on each machine.
  for ((c=0; c<5; c++)); do
    machineID="xcnd$((25 + $c % 5))"
    ssh $machineID "ps aux | grep -ie wholesale | grep -v grep | awk '{print $2}' | xargs kill -9"
    echo "Have killed on machine ID=${machineID}."
  done
}

# Schedules an experiment.
if [[ "$1" == "build" ]]; then
  echo "Begins to build on all machines."
  schedule_build
elif [[ "$1" == "run" ]]; then
  if [[ $2 == "" ]]; then
    echo "Please specify # of Java instances."
    exit
  fi
  if [[ $3 == "" ]]; then
    echo "Please specify consistency level."
    exit
  fi
  echo "Begins an experiment with size=$2."
  schedule_experiment $2 $3
elif [[ "$1" == "kill_all" ]]; then
  echo "Begins to kill all Java instances."
  schedule_kill
else
    echo "Unknown command"
fi
