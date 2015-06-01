# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
from django.conf import settings


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
        ('products', '0005_auto_20150530_1650'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='commentaries',
            name='username',
        ),
        migrations.AddField(
            model_name='commentaries',
            name='user',
            field=models.ForeignKey(related_name=b'Commentaries', to=settings.AUTH_USER_MODEL, null=True),
            preserve_default=True,
        ),
        migrations.AlterField(
            model_name='commentaries',
            name='product',
            field=models.ForeignKey(related_name=b'Commentaries', to='products.Product', null=True),
        ),
    ]
