# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
import django.core.validators


class Migration(migrations.Migration):

    dependencies = [
        ('products', '0012_auto_20150612_1140'),
    ]

    operations = [
        migrations.AlterField(
            model_name='product',
            name='barcode',
            field=models.CharField(max_length=20, null=True, validators=[django.core.validators.RegexValidator(b'^[0-9]*$', b'Only numeric characters are allowed.')]),
        ),
        migrations.AlterField(
            model_name='producttag',
            name='word',
            field=models.CharField(unique=True, max_length=23),
        ),
    ]
