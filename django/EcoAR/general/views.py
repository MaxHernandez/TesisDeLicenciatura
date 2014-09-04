from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.authtoken.serializers import AuthTokenSerializer
from rest_framework.authtoken.models import Token
from rest_framework import permissions 

from django.contrib import auth 
from django.contrib.auth.models import User
from django.views.decorators.csrf import csrf_exempt, ensure_csrf_cookie
from django.core.context_processors import csrf

from serializers import UserSerializer, UserDataAdmin
from authentications import IgnoreCSRFSessionAuthentication

class SignUp(APIView):

    def get(self, request, format=None):
        """Este metodo sirve solamente para debug """
        query = User.objects.all()
        serializer = UserSerializer(query, many=True)
        return Response(serializer.data)

    @csrf_exempt
    def post(self, request, format=None):
        user_data_admin = UserDataAdmin(data=request.DATA)
        if user_data_admin.save():
            return Response(dict(), status=status.HTTP_201_CREATED)
        else:
            return Response(user_data_admin.get_errors(), status=status.HTTP_400_BAD_REQUEST)


class LogIn(APIView):
    """
    Este es uno de los pocos views donde realmente se necesita desacivar la proteccion CSRF.
    """
    permission_classes = (permissions.AllowAny,)
    authentication_classes = (IgnoreCSRFSessionAuthentication,)

    @csrf_exempt
    def post(self, request, format=None):
        """ Verifica el username y password con authenticate en sus validaciones"""
        user_login = AuthTokenSerializer(data=request.DATA)
        
        if user_login.is_valid():
            user = user_login.object['user']
            auth.login(request, user)
            #token, created = Token.objects.get_or_create(user=user)
            #data = {'token': token.key}
            data = dict()
            data.update(csrf(request))
            return Response(data, status=status.HTTP_202_ACCEPTED)
        else:
            return Response(user_login.errors, status=status.HTTP_400_BAD_REQUEST)

class LogOut(APIView):
    permission_classes = (permissions.IsAuthenticated,)

    @csrf_exempt
    def post(self, request, format=None):
        auth.logout(request)
        return Response(dict(), status=status.HTTP_202_ACCEPTED)
