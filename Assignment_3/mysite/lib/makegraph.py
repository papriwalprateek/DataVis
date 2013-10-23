from dashboard.models import comvol
import datetime
from findtopics import poptopics

f = open("/home/papri/mysite/mysite/static/data1.tsv","w")
f.close()

fw = open("/home/papri/mysite/mysite/static/data1.tsv","rw+")

fw.write("date"+"\t"+poptopics[0]+ "\t" + poptopics[1] + "\t" + poptopics[2] + "\t" + poptopics[3]+"\t" +poptopics[4] + "\n")

date = datetime.date(2012,10,12)


for i in range(220):
	p = comvol.objects.filter(topic = poptopics[0], day = date)
	p1 = comvol.objects.filter(topic = poptopics[1], day = date)
	p2 = comvol.objects.filter(topic = poptopics[2], day = date)
	p3 = comvol.objects.filter(topic = poptopics[3], day = date)
	p4 = comvol.objects.filter(topic = poptopics[4], day = date)
        if date.month < 10:
               fw.write(str(date.year) + "0" + str(date.month) + str(date.day))
        else: 
	       fw.write(str(date.year) + str(date.month) + str(date.day))



	if len(p) == 0:
		fw.write("\t" + "0")
	else:
		fw.write("\t" + str(p[0].vol))
	
	if len(p1) == 0:
		fw.write("\t" + "0")
	else:
		fw.write("\t" + str(p1[0].vol))
	if len(p2) == 0:
		fw.write("\t" + "0")
	else:
		fw.write("\t" + str(p2[0].vol))
	if len(p3) == 0:
		fw.write("\t" + "0")
	else:
		fw.write("\t" + str(p3[0].vol))
	if len(p4) == 0:
		fw.write("\t" + "0" + "\n")
	else:
		fw.write("\t" + str(p4[0].vol) + "\n")
	x = datetime.timedelta(1)
	date = date + x	
fw.close()
