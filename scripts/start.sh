#!/usr/bin/env bash

project_path=$(dirname $(realpath $0))
#cql_path="$project_path/src/main/resources"
#data_path="$project_path/data/data-files"


cd $project_path
../gradlew shadowJar
java -jar build/libs/Wholesale-Cassandra-1.0-SNAPSHOT-all.jar
