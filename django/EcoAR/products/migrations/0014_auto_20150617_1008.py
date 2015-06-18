# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('search_camera', '0004_auto_20150617_1008'),
        ('products', '0013_auto_20150612_1302'),
    ]

    operations = [
        migrations.AddField(
            model_name='usersscore',
            name='brand',
            field=models.OneToOneField(related_name=b'users_score_model', null=True, to='search_camera.Brand'),
            preserve_default=True,
        ),
        migrations.AlterField(
            model_name='comment',
            name='product',
            field=models.ForeignKey(related_name=b'commentaries', to='search_camera.Brand', null=True),
        ),
    ]
