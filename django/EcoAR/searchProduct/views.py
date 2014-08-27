from django.shortcuts import render_to_response
from django.http import HttpResponse

from models import Producto
from serializers import ProductoSerializer

from rest_framework.decorators import api_view
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.renderers import JSONRenderer


from django.views.decorators.csrf import csrf_exempt

class SearchBarView(APIView):

    @csrf_exempt
    def get(self, request, format=None):
        productos = Producto.objects.all()
        serializer = ProductoSerializer(productos, many=True)
        return Response(serializer.data)

    @csrf_exempt
    def post(self, request, format=None):
        serializer = ProductoSerializer(data=request.DATA)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class SignIn(APIView):
    """
    
    """

class Login(APIView):
    """

    """
    
class Logout(APIView):
    """

    """

class LastProducts(APIView):
    """
    
    """

