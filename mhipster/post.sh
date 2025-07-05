#!/bin/bash

SERVER_URL="${1}"
JWT="${2}"
REPORT_PATH="${3}"

RESPONSE=$(
	curl -X POST "$SERVER_URL" \
	-H "Authorization: Bearer $JWT" \
	--data-binary "@$REPORT_PATH"
)

