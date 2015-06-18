#!/bin/bash

#INPUTFILE=$1
WEBSERVER=192.168.15.7:8080
INPUTFILE="$WEBSERVER"/login/
echo INPUTFILE
CURL_OUTPUT=$(curl -X POST $INPUTFILE \
    -d '{"username": "max", "password": "123456"}' \
    -H "Content-Type: application/json" \
    -c "login.cookies" \
    );
echo
echo $CURL_OUTPUT > login.output
cat login.output
#cat login.cookies;