#!/bin/bash

#INPUTFILE=$1
INPUTFILE=127.0.0.1:8080/signup/
curl -X GET $INPUTFILE -H "Content-Type: application/json" 
