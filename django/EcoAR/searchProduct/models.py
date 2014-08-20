from django.db import models

# Create your models here.


class Producto (models.Model):
    nombre = models.CharField(max_length=50)
    descripcion = models.TextField()
    marca = models.CharField(max_length=50, null=True)
    fabricante =  models.CharField(max_length=50)
    codigo_barras = models.CharField(max_length=20)
    imagen = models.ImageField(upload_to='/products/', null=True)

