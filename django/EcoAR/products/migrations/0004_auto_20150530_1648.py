# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('products', '0003_auto_20150530_1555'),
    ]

    operations = [
        migrations.RenameField(
            model_name='usersscore',
            old_name='tree',
            new_name='three',
        ),
    ]
