from django.conf.urls import patterns, include, url

from mysite.views import *

urlpatterns = patterns('',
    (r'^popgraph$', popgraph),
    (r'^topictrends$', topictrends),
    (r'^$', rootview),
    (r'^time/plus/(\d{1,2})/(\d{1,2})$', hours_ahead),

)
