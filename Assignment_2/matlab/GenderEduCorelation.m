x = [ 16 16 16 16 20 16 16 16 5 17 16 20 20 16 16 20 16 10 12 16 20 20 20 16 20 16 20 16 20 20 16 16 12 20 16 12 20 20 20 5 12 16 5 20 10 16 16 12 20 5 20 16 12 5 20 16 12 16 16 16 20 12 20 16 16 20 20 20 16 20 20 16 20 0 16 20 20 16 16 20 20 16 16 20 16 20 16 12 16 16 16 20 20 16 10 10 20 20 20 20 10 20 13 0 20 10 16 16 20 16 12 13 0 16 16 16 20 16 16 5 16 20 16 20 20 16 10 20 16 10 20 20 13 16 16 12 5 16 16 16 20 16 16 16 12 16 20 16 16 20 12 5 0 16 20 16 16 10 16 12 20 20 16 20 12 20 16 16 20 20 12 20 20 16 16 16 0 16 16 12 16 20 20 16 5 10 16 16 16 16 16 10 5 12 20 16 20 20 0 12 16 20 16 16 20 16 16 12 16 12 16 16 20 16 12 16 16 20 16 10 16 16 20 20 20 16 16 16 16 12 20 16 12 16 12 5 20 16 16 20 16 5 16 20 20 16 10 16 12 20 20 16 16 16 16 16 16 20 16 20 20 20 16 20 12 20 13 16 20 16 10 12 20 20 16 5 12 16 5 20 16 16 16 20 16 20 20 20 16 12 16 16 12 20 20 16 16 20 16 16 20 16 20 0 16 16 5 20 16 16 16 16 20 12 16 20 16 10 20 16 16 20 20 20 13 16 16 12 16 16 10 16 16 16 16 16 20 16 20 16 16 16 16 20 16 20 16 16 20 13 10 20 16 16 16 10 20 10 16 16 12 20 10 16 16 20 0 20 0 16 16 16 16 12 16 16 16 20 12 5 16 16 16 16 20 16 20 16 0 20 20 20 16 10 20 16 16 20 16 16 16 20 12 16 20 12 20 16 20 16 16 20 20 16 10 10 10 12 16 10 20 16 12 20 12 16 10 20 20 16 5 16 16 16 10 16 17 20 16 16 16 10 16 16 16 16 16 16 16 16 20 16 13 20 16 16 20 16 16 16 20 20 20 16 16 16 16 12 12 20 20 16 16 16 16 16 20 16 20 20 20 12 20 16 16 16 16 16 20 16 16];
y= [ 20 16 5 16 20 0 16 16 20 20 16 20 16 16 12 10 20 20 16 5 20 0 16 20 20 10 20 12 16 16 20 20 16 20 12 20 16 20 0 12 16 16 16 20 16 20 20 20 16 16 20 16 13 20 16 20 16 16 10 20 12];
[h,k]=ttest2(x,y,0.05,'left','unequal')