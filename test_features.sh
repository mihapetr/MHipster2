#!/bin/bash

############################ SETUP ###################################

# jq is required to run this script
if ! command -v jq &>/dev/null; then
    echo "jq is not installed. Please install with 'sudo apt-get install -y jq'"
    exit
fi

# ask user for MHipster credentials (login, password) required for JWT fetching
read -p "MHipster username: " login
read -s -p "MHipster password: " password
echo

# read the package name user has chosen from .yo-rc.json
packageName=$(jq -r '.["generator-jhipster"].packageName' .yo-rc.json)

############################# SOURCE RUN ####################################

echo "changing the retention policy of @MGenerated to SOURCE"
./mhipster/m_generate.sh "$packageName" SOURCE ./src/main/resources/mhipster

echo "performing integration testing"
#./mvnw verify -Pmhipster-it

########################### SUCCESS CHECK #############################

# todo : check if first IT was successful before continuing with the other test

# todo : script to get JWT from user credentials

# todo : post script using JWT

############################# RUNTIME RUN ###############################

echo "changing the retention policy of @MGenerated to RUNTIME"
./mhipster/m_generate.sh "$packageName" SOURCE ./src/main/resources/mhipster

echo "performing integration testing"
#./mvnw verify -Pmhipster-it

# todo : post script using JWT

