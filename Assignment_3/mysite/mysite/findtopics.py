import datetime
from dashboard.models import comvol

def findpoptopics(date, no_of_days):

 list_of_topics = []   
 vol_of_topics = []
 data_list = []
 for i in range(no_of_days.days):
	p = comvol.objects.filter(day = date)

	for j in range(len(p)):
		list_of_topics.append(str(p[j].topic))
                data_list.append(p[j]) 
	x = datetime.timedelta(1)
	date = date + x


 list_of_topics = list(set(list_of_topics))   # list containing unique for given no of days
 print list_of_topics

 for i in range(len(list_of_topics)):
	vol_of_topics.append(0)	           # volume list length made equal to list_of _topics and initialised with 0
 

 for i in range(len(list_of_topics)):
	for j in range(len(data_list)):
                if data_list[j].topic == list_of_topics[i] : 
		   vol_of_topics[i] = vol_of_topics[i] + data_list[j].vol  # storing sum of volume for each topic
 print vol_of_topics
 poptopics = []

 for i in range(5):
	y = vol_of_topics.index(max(vol_of_topics))   # finding out max volume index
        print(str(vol_of_topics[y]) + list_of_topics[y]+ "\n")
	poptopics.append(list_of_topics[y])
	list_of_topics.pop(y)
	vol_of_topics.pop(y)

 return poptopics
