
from rest_framework import status
from rest_framework import permissions 
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.renderers import TemplateHTMLRenderer
from django.core.context_processors import csrf
from django.core.exceptions import MultipleObjectsReturned

from models import Product, UsersScore, Comment, ProductTag
from models import ProductForm
from serializers import ProductSerializer, PostProductScore, CommentSaveSerializer
from serializers import	CommentIdChecker, GeneralIdCheker, CommentSeralizer#, CommentListSerializer
from serializers import ProductByBarcodeChecker, ProductByQueryChecker, ProductInfoSerializer

from django.core.paginator import Paginator, EmptyPage, PageNotAnInteger
from django.db import IntegrityError

import os
import string
import base64
from binascii import hexlify

SERVER_PREFIX = "ECAR"
PAGINATION_LENGTH = 6

class ProductSearchByQuery(APIView):
	permission_classes = (permissions.AllowAny,)

	def get(self, request, format=None):

		serializer = ProductByQueryChecker(data=request.DATA)

		if serializer.is_valid():

			query = serializer.data['query']
			query_list = query.split(', ')

			product_dict = dict()
			product_counter_dict = dict()

			for query_word in query_list:
				temp_product_list = ProductTag.objects.get(word=query_word)
				for product in temp_product_list:

					if product_dict.has_key(product.id):
						product_counter_dict[product.id] += 1
					else:
						product_dict[product.id] = product 
						product_counter_dict[product.id] = 0

			sorted_product_counter_list = sorted(product_counter_dict.items(), reverse=True)						
			product_list = list()

			for (pid, temp) sorted_product_counter_list:
				product_list.append(product_dict[pid])

			# Codigo para paginar los comentarios del producto
			paginator = Paginator(product_list, PAGINATION_LENGTH) 
			page = request.GET.get('page')
			try:
				products = paginator.page(page)
			except PageNotAnInteger:
				products = paginator.page(1)
			except EmptyPage:
				if page <= 0:
					products = paginator.page(1)
				products = list()


			# SERIALIZAR la lista de PRODUCTOS 
			serializer = ProductSerializer(products, many=True)
			data = dict()
			data['products'] = serializer.data
			data.update(csrf(request))
			print "Response:", data
			return Response(data, status=status.HTTP_202_ACCEPTED)
		else:
			print "Response:", general_id_checker.errors 
			return Response(general_id_checker.errors, status=status.HTTP_412_PRECONDITION_FAILED)


class ProductSearchByBarcode(APIView):
	permission_classes = (permissions.AllowAny,)

	def get(self, request, format=None):

		serializer = ProductByBarcodeChecker(data=request.DATA)

		if serializer.is_valid():

			barcode = serializer.data['barcode']
			product_list = Product.objects.filter(barcode=barcode)

			paginator = Paginator(product_list, PAGINATION_LENGTH) 
			page = request.GET.get('page')
			try:
				products = paginator.page(page)
			except PageNotAnInteger:
				products = paginator.page(1)
			except EmptyPage:
				if page <= 0:
					products = paginator.page(1)
				products = list()

			# SERIALIZAR EL PRODUCTO 
			serializer = ProductSerializer(products, many=True)
			data = dict()
			data['products'] = serializer.data
			data.update(csrf(request))
			print "Response:", data
			return Response(data, status=status.HTTP_202_ACCEPTED)

		else:
			print "Response:", general_id_checker.errors 
			return Response(general_id_checker.errors, status=status.HTTP_412_PRECONDITION_FAILED)


class AddProduct(APIView):

	#permission_classes = (permissions.IsAuthenticated,)
	permission_classes = (permissions.AllowAny,)
	renderer_classes = (TemplateHTMLRenderer,)

	def get(self, request, format=None):
		return Response(
			{'form': ProductForm()},
			 template_name='add_product.html')

	def post(self, request, format=None):
		request.POST['general_id'] = base64.urlsafe_b64encode(SERVER_PREFIX + hexlify(os.urandom(6)))[:12]
		print "Request:", request.POST, request.FILES

		product_form = ProductForm(data=request.POST, files=request.FILES)

		if product_form.is_valid():
			product_form.save()

			product = product_form.instance

			tags_string = product_form.cleaned_data['tags']			 
			word_list = filter(None,[word.strip(string.punctuation)
                 for word in tags_string.replace(';','; ').split()
                 ])
			for word in word_list:
				try:
					tag = ProductTag.objects.create(word=word)
				except IntegrityError as e:
					pass
				product.tags.add(tag)

			product.users_score_model = UsersScore.objects.create(product=product)
			product.save()

			return Response(
				{'form': product_form},
				template_name='success.html')
		else:
			return Response(
				{'form': product_form},
				template_name='add_product.html')


def get_product_from_data(data):
	product = None
	try:
		product = Product.objects.get(general_id=data.get('general_id'))
	except Product.DoesNotExist:
		product =  Product.objects.create(general_id=data.get('general_id'))
		product.users_score_model = UsersScore.objects.create(product=product)
		product.users_score_model.save()
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
	
	permission_classes = (permissions.IsAuthenticatedOrReadOnly,)

	def get(self, request, format=None):
		print "Request:", request.GET
		general_id_checker = GeneralIdCheker(data=request.GET)
		if general_id_checker.is_valid(): 
			# En caso de haber un problema accediendo al producto
			# Esta funcion devolvera un Response con un mensaje de
			# error
			product = get_product_from_data(request.GET)
			if isinstance(product, Response):
				return product

			serializer = ProductInfoSerializer(product,  many=False)
			data = serializer.data
			data['usersScore'] = {"users_score" : data['users_score']}
			del data['users_score']
			data.update(csrf(request))
			print "Response:", data
			return Response(data, status=status.HTTP_202_ACCEPTED)
		else:
			print "Response:", general_id_checker.errors 
			return Response(general_id_checker.errors, status=status.HTTP_412_PRECONDITION_FAILED)

	def post(self, request, format=None):
		print "Request:", request.DATA
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
					print "Response:", data
					return Response(data, status=status.HTTP_202_ACCEPTED)
				else:
					return Response(dict(), status=status.HTTP_500_INTERNAL_SERVER_ERROR)
			else:
				print "Response:", serializer.errors 
				return Response(serializer.errors, status=status.HTTP_412_PRECONDITION_FAILED)
		else:
			print "Response:", general_id_checker.errors
			return Response(general_id_checker.errors, status=status.HTTP_412_PRECONDITION_FAILED)

class Commentaries(APIView):

	permission_classes = (permissions.IsAuthenticatedOrReadOnly,)

	def get(self, request, format=None):
		print "Request:", request.GET
		general_id_checker = GeneralIdCheker(data=request.GET)
		if general_id_checker.is_valid(): 
			# En caso de haber un problema accediendo al producto
			# Esta funcion devolvera un Response con un mensaje de
			# error
			product = get_product_from_data(request.GET)
			if isinstance(product, Response):
				return product
			
			# Codigo para paginar los comentarios del producto
			paginator = Paginator(product.commentaries.all().order_by('-posting_date'), PAGINATION_LENGTH) 
			page = request.GET.get('page')
			try:
				comments = paginator.page(page)
			except PageNotAnInteger:
				comments = paginator.page(1)
			except EmptyPage:
				if page <= 0:
					comments = paginator.page(1)
				comments = list()

			# Codigo para serializar los commentarios
			serializer = CommentSeralizer(comments, many=True)
			data = dict()
			data["commentaries"] = serializer.data
			data.update(csrf(request))
			print "Response:", data
			return Response(data, status=status.HTTP_200_OK)

		else:
			return Response(dict(), status=status.HTTP_412_PRECONDITION_FAILED)

	def post(self, request, format=None):
		print "Request:", request.DATA
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
					print "Response:", data
					return Response(data, status=status.HTTP_202_ACCEPTED)
				else:
					return Response(dict(), status=status.HTTP_500_INTERNAL_SERVER_ERROR)
			else:
				print "Response:", save_serializer.errors
				return Response(save_serializer.errors, status=status.HTTP_412_PRECONDITION_FAILED)
		else:
			return Response(dict(), status=status.HTTP_412_PRECONDITION_FAILED)

class CommentariesDelete(APIView):

	permission_classes = (permissions.IsAuthenticatedOrReadOnly,)

	def post(self, request, format=None):
		print "Request:", request.DATA
		comment_id = CommentIdChecker(data=request.DATA)
		if comment_id.is_valid():
			try:
				comment = Comment.objects.get(id=request.DATA['id'])
				if request.user.username == comment.user.username:
					comment.delete()
					serializer = CommentSeralizer(comment, many=False)
					data = serializer.data
					data.update(csrf(request))
					print "Response:", data
					return Response(data, status=status.HTTP_202_ACCEPTED)
				else:
					return Response(dict(), status=status.HTTP_403_FORBIDDEN)
			except Comment.DoesNotExist:
				return Response(dict(), status=status.HTTP_404_NOT_FOUND)
			except Exception, e: 
				print "[ERROR]:", str(e)
				return Response(dict(), status=status.HTTP_500_INTERNAL_SERVER_ERROR)	
		else:
			print "Response:", comment_id.errors
			return Response(comment_id.errors, status=status.HTTP_412_PRECONDITION_FAILED)

# Create your views here.
