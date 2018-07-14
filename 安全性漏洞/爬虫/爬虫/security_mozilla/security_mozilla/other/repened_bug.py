import re
import MySQLdb

try:
    conn=MySQLdb.connect(host='localhost',user='root',passwd='123456',port=3306)
    cur=conn.cursor()
    conn.select_db('mozilla')
    fp=open("./buglist.cgi", "r")
    fp.readline()
    while 1:
        line = fp.readline()
        if not line:
            break
        bug = re.split(r'\"*,\"',line)
        cur.execute("insert into reopened_bug(id, summary, assigned_to, last_change_time) values(%s, %s, %s, %s)",
                    (bug[0], bug[6], bug[3], re.sub(r'\"\n*','',bug[7])))
    conn.commit()
    fp.close()
    cur.close()
    conn.close()

except MySQLdb.Error,e:
    print "Mysql Error %d: %s" % (e.args[0], e.args[1])