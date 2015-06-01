# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('products', '0002_auto_20150530_1533'),
    ]

    operations = [
        migrations.AlterField(
            model_name='product',
            name='ecological_score',
            field=models.FloatField(default=0.0),
        ),
        migrations.AlterField(
            model_name='product',
            name='users_score',
            field=models.FloatField(default=0.0),
        ),
    ]
