import sched, time, urllib2

s = sched.scheduler(time.time, time.sleep)

def download_file(file_name,output_file):
	u = urllib2.urlopen(file_name)
	localFile = open(output_file,'w')
	localFile.write(u.read())
	localFile.close()

def auto_downloader(sc,count): 

	if count<=9:
		output_file = 'data_files/log-comm'+'.0'+str(count)+'.out'
		file_name = 'http://10.22.4.33/csp301/log-comm'+'.0'+str(count)+'.out'
	else:
		output_file = 'data_files/log-comm.'+str(count)+'.out'
		file_name = 'http://10.22.4.33/csp301/log-comm.'+str(count)+'.out'

	print file_name
	download_file(file_name, output_file);
	count = count+1
	sc.enter(60, 1, auto_downloader, (sc,count))


s.enter(1, 1, auto_downloader, (s,0))
s.run()		
