from django.http import HttpResponse
import datetime

from dashboard.models import comvol
from django.template import Template, Context, loader
from django.shortcuts import render_to_response,render
from findtopics import findpoptopics
from makegraph import makechart, time_format

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

def popgraph(request):
    if 'q' in request.GET:
        message = 'You searched for: %r' % request.GET['q']
    else:
        message = 'You submitted an empty form.'
    timefrom = str(request.GET['q'])
    timeto = str(request.GET['q1'])
    makechart(timefrom,timeto)
    argument = time_format(timefrom,timeto)
    poptopics_views = findpoptopics(argument[0],argument[1])

#    import makegraph
    context = { 'poptopics1' : poptopics_views[0],'poptopics2' : poptopics_views[1],'poptopics3' : poptopics_views[2], 'poptopics4' : poptopics_views[3],'poptopics5' : poptopics_views[4] }
    return render(request,'popchart.html',context)
#    return render_to_response("popchart.html")

def topictrends(request):
	import topicgraph
	return render_to_response("topic_trends.html")

def rootview(request):
    return HttpResponse("It works.")
def new(request):
    return render_to_response("time.html")
