from django.conf.urls import patterns, include, url

from mysite.views import *

urlpatterns = patterns('',
    (r'^popgraph$', popgraph),
    (r'^topictrends$', topictrends),
    (r'^topictrends1$', topictrends1),
    (r'^topictrends2$', topictrends2),
    (r'^topictrends3$', topictrends3),
    (r'^topictrends4$', topictrends4),
    (r'^timefromto$', time_from_to),
    (r'^trendingtopicslist$', trendingtopicslist),
    (r'^timefromtoagr$', time_from_to1),
    (r'^locationratio$', locationratio),
    (r'^trendingtopicslist$', trendingtopicslist),
    (r'^$', rootview),
    (r'^time/plus/(\d{1,2})/(\d{1,2})$', hours_ahead),
    (r'^userbase$', userbase),
    (r'^bubbleview$', bubbleview),
    (r'^barview$', barview),
    (r'^treemap$', treemap),
    (r'^aggrcomm$', aggrcomm),
)
