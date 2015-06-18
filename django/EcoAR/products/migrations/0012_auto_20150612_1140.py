# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('products', '0011_auto_20150611_2231'),
    ]

    operations = [
        migrations.AlterField(
            model_name='product',
            name='image',
            field=models.ImageField(default=b'product_images/None/no-img.jpg', null=True, upload_to=b'product_images/%Y/%m/%d'),
        ),
    ]
