from dashboard.models import comvol
import datetime

f = open("F:/mysite/mysite/static/data3.tsv","w")
f.close()  # used for deleting existing data

fw = open("F:/mysite/mysite/static/data3.tsv","w") # used for modifying data

fw.write("date"+"\t"+"volume"+ "\t" + "no of users" + "\n") # writing data2.tsv file

date = datetime.date(2012,10,12)  # date format

for i in range(25):
	p = comvol.objects.filter(topic = "jimmy zuma", day = date)

	fw.write(str(date.year) + str(date.month) + str(date.day)) # date write into file
	
	if len(p) == 0:
		fw.write("\t" + "0" + "\t" + "0" + "\n")
	else:
		fw.write("\t" + str(p[0].vol) + "\t" + str(p[0].no_of_users) + "\n")
	
	x = datetime.timedelta(1)  
	date = date + x	 # go to next date
fw.close()
