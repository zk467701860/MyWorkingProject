import MySQLdb
import re

try:
    conn=MySQLdb.connect(host='localhost',user='root',passwd='123456',port=3306)
    cur=conn.cursor()
    conn.select_db('mozilla')
    count=cur.execute("select id,history from bugs")
    results=cur.fetchall()
    for result in results:
        id = result[0]
        history = result[1]
        try:
            newTime = re.search("([0-9\-\s:]+),[A-Z\-]+,NEW",history).group(1)
            #assignedTime = re.search("([0-9\-\s:]+),[A-Z\-]+,ASSIGNED",history).group(1)
            cur.execute("update bugs set new_time = %s where id = %s", (newTime,id ))
        except AttributeError,e:
            conn.commit()
            print id
    conn.commit()
    cur.close()
    conn.close()

except MySQLdb.Error,e:
    print "Mysql Error %d: %s" % (e.args[0], e.args[1])
