# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
from django.conf import settings


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
        ('products', '0006_auto_20150530_1912'),
    ]

    operations = [
        migrations.CreateModel(
            name='Comment',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('body', models.CharField(max_length=1000)),
                ('posting_date', models.DateTimeField()),
                ('product', models.ForeignKey(related_name=b'commentaries', to='products.Product', null=True)),
                ('user', models.ForeignKey(related_name=b'commentaries', to=settings.AUTH_USER_MODEL, null=True)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.RemoveField(
            model_name='commentaries',
            name='product',
        ),
        migrations.RemoveField(
            model_name='commentaries',
            name='user',
        ),
        migrations.DeleteModel(
            name='Commentaries',
        ),
        migrations.AlterField(
            model_name='product',
            name='general_id',
            field=models.CharField(unique=True, max_length=20),
        ),
        migrations.AlterField(
            model_name='product',
            name='image',
            field=models.ImageField(default=b'product_images/None/no-img.jpg', null=True, upload_to=b'product_images/', blank=True),
        ),
    ]
