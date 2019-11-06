#!/usr/bin/env bash

project_path=$(dirname $(dirname $(realpath $0)))

run() {
  cd $project_path
  ./gradlew shadowJar
  java -jar build/libs/Wholesale-MongoDB-1.0-SNAPSHOT-all.jar run $2 <$1
}

load_data() {
  cd $project_path
  ./gradlew shadowJar
  java -jar build/libs/Wholesale-MongoDB-1.0-SNAPSHOT-all.jar loaddata
}

calculate_stats() {
  cd $project_path
  ./gradlew shadowJar
  java -jar build/libs/Wholesale-MongoDB-1.0-SNAPSHOT-all.jar stats $1 $2
}

strip_log() {
  cd $project_path
  ./gradlew shadowJar
  java -jar build/libs/Wholesale-MongoDB-1.0-SNAPSHOT-all.jar strip $1
}

if [[ "$1" == "run" ]]; then
  run $2 $3
elif [[ "$1" == "loaddata" ]]; then
  load_data
elif [[ "$1" == "stats" ]]; then
  calculate_stats $2 $3
elif [[ "$1" == "strip" ]]; then
  calculate_stats $2
else
  echo "unknown command"
fi
