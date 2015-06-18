from django.core import validators
from django.db import models
from django import forms

#import search_camera.models
#print "----->"
#for i in dir(search_camera.models):
#    print i

from search_camera.models import Brand
#print "----->"

numeric = validators.RegexValidator(r'^[0-9]*$', 'Only numeric characters are allowed.')

class Product(models.Model):

    name                 = models.CharField(max_length=50, null=True)
    description          = models.CharField(max_length=500, null=True)
    image                = models.ImageField(upload_to = 'product_images/%Y/%m/%d', 
        default = 'product_images/None/no-img.jpg', null=True)

    barcode              = models.CharField(max_length=20, null=True, validators=[numeric])
    brand                = models.ForeignKey(Brand, related_name='products', on_delete=models.PROTECT, null=True)

    general_id           = models.CharField(max_length=20, unique=True)

    ecological_score     = models.FloatField(default=0.0)
    users_score          = models.FloatField(default=0.0)
    
    def __unicode__(self):
        return u'%s' % (self.name)

class ProductForm(forms.ModelForm):
    #tags = forms.CharField(widget=forms.Textarea(attrs={'cols': 50, 'rows': 10}))
    description          = forms.CharField(widget=forms.Textarea())
    tags                 = forms.CharField(widget=forms.Textarea())
    brand                = forms.ModelChoiceField(queryset=Brand.objects.all(), required=False)

    class Meta:
        model = Product
        fields = ['name', 'description', 'image', 'barcode', 'brand', 'general_id']


class Comment(models.Model):
    
    product              = models.ForeignKey('Product', related_name="commentaries", on_delete=models.CASCADE, null=True)
    product              = models.ForeignKey(Brand, related_name="commentaries", on_delete=models.CASCADE, null=True)

    body                 = models.CharField(max_length=1000)
    posting_date         = models.DateTimeField(auto_now=False, auto_now_add=False)
    user                 = models.ForeignKey('auth.User', related_name="commentaries", on_delete=models.CASCADE, null=True)

    def __unicode__(self):
        return u'%s - %s' % (self.user, self.body)
    
class UsersScore(models.Model):

    # The related_name='+' value ensures that the UsersScore model won't have a backwards relation
    # The on_delete=models:CASCADE ensures that the object who has the key will be deleted in cascade
    #brand                = models.OneToOneField(Brand, related_name='users_score_model', on_delete=models.CASCADE, null=True)
    product              = models.OneToOneField('Product', related_name='users_score_model', on_delete=models.CASCADE, null=True)
    brand                = models.OneToOneField(Brand, related_name='users_score_model', on_delete=models.CASCADE, null=True)

    one                  = models.BigIntegerField(default=0)
    two                  = models.BigIntegerField(default=0)
    three                = models.BigIntegerField(default=0)
    four                 = models.BigIntegerField(default=0)
    five                 = models.BigIntegerField(default=0)


class ProductTag(models.Model):

    products             = models.ManyToManyField(Product, related_name="tags")    
    word                 = models.CharField(max_length=23, unique=True) 

    def __unicode__(self):
        return u'%s' % (self.word)