from dashboard.models import comvol
import datetime
from findtopics import findpoptopics

def time_format(timefrom,timeto):

	year_from = int(timefrom.split('-')[0])
	month_from = int(timefrom.split('-')[1])
	day_from  = int(timefrom.split('-')[2])
	year_to = int(timeto.split('-')[0])
	month_to = int(timeto.split('-')[1])
	day_to = int(timeto.split('-')[2])
	date = datetime.date(year_from,month_from,day_from)
	date_to = datetime.date(year_to,month_to,day_to)
	no_of_days = date_to - date
        return [date,no_of_days]

   

def makechart(timefrom,timeto):

	f = open("F:/mysite/mysite/static/data1.tsv","w")
	f.close()

	year_from = int(timefrom.split('-')[0])
	month_from = int(timefrom.split('-')[1])
	day_from  = int(timefrom.split('-')[2])
	year_to = int(timeto.split('-')[0])
	month_to = int(timeto.split('-')[1])
	day_to = int(timeto.split('-')[2])
	date = datetime.date(year_from,month_from,day_from)
	date_to = datetime.date(year_to,month_to,day_to)
	no_of_days = date_to - date

	fw = open("F:/mysite/mysite/static/data1.tsv","w")

	poptopics_make = findpoptopics(date,no_of_days)

        fw.write("date"+"\t"+poptopics_make[0]+ "\t" + poptopics_make[1] + "\t" + poptopics_make[2] + "\t" + poptopics_make[3]+"\t" +poptopics_make[4] + "\n")

	

	
	for i in range(no_of_days.days):
		p = comvol.objects.filter(topic = poptopics_make[0], day = date)
		p1 = comvol.objects.filter(topic = poptopics_make[1], day = date)
		p2 = comvol.objects.filter(topic = poptopics_make[2], day = date)
		p3 = comvol.objects.filter(topic = poptopics_make[3], day = date)
		p4 = comvol.objects.filter(topic = poptopics_make[4], day = date)
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
