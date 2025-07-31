#!/bin/bash

# root directory of the project from environment variable
root=${RTREE_DIR}

 # Data Layer
cd ${root}/RTreeData
mvn clean install

# Business Layer
cd ${root}/RTreeBusiness
mvn clean install

# API Layer
cd ${root}/rtree-api-boot/rtree-api-boot
mvn clean package

# run api in background, and get the PID
nohup java -jar ./target/rtree-api-boot-1.0.jar > /dev/null 2>&1 &
pid=$!
echo "API server started with PID: $pid"

# Web Layer
cd ${root}/RTreeWeb
./scripts/generate_rtree_typescript_api.sh

