from django.http import HttpResponse
from django.template import Template, Context, loader
from django.shortcuts import render_to_response,render

import datetime
from dashboard.models import comvol

from findtopics import findpoptopics
from makegraph import makechart, time_format
from topicgraph import maketopicgraph
from agg_com import aggregatecomm
from userbase import location_pie_chart,bubble_chart,bar_chart,tree_map
from findtrendingtopics import trending_topics


convert = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'June', 'Jul', 'Aug', 'Sept', 'Oct', 'Nov', 'Dec']
#timefrom = []


def hours_ahead(request, offset, offset1):
    try:
        offset = int(offset)
        offset1 = int(offset1)
    except ValueError:
        raise Http404()
    dt = datetime.datetime.now() + datetime.timedelta(hours=offset)
    dt1 = datetime.datetime.now() + datetime.timedelta(hours=offset)

    html = "<html><body>In %s hour(s), it will be %s. In %s hour(s), it will be %s.</body></html>" % (offset, dt, offset1, dt1)
    return HttpResponse(html)

def time_from_to(request):
	return render_to_response("time.html")

def time_from_to1(request):
	return render_to_response("time1.html")

def barview(request):
	bar_chart();
	return render_to_response("barchart.html");

def treemap(request):
	tree_map("treemap.js");
	return render_to_response("treemap.html");

def userbase(request):
	location_pie_chart();
	return render_to_response("userbase.html");

def bubbleview(request):
	tree_map("bubble.js");
	return render_to_response("bubble.html");

def pie_time(request):
	location_pie_chart();
	return render_to_response("pie_time.html");



def popgraph(request):
    timefrom = str(request.GET['q'])
    makechart(timefrom)
    maketopicgraph(timefrom)
    argument = time_format(timefrom)
    poptopics_views = findpoptopics(argument[0],argument[1])
    context = { 'poptopics1' : poptopics_views[0],'poptopics2' : poptopics_views[1],'poptopics3' : poptopics_views[2], 'poptopics4' : poptopics_views[3],'poptopics5' : poptopics_views[4] }
    return render(request,'popchart.html',context)

def aggrcomm(request):
    timefrom = str(request.GET['q'])
    aggregatecomm(timefrom)
    return render_to_response("aggr_com.html")	

def topictrends(request):
	import topicgraph
	return render_to_response("topic_trends.html")

def topictrends1(request):
	import topicgraph
	return render_to_response("topic_trends1.html")

def topictrends2(request):
	import topicgraph
	return render_to_response("topic_trends2.html")

def topictrends3(request):
	import topicgraph
	return render_to_response("topic_trends3.html")

def topictrends4(request):
	import topicgraph
	return render_to_response("topic_trends4.html")

def locationratio(request):
	return render_to_response("locratio.html")

def trendingtopicslist(request):
	import findtrendingtopics
        context = { 'poptopics1' : trending_topics[0],'poptopics2' : trending_topics[1],'poptopics3' : trending_topics[2], 'poptopics4' : trending_topics[3],'poptopics5' : trending_topics[4] }
        return render(request,'x.html',context)


def rootview(request):
    return render_to_response("index.html")
