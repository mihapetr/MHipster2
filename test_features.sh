#!/bin/bash

############################ SETUP ###################################

source ./mhipster/test_features.conf

echo "marking executables"
chmod u+x ./mhipster/get_jwt.sh ./mhipster/m_generate.sh ./mhipster/post.sh

# jq is required to run this script
if ! command -v jq &>/dev/null; then
  	echo "jq is not installed. Please install with 'sudo apt-get install -y jq'"
  	exit
fi

# ask user for MHipster credentials (login, password) required for JWT fetching
# they can be directly passed (for integration testing)
if [ "$#" -eq 0 ]; then
		read -p "MHipster username: " login
    read -s -p "MHipster password: " password
    echo
else
		login="${1}"
		password="${2}"
fi

echo "authenticating at endpoint $BASE_URL/api/authenticate with username $user"
JWT=$(./mhipster/get_jwt.sh "$BASE_URL/api/authenticate" login password)

# read the package name user has chosen from .yo-rc.json
packageName=$(jq -r '.["generator-jhipster"].packageName' .yo-rc.json)

############################# SOURCE RUN ####################################

echo "changing the retention policy of @NotGenerated at $NOT_GENERATED_DIR to SOURCE with packageName $packageName"
./mhipster/m_generate.sh "$NOT_GENERATED_DIR" "$packageName" SOURCE

echo "performing integration testing"
#./mvnw verify -Pmhipster-it

########################### SUCCESS CHECK #############################

# todo : check if first IT was successful before continuing with the other test

echo "posting report at $TEST_REPORT_PATH to $BASE_URL/api/test-reports/of-project/$PROJECT_ID with token $JWT"
./mhipster/post.sh  "$BASE_URL/api/test-reports/of-project/$PROJECT_ID" "$JWT" "$TEST_REPORT_PATH"

############################# RUNTIME RUN ###############################

echo "changing the retention policy of @NotGenerated at $NOT_GENERATED_DIR to RUNTIME with packageName $packageName"
./mhipster/m_generate.sh "$NOT_GENERATED_DIR" "$packageName" RUNTIME

echo "performing integration testing"
#./mvnw verify -Pmhipster-it

echo "posting report at $TEST_REPORT_PATH to $BASE_URL/api/test-reports/of-project/$PROJECT_ID with token $JWT"
./mhipster/post.sh  "$BASE_URL/api/test-reports/of-project/$PROJECT_ID" "$JWT" "$TEST_REPORT_PATH"
