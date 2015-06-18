# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
import django.core.files.storage


class Migration(migrations.Migration):

    dependencies = [
        ('search_camera', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='brandlogo',
            name='array_image_file',
            field=models.FileField(storage=django.core.files.storage.FileSystemStorage(base_url=b'', location=b'/home/max/django/EcoAR_SEARCH_LOGO/'), null=True, upload_to=b'array_image_files/'),
        ),
        migrations.AlterField(
            model_name='brandlogo',
            name='descriptors_file',
            field=models.FileField(storage=django.core.files.storage.FileSystemStorage(base_url=b'', location=b'/home/max/django/EcoAR_SEARCH_LOGO/'), null=True, upload_to=b'descriptors_files/'),
        ),
        migrations.AlterField(
            model_name='brandlogo',
            name='image',
            field=models.ImageField(default=b'brand_images/None/no-img.jpg', upload_to=b'brand_images/%Y/%m/%d'),
        ),
        migrations.AlterField(
            model_name='brandlogo',
            name='keypoints_file',
            field=models.FileField(storage=django.core.files.storage.FileSystemStorage(base_url=b'', location=b'/home/max/django/EcoAR_SEARCH_LOGO/'), null=True, upload_to=b'keypoints_files/'),
        ),
    ]
