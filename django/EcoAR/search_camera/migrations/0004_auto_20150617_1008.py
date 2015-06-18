# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('search_camera', '0003_auto_20150611_2131'),
    ]

    operations = [
        migrations.AddField(
            model_name='brand',
            name='ecological_score',
            field=models.FloatField(default=0.0),
            preserve_default=True,
        ),
        migrations.AddField(
            model_name='brand',
            name='users_score',
            field=models.FloatField(default=0.0),
            preserve_default=True,
        ),
    ]
