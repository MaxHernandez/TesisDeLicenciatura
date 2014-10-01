from rest_framework import serializers
from models import UserData
from django.contrib.auth.models import User

class UserSignUpSerializer(serializers.ModelSerializer):
    """ Esta clase se encarga de verificar los datos al registrar un nuevo usuario y
    guardar los datos en el modelo de usuarios por defecto de django """

    email        = serializers.EmailField(required = True)
    first_name   = serializers.CharField(required = True)
    last_name    = serializers.CharField(required = True)

    class Meta:
        model    = User
        fields   = ('id', 'username', 'password', 'email', 'first_name', 'last_name')

    #def validate_username(self, attrs, source):
    #    value = attrs[source]
    #    try:
    #        user = User.objects.get(username=value)
    #    except User.DoesNotExist:
    #        return attrs
    #    raise serializers.ValidationError("Ya existe el nombre de usuario")

    def validate_password(self, attrs, source):
        if not self.init_data.has_key("password_confirmation"):
            raise serializers.ValidationError("Las contrasenas no coinciden")
        else:
            if attrs["password"] != self.init_data["password_confirmation"]:
                raise serializers.ValidationError("Las contrasenas no coinciden")
            
        return attrs 
    
    def save(self):
        user = User.objects.create_user(self.data['username'], self.data['email'], self.data['password'])
        user.first_name = self.data['first_name']
        user.last_name = self.data['last_name']
        user.save()
        self.data['id'] = user.id 

class UserDataSignUpCheckerSerializer(serializers.ModelSerializer):
    """Esta clase solo sirve para verificar que los campos sean correctos"""

    class Meta:
        model    = UserData
        fields   = ('location', 'birthdate', 'gender')


class UserDataSignUpSerializer(serializers.ModelSerializer):
    """Esta clase sirve para guardar los datos junto con su relacion con el nuevo usuario"""
    user = serializers.PrimaryKeyRelatedField(many = False, required = True)

    class Meta:
        model    = UserData
        fields   = ('location', 'birthdate', 'gender', 'user')


class UserDataAdmin:

    def __init__(self, data=None):
        self.data = data
        self.user_serializer = None
        self.userData_serializer = None

    def get_errors(self):
        errors = self.user_serializer.errors
        errors.update(self.userData_serializer.errors)
        return errors

    def save(self):
        self.user_serializer = UserSignUpSerializer(data=self.data)
        self.userData_serializer = UserDataSignUpCheckerSerializer(data=self.data)

        if self.user_serializer.is_valid() and self.userData_serializer.is_valid():
            self.user_serializer.save()
            self.data['user'] = self.user_serializer.data['id']
            self.userData_serializer = UserDataSignUpSerializer(data = self.data)
            if self.userData_serializer.is_valid():
                self.userData_serializer.save()
                return True
        return False


class UserLogInSerializer(serializers.ModelSerializer):
    """ Esta clase solo sirve para mostrar la informacion de todos los usuarios registrados """
    userdata  = UserDataSignUpSerializer()

    def __init__ (self, *args, **kwargs):
         super(serializers.ModelSerializer, self).__init__(*args, **kwargs)
         if type(self.data) == type(list()):
             for i in range(len(self.data)):
                 temp = self.data[i].pop('userdata')
                 self.data[i].update(temp[0])
         else:
             temp = self.data.pop('userdata')
             for field in self.Meta.userdata_fields:
                 self.data[field] = temp[0][field]
             #temp = self.data.pop('userdata')
             #self.data.update(temp[0])             

    class Meta:
        model = User
        fields = ('username', 'email', 'first_name', 'last_name', 'userdata')
        userdata_fields = ('birthdate', 'gender')
