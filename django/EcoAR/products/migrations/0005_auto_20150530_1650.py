# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('products', '0004_auto_20150530_1648'),
    ]

    operations = [
        migrations.AlterField(
            model_name='usersscore',
            name='five',
            field=models.BigIntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='usersscore',
            name='four',
            field=models.BigIntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='usersscore',
            name='one',
            field=models.BigIntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='usersscore',
            name='three',
            field=models.BigIntegerField(default=0),
        ),
        migrations.AlterField(
            model_name='usersscore',
            name='two',
            field=models.BigIntegerField(default=0),
        ),
    ]
