from django.db import models


class Product(models.Model):

    name                 = models.CharField(max_length=50, null=True)
    description          = models.CharField(max_length=500, null=True)
    image                = models.ImageField(upload_to = 'product_images/', 
        default = 'product_images/None/no-img.jpg', blank=True, null=True)
    ecological_score     = models.FloatField(default=0.0)
    users_score          = models.FloatField(default=0.0)
    general_id           = models.CharField(max_length=20, unique=True)

    # The related_name='+' value ensures that the UsersScore model won't have a backwards relation
    # The on_delete=models:CASCADE ensures that the related objects related to this will be deleted in cascade
    users_score_model    = models.ForeignKey('UsersScore', related_name='+', on_delete=models.CASCADE, null=True)


class Comment(models.Model):
    
    body                 = models.CharField(max_length=1000)
    posting_date         = models.DateTimeField(auto_now=False, auto_now_add=False)
    user                 = models.ForeignKey('auth.User', related_name="commentaries", null=True)
    product              = models.ForeignKey('Product', related_name="commentaries", null=True)

    
class UsersScore(models.Model):

    one                  = models.BigIntegerField(default=0)
    two                  = models.BigIntegerField(default=0)
    three                 = models.BigIntegerField(default=0)
    four                 = models.BigIntegerField(default=0)
    five                 = models.BigIntegerField(default=0)
