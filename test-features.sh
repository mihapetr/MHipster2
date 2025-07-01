#!/bin/bash

# jq is required to run this script
if ! command -v jq &>/dev/null; then
    echo "jq is not installed. Please install with 'sudo apt-get install -y jq'"
    exit
fi

# read the package name user has chosen from .yo-rc.json
packageName=$(jq -r '.["generator-jhipster"].packageName' .yo-rc.json)

# change the retention policy of @MGenerated to SOURCE
sudo /bin/bash m-generate.sh "$packageName" SOURCE ./src/main/resources/mhipster

# perform integration testing
./mvnw verify -Pmhipster-it

# todo : check if first IT was successful before continuing with the other test

# todo : script to get JWT from user credentials

# todo : post script using JWT

# change the retention policy of @MGenerated to RUNTIME
sudo /bin/bash m-generate.sh "$packageName" SOURCE ./src/main/resources/mhipster

# perform integration testing
./mvnw verify -Pmhipster-it

# todo : post script using JWT

