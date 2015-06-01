from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework import permissions 
from django.core.context_processors import csrf
from django.core.exceptions import MultipleObjectsReturned

from models import Product, UsersScore, Comment
from serializers import ProductSerializer, PostProductScore, CommentSaveSerializer
from serializers import	CommentIdChecker, GeneralIdCheker, CommentSeralizer#, CommentListSerializer

from django.core.paginator import Paginator, EmptyPage, PageNotAnInteger

def get_product_from_data(data):
	product = None
	try:
		product = Product.objects.get(general_id=data.get('general_id'))
	except Product.DoesNotExist:
		product =  Product.objects.create(general_id=data.get('general_id'), 
		users_score_model=UsersScore.objects.create())
	#except MultipleObjectsReturned:
		# esto no debe pasar
	except Exception, e: 
		print "[ERROR]:", str(e)
		return Response(product, status=status.HTTP_500_INTERNAL_SERVER_ERROR)	
	finally:
		if product == None:
			return Response(dict(), status=status.HTTP_500_INTERNAL_SERVER_ERROR)
		else:
			return product

class ProductInfo(APIView):
	
	permission_classes = (permissions.IsAuthenticated,)

	def get(self, request, format=None):
		general_id_checker = GeneralIdCheker(data=request.GET)
		if general_id_checker.is_valid(): 
			# En caso de haber un problema accediendo al producto
			# Esta funcion devolvera un Response con un mensaje de
			# error
			product = get_product_from_data(request.GET)
			if isinstance(product, Response):
				return product

			serializer = ProductSerializer(product,  many=False)
			data = serializer.data
			data.update(csrf(request))
			return Response(data, status=status.HTTP_202_ACCEPTED)
		else:
			return Response(general_id_checker.errors, status=status.HTTP_412_PRECONDITION_FAILED)

	def post(self, request, format=None):
		general_id_checker = GeneralIdCheker(data=request.DATA)
		if general_id_checker.is_valid(): 
			# En caso de haber un problema accediendo al producto
			# Esta funcion devolvera un Response con un mensaje de
			# error
			product = get_product_from_data(request.DATA)
			if isinstance(product, Response):
				return product

			serializer = PostProductScore(data=request.DATA, many=False)
			if serializer.is_valid():
				if serializer.save(product_model=product):
					data = serializer.data
					data.update(csrf(request))
					return Response(data, status=status.HTTP_202_ACCEPTED)
				else:
					return Response(dict(), status=status.HTTP_500_INTERNAL_SERVER_ERROR)
			else:
				return Response(serializer.errors, status=status.HTTP_412_PRECONDITION_FAILED)
		else:
			return Response(general_id_checker.errors, status=status.HTTP_412_PRECONDITION_FAILED)

class Commentaries(APIView):

	permission_classes = (permissions.IsAuthenticated,)

	def get(self, request, format=None):
		general_id_checker = GeneralIdCheker(data=request.GET)
		if general_id_checker.is_valid(): 
			# En caso de haber un problema accediendo al producto
			# Esta funcion devolvera un Response con un mensaje de
			# error
			product = get_product_from_data(request.GET)
			if isinstance(product, Response):
				return product
			
			# Codigo para paginar los comentarios del producto
			paginator = Paginator(product.commentaries.all(), 6) 
			page = request.GET.get('page')
			try:
				comments = paginator.page(page)
			except PageNotAnInteger:
				comments = paginator.page(1)
			except EmptyPage:
				if page <= 0:
					comments = paginator.page(1)	
				comments = paginator.page(paginator.num_pages)

			# Codigo para serializar los commentarios
			serializer = CommentSeralizer(comments, many=True)
			data = dict()
			data["commentaries"] = serializer.data
			data.update(csrf(request))
			return Response(data, status=status.HTTP_200_OK)

		else:
			return Response(dict(), status=status.HTTP_412_PRECONDITION_FAILED)

	def post(self, request, format=None):
		general_id_checker = GeneralIdCheker(data=request.DATA)
		if general_id_checker.is_valid():
			# En caso de haber un problema accediendo al producto
			# Esta funcion devolvera un Response con un mensaje de
			# error
			product = get_product_from_data(request.DATA)
			if isinstance(product, Response):
				return product
			#####################################
			request.DATA['user'] = request.user.id
			request.DATA['product'] = product.id
			save_serializer = CommentSaveSerializer(data=request.DATA)

			if save_serializer.is_valid():
				if save_serializer.save():

					
					serializer = CommentSeralizer(save_serializer.object, many=False)
					data = serializer.data
					data.update(csrf(request))
					return Response(data, status=status.HTTP_202_ACCEPTED)
				else:
					return Response(dict(), status=status.HTTP_500_INTERNAL_SERVER_ERROR)
			else:
				return Response(serializer.errors, status=status.HTTP_412_PRECONDITION_FAILED)
		else:
			return Response(dict(), status=status.HTTP_412_PRECONDITION_FAILED)

	def delete(self, request, format=None):
		comment_id = CommentIdChecker(data=request.GET)
		if comment_id.is_valid(): 
			try:
				comment = Comment.objects.get(id=request.GET['server_id'])
				if request.user.username == comment.user.username:
					comment.delete()
					serializer = CommentSeralizer(comment, many=False)
					data = serializer.data
					data.update(csrf(request))
					return Response(data, status=status.HTTP_202_ACCEPTED)
				else:
					return Response(dict(), status=status.HTTP_403_FORBIDDEN)
			except Comment.DoesNotExist:
				return Response(dict(), status=status.HTTP_404_NOT_FOUND)
			except Exception, e: 
				print "[ERROR]:", str(e)
				return Response(product, status=status.HTTP_500_INTERNAL_SERVER_ERROR)	
		else:
			return Response(comment_id.errors, status=status.HTTP_412_PRECONDITION_FAILED)

# Create your views here.
