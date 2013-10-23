import datetime
from dashboard.models import comvol

p = comvol.objects.all()

list_of_topics = []
vol_of_topics = []


for i in range(len(p)):
	list_of_topics.append(str(p[i].topic))

list_of_topics = list(set(list_of_topics))

for i in range(len(list_of_topics)):
	vol_of_topics.append(0)	

for i in range(len(list_of_topics)):
	x = comvol.objects.filter(topic = list_of_topics[i])
	for j in range(len(x)):
		vol_of_topics[i] = vol_of_topics[i] + x[j].vol

poptopics = []

for i in range(5):
	y = vol_of_topics.index(max(vol_of_topics))
	poptopics.append(list_of_topics[y])
	list_of_topics.pop(y)
	vol_of_topics.pop(y)

toptopic = poptopics[0]
