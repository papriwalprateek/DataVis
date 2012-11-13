from dashboard.models import node
f = open("log-graph.out", "rb")
while True:
	x = f.readline()
	if not x: break
	x = x.split(" ")
	if x[1] == 'node':
		node.objects.create(key = int(x[2].split(",")[0]), place = x[3].split("\r\n")[0])
                print(x[3].split("\r\n")[0]+"\n")

#	if x[1] == 'edge':
#		a = x[2].split("-")
#		fw.write("<edge source = "+"\"" + a[0] + "\"" + " target = " + "\"" + a[1].split("\r\n")[0] + "\"" + "/>" + "\n")	

f.close()
