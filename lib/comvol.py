from datetime import date
from dashboard.models import comvol
fw = open("test.out", "rw+")
conv = ["Jan", "Feb", "Mar", "Apr", "May", "June", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"]
topic_day_list = []
vol_list = []
users_list = []
def readfile():
  f = open("log-comm.01.out","rb")
  line = f.readline()
  while line:
       split_line = line.strip().split(", ")
       topic = split_line.pop()
       edge = split_line.pop()
       split_split_line = split_line.pop().split(" ")
       year = int(split_split_line.pop())
       split_split_line.pop()
       split_split_line.pop()
       date_num = int(split_split_line.pop())
       month = conv.index(split_split_line.pop()) + 1
       day = date(year, month, date_num)
       users = edge.split("-")
       users[0] = int(users[0])
       users[1] = int(users[1])
#       fw.write(topic + "\n") 
       updatelists(topic, day, users)
       line = f.readline()
  f.close()


def updatelists(topic, day, users):
    index = -1
    topic_day = [topic, day]
    for i in range(len(topic_day_list)):
        if topic_day_list[i] == topic_day:
           index = i
           break
    if index < 0 :
       topic_day_list.append(topic_day)
       vol_list.append(1)
       users_list.append(users)
    else:
       vol_list[index] = vol_list[index] + 1
       temp_list = []
       temp_list = users_list[index]
       temp_list.extend(users)
       users_list[index] = list(set(temp_list))   

      
readfile()

for i in range(len(topic_day_list)):
   entry_topic = topic_day_list[i][0]
   entry_date = topic_day_list[i][1]
   entry_vol = vol_list[i]
   entry_users = len(users_list[i])
   fw.write(entry_topic + "\n")
   
   comvol.objects.create(topic = entry_topic, day = entry_date, vol = entry_vol, no_of_users = entry_users)
fw.close()
   
