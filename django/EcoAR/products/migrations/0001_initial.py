# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
from django.conf import settings


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='Commentaries',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('body', models.CharField(max_length=1000)),
                ('postingDate', models.DateTimeField()),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='Product',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('name', models.CharField(max_length=50, null=True)),
                ('description', models.CharField(max_length=500, null=True)),
                ('image', models.ImageField(default=b'pic_folder/None/no-img.jpg', null=True, upload_to=b'pic_folder/')),
                ('ecologicalScore', models.FloatField()),
                ('usersScore', models.FloatField()),
                ('generalId', models.CharField(max_length=20)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.AddField(
            model_name='commentaries',
            name='product',
            field=models.ForeignKey(related_name=b'commentaries', to='products.Product'),
            preserve_default=True,
        ),
        migrations.AddField(
            model_name='commentaries',
            name='username',
            field=models.ForeignKey(related_name=b'commentaries', to=settings.AUTH_USER_MODEL),
            preserve_default=True,
        ),
    ]
