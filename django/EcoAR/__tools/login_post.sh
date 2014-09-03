#!/bin/bash

#INPUTFILE=$1
INPUTFILE=127.0.0.1:8080/login/
CURL_OUTPUT=$(curl -X POST $INPUTFILE \
    -d '{"username": "max", "password": "123456"}' \
    -H "Content-Type: application/json" \
    -c "login.cookies" \
    );
echo $CURL_OUTPUT > login.output
#cat login.cookies;