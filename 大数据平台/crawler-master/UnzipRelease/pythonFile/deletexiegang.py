#coding:utf-8
import zipfile
import os
import MySQLdb

def delete():
    result = cur.execute("select git_release_id,src_address from gitrelease")
    print 'gitrelease中有 ' + str(result) + ' 条记录'
    #打印表中的多少数据
    lists = cur.fetchall()
    i = 1
    for list in lists:
        srcname = list[1].encode('utf-8')
        try:
            cur.execute("update gitrelease set src_address = %s where git_release_id = %s",(srcname.replace("//","/"),list[0]))
            print str(i) + '    ' + srcname.replace("//","/")
            conn.commit()
        except Exception,e:
            print Exception,":",e
        i += 1

conn= MySQLdb.connect(
        host='localhost',
        port = 3306,
        user='root',
        passwd='1234',
        db ='fdroid',
        charset='utf8'
        )
cur = conn.cursor()

if __name__ == '__main__':
    delete()
