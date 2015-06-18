# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('search_camera', '0002_auto_20150610_2210'),
    ]

    operations = [
        migrations.AlterField(
            model_name='brandlogo',
            name='brand',
            field=models.OneToOneField(related_name=b'logo', null=True, to='search_camera.Brand'),
        ),
    ]
