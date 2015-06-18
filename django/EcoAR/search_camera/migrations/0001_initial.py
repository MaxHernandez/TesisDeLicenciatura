# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
import django.core.files.storage


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Brand',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=50, null=True)),
                ('description', models.CharField(max_length=500, null=True)),
                ('webpage', models.CharField(max_length=100, null=True)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='BrandLogo',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('image', models.ImageField(default=b'brand_images/None/no-img.jpg', upload_to=b'brand_images/')),
                ('array_image_file', models.FileField(storage=django.core.files.storage.FileSystemStorage(base_url=b'', location=b'/home/max/django/EcoAR_SEARCH_LOGO/'), upload_to=b'array_image_files/')),
                ('keypoints_file', models.FileField(storage=django.core.files.storage.FileSystemStorage(base_url=b'', location=b'/home/max/django/EcoAR_SEARCH_LOGO/'), upload_to=b'keypoints_files/')),
                ('descriptors_file', models.FileField(storage=django.core.files.storage.FileSystemStorage(base_url=b'', location=b'/home/max/django/EcoAR_SEARCH_LOGO/'), upload_to=b'descriptors_files/')),
                ('brand', models.ForeignKey(related_name=b'logo', to='search_camera.Brand', null=True)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
    ]
