# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Runner',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('runner_id', models.IntegerField()),
                ('donation_received', models.BooleanField()),
                ('location_x', models.FloatField()),
                ('location_y', models.FloatField()),
            ],
        ),
    ]
