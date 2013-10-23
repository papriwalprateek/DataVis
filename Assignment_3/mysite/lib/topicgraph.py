from dashboard.models import comvol
import datetime

f = open("/home/papri/mysite/mysite/static/data2.tsv","w")
f.close()

fw = open("/home/papri/mysite/mysite/static/data2.tsv","rw+")

fw.write("date"+"\t"+"volume"+ "\t" + "no of users" + "\n")

date = datetime.date(2012,10,12)

for i in range(25):
	p = comvol.objects.filter(topic = "jimmy zuma", day = date)

	fw.write(str(date.year) + str(date.month) + str(date.day))
	
	if len(p) == 0:
		fw.write("\t" + "0" + "\t" + "0" + "\n")
	else:
		fw.write("\t" + str(p[0].vol) + "\t" + str(p[0].no_of_users) + "\n")
	
	x = datetime.timedelta(1)
	date = date + x	
fw.close()
