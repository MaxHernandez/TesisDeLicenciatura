# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('search_camera', '0001_initial'),
        ('products', '0007_auto_20150530_2022'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='product',
            name='users_score_model',
        ),
        migrations.AddField(
            model_name='product',
            name='barcode',
            field=models.CharField(max_length=20, null=True),
            preserve_default=True,
        ),
        migrations.AddField(
            model_name='product',
            name='brand',
            field=models.ForeignKey(related_name=b'products', to='search_camera.Brand', null=True),
            preserve_default=True,
        ),
        migrations.AddField(
            model_name='usersscore',
            name='product',
            field=models.ForeignKey(related_name=b'users_score_model', to='products.Product', null=True),
            preserve_default=True,
        ),
    ]
