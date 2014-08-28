from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status

from django.contrib.auth.models import User
from serializers import UserSerializer, UserDataAdmin


from django.views.decorators.csrf import csrf_exempt

class SignUp(APIView):

    @csrf_exempt
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
        return Response(user_data_admin.get_errors(), status=status.HTTP_400_BAD_REQUEST)
