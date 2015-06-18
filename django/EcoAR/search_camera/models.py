from django.db import models
from django.core.files.storage import FileSystemStorage
from django.conf import settings
from django import forms

#from products import Product
#from products.models import Product

SEARCH_LOGO_STORAGE = FileSystemStorage(location=settings.SEARCH_LOGO_ROOT, base_url='')

class Brand(models.Model):
    name                 = models.CharField(max_length=50, null=True)
    description          = models.CharField(max_length=500, null=True)
    webpage				 = models.CharField(max_length=100, null=True)

    ecological_score     = models.FloatField(default=0.0)
    users_score          = models.FloatField(default=0.0)

    def __unicode__(self):
        return u'%s' % (self.name)

class BrandForm(forms.ModelForm):
	description = forms.CharField(widget=forms.Textarea(attrs={'cols': 50, 'rows': 10}))

	class Meta:
		model = Brand
		exclude = []


class BrandLogo(models.Model):
    image                = models.ImageField(upload_to = 'brand_images/%Y/%m/%d', 
        default = 'brand_images/None/no-img.jpg')

    array_image_file 	= models.FileField(upload_to='array_image_files/', storage=SEARCH_LOGO_STORAGE, null=True)
    keypoints_file		= models.FileField(upload_to='keypoints_files/', storage=SEARCH_LOGO_STORAGE, null=True)
    descriptors_file	= models.FileField(upload_to='descriptors_files/', storage=SEARCH_LOGO_STORAGE, null=True)

    brand               = models.OneToOneField('Brand', related_name='logo', on_delete=models.CASCADE, null=True)


class BrandLogoForm(forms.ModelForm):

	image = forms.ImageField(required=True)

	class Meta:
		model = BrandLogo
		fields = ['image',]