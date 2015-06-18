# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('products', '0009_auto_20150610_2210'),
    ]

    operations = [
        migrations.CreateModel(
            name='ProductTag',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('word', models.CharField(max_length=23, null=True)),
                ('products', models.ManyToManyField(related_name=b'tags', to='products.Product')),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.AlterField(
            model_name='usersscore',
            name='product',
            field=models.OneToOneField(related_name=b'users_score_model', null=True, to='products.Product'),
        ),
    ]
