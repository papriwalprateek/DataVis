from django.db import models

class node(models.Model):
    key = models.IntegerField(primary_key=True)
    place = models.CharField(max_length=20)
    clid = models.IntegerField(default = 0) 

class edges(models.Model):
    source = models.ForeignKey('node', related_name = 'fromsource')
    target = models.ForeignKey('node', related_name = 'fromtarget')

class comvol(models.Model):
    topic = models.CharField(max_length=20)
    day = models.DateField()
    vol = models.IntegerField()
    no_of_users = models.IntegerField()
    class Meta:
         ordering = ['day']
