#!/bin/bash

#INPUTFILE=$1
INPUTFILE=127.0.0.1:8080/signup/
curl -X POST $INPUTFILE -H "Content-Type: application/json" \
    -d '{"username": "ThisIsMyName", "password": "123456", "password_confirmation": "123456", "location": "Algun lugar", "first_name": "Solaris", "last_name": "Gonzales", "gender": "M", "email": "ejemplo@correo.com", "birthdate": "1994-05-12"}' > output.html
