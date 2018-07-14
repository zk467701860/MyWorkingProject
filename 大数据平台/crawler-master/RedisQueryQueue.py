import MySQLdb
from redis import Redis

import sys
reload(sys)
sys.setdefaultencoding('utf-8')



#read github name from mysql and push into redis
r = Redis(host='10.131.252.156',port=6379)
conn = MySQLdb.connect(host='localhost',user='root',passwd='root',port=3306)
cur=conn.cursor()
conn.select_db('fdroid')
cur.execute("select id,name,sourcecode from collections")
results = cur.fetchall()
for row in results:
    r.lpush('ids',row[0])
    r.lpush('sourcecodes',row[2])
cur.close()
conn.commit()
conn.close()












