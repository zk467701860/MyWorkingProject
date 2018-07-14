from git import *
import MySQLdb
import re
import time
import sys
reload(sys)
sys.setdefaultencoding('utf-8')


try:
    conn=MySQLdb.connect(host='localhost',user='root',passwd='123456',port=3306)
    cur=conn.cursor()
    conn.select_db('mozilla')
    count=cur.execute('SELECT count(*) FROM mozilla.securityBug_edit where inc_num <=100')
    results=cur.fetchall()
    print results

except MySQLdb.Error,e:
    print "Mysql Error %d: %s" % (e.args[0], e.args[1])
