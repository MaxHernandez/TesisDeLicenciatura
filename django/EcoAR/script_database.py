from searchProduct.models import Producto

#python manage.py shell < script_database.py

def main():
    print len(Producto.objects.all())

main()
