from rest_framework import serializers
from models import Product, Comment
from django.contrib.auth.models import User

class GeneralIdCheker(serializers.Serializer):

	general_id  = serializers.CharField(required=True, max_length=20, min_length=1)


class CommentIdChecker(serializers.Serializer):

	server_id 	= serializers.IntegerField(required=True)


class ProductSerializer(serializers.ModelSerializer):

    class Meta:
        model    = Product
        fields   = ('ecological_score', 'users_score')


class PostProductScore(serializers.Serializer):

	own_score   = serializers.IntegerField(required=True)

	def validate_own_score(self, attrs, source):
		if attrs['own_score'] <= 0 or attrs['own_score'] > 5:
			raise serializers.ValidationError("El valor de campo \"own_score\" debe ser entre 1 y 5.")
		return attrs

	def save(self, product_model=None):
		if isinstance(product_model, Product):
			own_score = self.data['own_score']

			if   own_score == 1:
				product_model.users_score_model.one += 1
			elif own_score == 2:
				product_model.users_score_model.two += 1
			elif own_score == 3:
				product_model.users_score_model.three += 1
			elif own_score == 4:
				product_model.users_score_model.four += 1
			elif own_score == 5:
				product_model.users_score_model.five += 1
			product_model.users_score_model.save()

			product_model.users_score = float(
				product_model.users_score_model.one +
				product_model.users_score_model.two +
				product_model.users_score_model.three +
				product_model.users_score_model.four +
				product_model.users_score_model.five) / 5.0
			product_model.save()
			del self.data['own_score']
			self.data['users_score'] = product_model.users_score
			return True
		else:
			return False


class UsernameSerializer(serializers.ModelSerializer):

    class Meta:
        model    = User
        fields   = ('username',)

class CommentSeralizer(serializers.ModelSerializer):

	user = UsernameSerializer(many=False)

	class Meta:
		model    = Comment
		fields   = ('id', 'body', 'posting_date', 'user')

	def __init__ (self, *args, **kwargs):
		super(serializers.ModelSerializer, self).__init__(*args, **kwargs)

		if type(self.data) == type(list()):
			for comment in self.data:
				comment['username'] = comment['user']['username']
				del comment['user']
		else:
			comment = self.data
			comment['username'] = comment['user']['username']
			del comment['user']

#class CommentListSerializer(serializers.ModelSerializer):
#	"""Esta clase ahora no tiene ninguna funcionalidad."""
	#commentaries = serializers.PrimaryKeyRelatedField(many=True)
#	commentaries = CommentSeralizer(many=True)

#	class Meta:
#		model    = Product
#		fields   = ('commentaries',)


class CommentSaveSerializer(serializers.ModelSerializer):
	"""Esta clase ahora no tiene ninguna funcionalidad."""
	#commentaries = serializers.PrimaryKeyRelatedField(many=True)
	posting_date = serializers.DateTimeField()#By Default django uses 'iso-8601' 
											 #(format='%Y-%m-%d %H:%M:%S')  # '14:30 25/10/06'

	class Meta:
		model    = Comment
		fields   = ('body', 'posting_date', 'user', 'product')
