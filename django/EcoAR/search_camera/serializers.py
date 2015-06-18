from rest_framework import serializers
from models import *

from django.conf import settings

class SearchCameraChecker(serializers.Serializer):

	product_image = serializers.ImageField()


class BrandSerializer(serializers.HyperlinkedModelSerializer):

	image_url = serializers.SerializerMethodField('get_image_url')

	def get_image_url(self, obj):
		return '%s%s' % (settings.MEDIA_URL, obj.image.url)

	class Meta:
		model    = Brand
		fields   = ('id', 'name', 'description', 'image_url', 'webpage')


class BrandLogoSerializer(serializers.ModelSerializer):

	class Meta:
		model    = BrandLogo
		fields   = ('image',)



###########################################

class IdChecker(serializers.Serializer):
	""" Esta clase sirve para verificar si un campo de id se encuentre
	en el request.
	"""

	id 	= serializers.IntegerField(required=True)


class BrandInfoSerializer(serializers.ModelSerializer):
	""" Este serializer sirve para recuperar los valores de la informacion extra
		de un brand.
	"""

	class Meta:
		model    = Brand
		fields   = ('ecological_score', 'users_score')

class PostProductScore(serializers.Serializer):
	""" Esta clase sirve para calcular y guardar los valores al votar por la 
		calidad de un producto por parte de los usuarios.
	"""

	own_score   = serializers.IntegerField(required=True)

	def validate_own_score(self, attrs, source):
		if attrs['own_score'] <= 0 or attrs['own_score'] > 5:
			raise serializers.ValidationError("El valor de campo \"own_score\" debe ser entre 1 y 5.")
		return attrs

	def save(self, brand_model=None):
		if isinstance(brand_model, Brand):
			own_score = self.data['own_score']

			if   own_score == 1:
				brand_model.users_score_model.one += 1
			elif own_score == 2:
				brand_model.users_score_model.two += 1
			elif own_score == 3:
				brand_model.users_score_model.three += 1
			elif own_score == 4:
				brand_model.users_score_model.four += 1
			elif own_score == 5:
				brand_model.users_score_model.five += 1
			brand_model.users_score_model.save()

			brand_model.users_score = float(
				brand_model.users_score_model.one +
				brand_model.users_score_model.two +
				brand_model.users_score_model.three +
				brand_model.users_score_model.four +
				brand_model.users_score_model.five) / 5.0
			brand_model.save()
			self.data['users_score'] = brand_model.users_score
			return True
		else:
			return False

from products.models import Comment

class CommentSaveSerializer(serializers.ModelSerializer):
	""" Esta clase solo sirve para guardar un comentario en la base de datos.
	"""
	#commentaries = serializers.PrimaryKeyRelatedField(many=True)
	posting_date = serializers.DateTimeField()#By Default django uses 'iso-8601' 
											 #(format='%Y-%m-%d %H:%M:%S')  # '14:30 25/10/06'

	class Meta:
		model    = Comment
		fields   = ('body', 'posting_date', 'user', 'brand')