from django.shortcuts import render_to_response
from django.http import HttpResponse

#from rest_framework.response import JSONResponse

from serializers import ProductoSerializer
from models import Producto
from rest_framework.renderers import JSONRenderer

def search(request):
    if request.method == 'GET':
        productos = Producto.objects.all()
        serializer = ProductoSerializer(productos, many=True)
        return HttpResponse(
            content=JSONRenderer().render(serializer.data),
            content_type = 'application/json'
            )

    """
    if request.method == 'GET':
        res = {'titulo' : 'Buscar producto',
               'found_list' : range(10) 
               }
        return render_to_response('searchProduct/search.html', res)        
    else:    
        return render_to_response('searchProduct/search.html', {'titulo' : 'Buscar producto',
               'found_list' : list()
               })
    """
