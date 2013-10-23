from dashboard.models import node, edges
f = open("log-graph.out", "rb")
while True:
	x = f.readline()
	if not x: break
	x = x.split(" ")
	if x[1] == 'edge':
		a = x[2].split("-")
                source = a[0];
                target = a[1].split("\r\n")[0]
	        s_node = node.objects.filter(key = int(source))
                t_node = node.objects.filter(key = int(target))
                edges.objects.create(source = s_node[0], target = t_node[0])
                print(s_node[0].place + " " + t_node[0].place + "\n")

f.close()

