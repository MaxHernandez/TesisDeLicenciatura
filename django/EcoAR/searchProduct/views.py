from django.shortcuts import render_to_response

def search(request):

    if request.method == 'GET':
        res = {'titulo' : 'Buscar producto',
               'found_list' : range(10) 
               }
        return render_to_response('searchProduct/search.html', res)        
    else:    
        return render_to_response('searchProduct/search.html', {'titulo' : 'Buscar producto',
               'found_list' : list()
               })
