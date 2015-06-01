#!/bin/bash

#INPUTFILE=$1
URL=127.0.0.1:8080/logout/
LOGIN="login.output"
PYTHON="fl = open('$LOGIN', 'r'); import json; print json.loads(fl.read())['csrf_token']"
JSONPARSER=$(python -c "$PYTHON")
CSRF="X-CSRFToken: $JSONPARSER"

curl -X POST $URL \
    -H "Content-Type: application/json" \
    -H "$CSRF" \
    -b "login.cookies"
    #-H "X-CSRFToken: LwnG02cmRvj0LEGO7VvOTbHMEkgXagze" \
    #-H "Authorization: Token 8eaa33b5d40d7fb74bfb52d25bb8f587617986e6" \ # Este valor ya no se utiliza desde que quite la autenticacion por token de la funcionalidad del servidor.
    #-b "sessionid sfay9rrgsat46c5zbi6vnwrjlkl5fw81";