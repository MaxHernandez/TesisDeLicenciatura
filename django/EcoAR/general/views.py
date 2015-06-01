from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework import permissions 

from django.contrib import auth 
from django.contrib.auth.models import User
from django.views.decorators.csrf import csrf_exempt, ensure_csrf_cookie
from django.core.context_processors import csrf

from serializers import UserDataSerializer, UserDataAdmin, AuthSerializer
from authentications import IgnoreCSRFSessionAuthentication

class Session(APIView):
    permission_classes = (permissions.IsAuthenticated,)
    authentication_classes = (IgnoreCSRFSessionAuthentication,)
    
    def get(self, request, format=None):
        serializer = UserDataSerializer(request.user, many=False)
        data = serializer.data
        data.update(csrf(request))
        return Response(data, status=status.HTTP_202_ACCEPTED)

class CheckSession(APIView):
    permission_classes = (permissions.IsAuthenticated,)
    authentication_classes = (IgnoreCSRFSessionAuthentication,)
    
    def get(self, request, format=None):
        return Response(csrf(request), status=status.HTTP_200_OK)

class SignUp(APIView):

    permission_classes = (permissions.AllowAny,)
    authentication_classes = (IgnoreCSRFSessionAuthentication,)

    @csrf_exempt
    def post(self, request, format=None):
        user_data_admin = UserDataAdmin(data=request.DATA)
        if user_data_admin.save():
            return Response(dict(), status=status.HTTP_201_CREATED)
        else:
            return Response(user_data_admin.get_errors(), status=status.HTTP_400_BAD_REQUEST)

class LogIn(APIView):
    """
    Estas dos lineas de abajo sirven desacivar la proteccion CSRF en un APIView dado.
    """
    permission_classes = (permissions.AllowAny,)
    authentication_classes = (IgnoreCSRFSessionAuthentication,)

    @csrf_exempt
    def post(self, request, format=None):
        """ Verifica el username y password con authenticate en sus validaciones.
            Se usa el serializer del AuthToken solo por que es comodo verificar con
            su serializer que el username y password tengan el formato correcto, no
            es obligacion utilizar una autenticacion con Token.
        """
        # Este serializer sirve para verificar el nombre de usuario y contrasena
        user_login = AuthSerializer(data=request.DATA)
        if user_login.is_valid():
            user = user_login.object['user']
            auth.login(request, user)

            serializer = UserDataSerializer(user, many=False)
            data = serializer.data
            data.update(csrf(request))
            return Response(data, status=status.HTTP_202_ACCEPTED)
        else:
            return Response(user_login.errors, status=status.HTTP_401_UNAUTHORIZED)

class LogOut(APIView):
    permission_classes = (permissions.IsAuthenticated,)

    @csrf_exempt
    def delete(self, request, format=None):
        auth.logout(request)
        return Response(dict(), status=status.HTTP_202_ACCEPTED)
