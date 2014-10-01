from django.db import models

# Create your models here.
class UserData(models.Model):
    GENDER_CHOICES = (
        ('M', 'Masculino'),
        ('F', 'Femenino'),
    )

    location  = models.CharField(max_length=60)
    birthdate = models.DateField()
    gender    = models.CharField(max_length=1, choices=GENDER_CHOICES)
    user      = models.ForeignKey('auth.User', related_name='userdata')
