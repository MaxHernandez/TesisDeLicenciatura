
from rest_framework import status
from rest_framework import permissions 
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.parsers import JSONParser
from rest_framework.renderers import TemplateHTMLRenderer

from django.core.files import File
from django.core.files.temp import NamedTemporaryFile
from django.core.files.base import ContentFile
from django.core.context_processors import csrf

from parsers import ImageJPGParser

from products.models import ProductTag
from models import Brand, BrandLogo
from models import BrandForm, BrandLogoForm

from serializers import SearchCameraChecker, BrandSerializer, BrandLogoSerializer

# IMAGENES
from ecoglasses.LogoDetection import preprocessFrame, isLogoInImage 
from ecoglasses.FeatureExtraction import FeatureExtractor
from ecoglasses.Matcher import loadKeypoints
from ecoglasses.matchers import BFMatcher

from enchant.checker import SpellChecker
from PIL import Image
import numpy, cv2
import zbar
import pytesseract
import enchant
import pickle


def search_barcode(image_pil):

    # configure the reader
    scanner = zbar.ImageScanner()

    width, height = image_pil.size
    image = zbar.Image(width, height, 'Y800', image_pil.tostring())
    
    scanner.scan(image)
    
    for symbol in image:
        yield symbol.data.decode(u'utf-8')
    #if image != 0:
    #    sData = [s for s in image][-1]
    #    return sData.data.decode(u'utf-8')
    #else:
    #    return None

def search_ocr(image_pil):
	output_word_list = list()
	output_text = pytesseract.image_to_string(image_pil, lang='spa')

	spell_checker = SpellChecker('es_MX', output_text)
	for (word_array, pos) in spell_checker._tokens:
		word = spell_checker._array_to_string(word_array)

		if not spell_checker.dict.check(word):
			suggest_list = SpellChecker.suggest(error_word.word)
			if suggest_list:
				word = suggest_list[0]

		# Revisar si esta en la base de datos 
		if ProductTag.objects.filter(word = word).exists():
			output_word_list.append(word)


	return output_word_list

class SearchLogo():

	logo_list = list() # Query

	def search(self, image_pil):

		image_array = numpy.asarray(image_pil)
		image_template = FeatureExtractor(cvImage=image_array)
		#image_template = preprocessFrame(image_array)

		for brand in Brand.objects.all():

			keypoints = loadKeypoints(brand.logo.keypoints_file.path)
			if not keypoints:
				print "[!] Template for '%s' not found, the sequence is broken, end reached." % (path)
				continue

			descriptors = numpy.load(brand.logo.descriptors_file.path)
			array = numpy.load(brand.logo.array_image_file.path)

			logo_template = {
				'keypoints': keypoints,
				'descriptors': descriptors,
				'array': array
			}

			#if isLogoInImage(logo_template, image_template):
			if BFMatcher.run(image_template, logo_template):
				return brand

			print "VERIFICANDO..."
		return None


def search_on_image(image_pil):
	if not image_pil: return None

	output = dict()

	search_logo = SearchLogo()
	logo_brand = search_logo.search(image_pil)
	if logo_brand: output['brand'] = logo_brand
	#print "Barcode:", [i for i in search_barcode(filtered_image_pil)]
	#print "Text-OCR:", search_ocr(filtered_image_pil)
	ocr_query = search_ocr(image_pil)
	if ocr_query: output['query'] = ocr_query

    #Image.fromarray(pil_image, 'RGB').show()
    #pil_image.show()

	print "terminado"
	return output


class SearchCamera(APIView):

	permission_classes = (permissions.AllowAny,)
	#parser_classes = (ImageJPGParser,)

	def post(self, request, format=None):
		print "Request:", request.GET, request.FILES

		#if request.FILES.has('product_image'):
		search_camera_checker = SearchCameraChecker(data=request.DATA, files=request.FILES)
		if search_camera_checker.is_valid():
			image_rawdata = request.FILES['product_image']
			image_pil = Image.open(image_rawdata)		
			#image_array = numpy.asarray(image_pil) # PIL to Numpy array
 	   		#Image.fromarray(image_array, 'RGB') # Numpy array to PIL
			#image_pil.show()

			search_on_image_output = search_on_image(image_pil)
			data = dict()

			if search_on_image_output != None:

				if search_on_image_output.has_key('brand'):
					brand = search_on_image_output['brand']
					brand_serializer = BrandSerializer(brand, many=False)

					search_on_image_output['brand'] = brand_serializer.data

				elif search_on_image_output.has_key('query'):
					search_on_image_output['query'] = ', '.join(search_on_image_output['query'])

				data.update(search_on_image_output)

			
			data.update(csrf(request))
			return Response(data, status=status.HTTP_202_ACCEPTED)
		else:
			print "Response:", search_camera_checker.errors 
			return Response(search_camera_checker.errors, status=status.HTTP_412_PRECONDITION_FAILED)


def saveKeypoints(keypoints):
  kArray = list()

  for point in keypoints:
    keypoint = (point.pt, point.size, point.angle, point.response, point.octave, point.class_id)
    kArray.append(keypoint)

  return pickle.dumps(kArray)

class BrandsView(APIView):

	#permission_classes = (permissions.IsAuthenticated,)
	permission_classes = (permissions.AllowAny,)
	renderer_classes = (TemplateHTMLRenderer,)

	def get(self, request, format=None):

		return Response(
			{'brand_form': BrandForm(), 'brand_logo_form': BrandLogoForm()},
			 template_name='add_brand.html')

	def post(self, request, format=None):
		print "Request:"
		print "Data:", request.DATA
		print "FILES:", request.FILES
		
		#brand_serializer = BrandSerializer(data=request.DATA)
		#brand_logo_serializer = BrandLogoSerializer(data=request.DATA, files=request.FILES)
		brand_form = BrandForm(data=request.POST)
		brand_logo_form = BrandLogoForm(files=request.FILES)

		if brand_form.is_valid() and brand_logo_form.is_valid():
			try:
				brand = Brand.objects.get(name=request.DATA['name'])
				brand.logo.keypoints_file.delete()
				brand.logo.array_image_file.delete()
				brand.logo.descriptors_file.delete()
				brand.delete()
			except Brand.DoesNotExist:
				pass

			brand_form.save()
			brand_logo_form.save()	
		#if brand_serializer.is_valid() and brand_logo_serializer.is_valid():
			#brand_serializer.save()
			#brand_logo_serializer.save()

			# To extract keypoints and features from the image
			image_rawdata = request.FILES['image']
			image_rawdata.seek(0)

			image_pil = Image.open(image_rawdata)
			image_array = numpy.asarray(image_pil)
			image_template = FeatureExtractor(cvImage=image_array)

			# Get the instance of the recently saved models 
			brand_model = brand_form.instance
			brand_logo_model = brand_logo_form.instance
			#brand_model = brand_serializer.instance
			#brand_logo_model  = brand_logo_serializer.instance

			# Saving keypoints
			pk = brand_logo_model.pk
			brand_logo_model.keypoints_file.save("keypoints_%s_.kp" % pk, 
				ContentFile(saveKeypoints(image_template['keypoints'])))

			# Saving the image as a numpy array in grayscale
			array_temporary_file = NamedTemporaryFile(delete = True)
			numpy.save(array_temporary_file, image_template['array'])
			brand_logo_model.array_image_file.save("array_image_%s_.npy" % pk, File(array_temporary_file))
			
			# Saving descriptors
			descriptors_temporary_file = NamedTemporaryFile(delete = True)
			numpy.save(descriptors_temporary_file, image_template['descriptors'])
			brand_logo_model.descriptors_file.save("descriptors_%s_.npy" % pk, File(descriptors_temporary_file))

			# uptading the BrandLogo instance values 
			brand_logo_model.brand = brand_model
			brand_logo_model.save()

			#data = dict()
			#data.update(csrf(request))
			#return Response(data, status=status.HTTP_202_ACCEPTED)
			return Response(
				{'brand_form': brand_form, 'brand_logo_form': brand_logo_form},
				template_name='success.html')
		else:
			#erros = dict()
			#if brand_serializer.errors:
				#errors.update(brand_serializer.errors)
			#if brand_logo_serializer.errors:
				#errors.update(brand_logo_serializer.errors)
			#return Response(errors, status=status.HTTP_412_PRECONDITION_FAILED)

			brand_logo_form.is_valid()
			return Response(
				{'brand_form': brand_form, 'brand_logo_form': brand_logo_form},
				template_name='add_brand.html')






from django.core.exceptions import ObjectDoesNotExist
from django.core.paginator import Paginator, EmptyPage, PageNotAnInteger
from serializers import IdChecker
from serializers import BrandInfoSerializer, CommentSaveSerializer

from products.serializers import CommentSeralizer


PAGINATION_LENGTH = 6


class BrandInfo(APIView):
	
	permission_classes = (permissions.IsAuthenticatedOrReadOnly,)

	def get(self, request, format=None):
		print "Request:", request.GET
		id_checker = IdChecker(data=request.GET)
		if id_checker.is_valid(): 

			try:
				brand = Brand.objects.get(pk=id_checker.data['id'])
			except ObjectDoesNotExist::
				return Response(id_checker.errors, status=status.HTTP_404_NOT_FOUND)

			serializer = BrandInfoSerializer(brand,  many=False)
			data = serializer.data
			data['usersScore'] = {"users_score" : data['users_score']}
			del data['users_score']
			data.update(csrf(request))
			print "Response:", data
			return Response(data, status=status.HTTP_202_ACCEPTED)
		else:
			print "Response:", id_checker.errors 
			return Response(id_checker.errors, status=status.HTTP_412_PRECONDITION_FAILED)

	def post(self, request, format=None):
		print "Request:", request.DATA
		id_checker = IdChecker(data=request.DATA)
		if id_checker.is_valid(): 

			try:
				brand = Brand.objects.get(pk=id_checker.data['id'])
			except ObjectDoesNotExist::
				return Response(id_checker.errors, status=status.HTTP_404_NOT_FOUND)

			serializer = PostProductScore(data=request.DATA, many=False)
			if serializer.is_valid():
				if serializer.save(brand_model=brand):
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
			print "Response:", id_checker.errors
			return Response(id_checker.errors, status=status.HTTP_412_PRECONDITION_FAILED)


class BrandCommentaries(APIView):

	permission_classes = (permissions.IsAuthenticatedOrReadOnly,)

	def get(self, request, format=None):
		print "Request:", request.GET
		id_checker = IdChecker(data=request.GET)
		if id_checker.is_valid():

			try:
				brand = Brand.objects.get(pk=id_checker.data['id'])
			except ObjectDoesNotExist::
				return Response(id_checker.errors, status=status.HTTP_404_NOT_FOUND)
			
			# Codigo para paginar los comentarios del producto
			paginator = Paginator(brand.commentaries.all().order_by('-posting_date'), PAGINATION_LENGTH) 
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
		id_checker = IdChecker(data=request.DATA)
		if general_id_checker.is_valid():

			try:
				brand = Brand.objects.get(pk=id_checker.data['id'])
			except ObjectDoesNotExist::
				return Response(id_checker.errors, status=status.HTTP_404_NOT_FOUND)

			#####################################
			request.DATA['user'] = request.user.id
			request.DATA['brand'] = brand.id

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