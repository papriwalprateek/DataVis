from dashboard.models import comvol

def findTrendingTopics(date):
    n=5  #no of trending topics
    p=comvol.objects.filter(day = date)
    x=datetime.timedelta(-1)
    date=date+x
    p0=comvol.objects.filter(day = date)
    vol_change=[]
    for i in range(len(p)):
        flag=false
        for j in range (len(p0)):
            if p[i].topic == p0[j].topic:
                vol_change.append(p[i].vol-p0[j].vol)
                flag=true
                break
        if flag==true:
            vol_change.append(p[i])
    trending_topics=[]
    for i in range(n):
        y=vol_change.index(max(vol_change))
        trending_topics.append(str(p[y].topic))
        p.pop(y)
        vol_change.pop(y)

return trending_topics
    
