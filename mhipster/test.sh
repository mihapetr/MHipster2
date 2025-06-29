#!/bin/bash

# move to project root
cd ../groovy

# run integration test excluding custom code from report
#
groovy retention.groovy ../../src/main/java/com/mihael/jhip/MGenerated.java SOURCE
cd ../..
./mvnw verify -P mhipster-runtime

# todo : write a checker for the success of first test run

# run integration test including custom code in report
groovy retention.groovy ../../src/main/java/com/mihael/jhip/MGenerated.java RUNTIME
cd ../..
./mvnw verify -P mhipster-source

# todo : write a curl/groovy post script with API key
