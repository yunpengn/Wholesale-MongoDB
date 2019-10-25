#!/usr/bin/env bash

project_path=$(dirname $(dirname $(realpath $0)))
#cql_path="$project_path/src/main/resources"
#data_path="$project_path/data/data-files"


run() {
    cd $project_path
    ./gradlew shadowJar
    java -jar build/libs/Wholesale-MongoDB-1.0-SNAPSHOT-all.jar run $2 < $1
}

load_data() {
    cd $project_path
    ./gradlew shadowJar
    java -jar build/libs/Wholesale-MongoDB-1.0-SNAPSHOT-all.jar loaddata
}


if [[ "$1" == "run" ]]; then
    run $2 $3
elif [[ "$1" == "loaddata" ]]; then
    load_data
else
    echo "unknown command"
fi

