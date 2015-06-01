# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('products', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='UsersScore',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('one', models.BigIntegerField(default=b'0')),
                ('two', models.BigIntegerField(default=b'0')),
                ('tree', models.BigIntegerField(default=b'0')),
                ('four', models.BigIntegerField(default=b'0')),
                ('five', models.BigIntegerField(default=b'0')),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.RenameField(
            model_name='commentaries',
            old_name='postingDate',
            new_name='posting_date',
        ),
        migrations.RenameField(
            model_name='product',
            old_name='generalId',
            new_name='general_id',
        ),
        migrations.RemoveField(
            model_name='product',
            name='ecologicalScore',
        ),
        migrations.RemoveField(
            model_name='product',
            name='usersScore',
        ),
        migrations.AddField(
            model_name='product',
            name='ecological_score',
            field=models.FloatField(default=b'0.0'),
            preserve_default=True,
        ),
        migrations.AddField(
            model_name='product',
            name='users_score',
            field=models.FloatField(default=b'0.0'),
            preserve_default=True,
        ),
        migrations.AddField(
            model_name='product',
            name='users_score_model',
            field=models.ForeignKey(related_name=b'+', to='products.UsersScore', null=True),
            preserve_default=True,
        ),
        migrations.AlterField(
            model_name='product',
            name='image',
            field=models.ImageField(default=b'product_images//None/no-img.jpg', null=True, upload_to=b'product_images/', blank=True),
        ),
    ]
