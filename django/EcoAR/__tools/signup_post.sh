#!/bin/bash

#INPUTFILE=$1
WEBSERVER=192.168.15.2:8080
INPUTFILE="$WEBSERVER"/signup/
echo $INPUTFILE
curl -X POST $INPUTFILE -H "Content-Type: application/json" \
    -d '{"username": "max", "password": "123456", "password_confirmation": "123456", "location": "Algun lugar", "first_name": "Solaris", "last_name": "Gonzales", "gender": "M", "email": "ejemplo@correo.com", "birthdate": "1994-05-12"}' 
