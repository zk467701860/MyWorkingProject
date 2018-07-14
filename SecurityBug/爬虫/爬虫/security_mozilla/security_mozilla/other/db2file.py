import os
import random
import MySQLdb

try:
    conn=MySQLdb.connect(host='localhost',user='root',passwd='123456',port=3306)
    cur=conn.cursor()
    conn.select_db('mozilla')
    count=cur.execute('select id,summry from other_bug order by id')
    results=cur.fetchall()
    os.makedirs("test\\other")
    os.makedirs("train\\other")
    for r in results:
        a = random.randint(1, 5)
        if a == 1:
            f=open('test\\other\\'+str(r[0])+'.txt','w')
        else:
            f=open('train\\other\\'+str(r[0])+'.txt','w')
        f.write(r[1])
        f.close()
    cur.close()
    conn.close()

except MySQLdb.Error,e:
    print "Mysql Error %d: %s" % (e.args[0], e.args[1])