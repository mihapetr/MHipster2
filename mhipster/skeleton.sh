#!/bin/bash

# having live output in the terminal and writing to file
cd ..
./mvnw verify -Pskeleton 2>&1 | tee skeleton-output.txt
#groovy Groovy/extract.groovy
