from rest_framework import serializers
from models import Producto

class ProductoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Producto
        fields = ('id',
                  'nombre',
                  'descripcion',
                  'marca',
                  'fabricante',
                  'codigo_barras',
                  'imagen',
                  )
