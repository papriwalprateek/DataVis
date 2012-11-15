from dashboard.models import node
f = open("cluster.out","r")
line = f.readline()

while line:
 line_list = line.split(",")
 cluster_id = int(line_list.pop())
 print(str(cluster_id) + "\n")
 for i in range(len(line_list)):
    p = node.objects.get(key = int(line_list[i]))
    p.clid = cluster_id
    p.save()
 line = f.readline()
f.close()
