#coding:utf-8
import MySQLdb

def add_name():
    result = cur.execute("select api_class_id,name from api_class")
    print 'api_class中有 ' + str(result) + ' 条记录'
    #打印表中的多少数据
    lists = cur.fetchall()
    index = 1
    for list in lists:
        id = list[0]
        name = list[1]
        className = name.split('.')[-1]
        print str(index) + '    ' + className
        try:
            cur.execute("update api_class set class_name = %s where api_class_id = %s",(className,id))
            conn.commit()
        except Exception,e:
            cur.close()
            conn.commit()
            conn.close()
            print "mysql出错"
            print Exception,":",e
        index += 1

conn= MySQLdb.connect(
        host='10.131.252.156',
        port = 3306,
        user='root',
        passwd='root',
        db ='fdroid',
        charset='utf8'
        )
cur = conn.cursor()


if __name__ == '__main__':
    add_name()
