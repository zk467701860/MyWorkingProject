#coding:utf-8
import os
import MySQLdb

def get_zipaddress_mysql():
    result = cur.execute("select zip_address from gitrelease")
    print 'gitrelease中有 ' + str(result) + ' 条记录'
    #打印表中的多少数据
    lists = cur.fetchall()
    i = 1
    for list in lists:
        if not os.path.exists(list[0]+'.zip'):
            print list[0]
            print i
            try:
                cur.execute("delete from gitrelease where git_release_id = %s",(i))
                conn.commit()
            except Exception,e:
                print Exception,":",e
        i += 1

conn= MySQLdb.connect(
        host='localhost',
        port = 3306,
        user='root',
        passwd='root',
        db ='fdroid',
        charset='utf8'
        )
cur = conn.cursor()

if __name__ == '__main__':
    get_zipaddress_mysql()
