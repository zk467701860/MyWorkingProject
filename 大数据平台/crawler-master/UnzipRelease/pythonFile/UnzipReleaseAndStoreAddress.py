#coding:utf-8
import zipfile
import os
import MySQLdb

def unZip():
    result = cur.execute("select git_release_id,zip_address from gitrelease")
    print 'gitrelease中有 ' + str(result) + ' 条记录'
    #打印表中的多少数据
    lists = cur.fetchall()
    i = 1
    for list in lists:
        if i > 2183:
            zipname = list[1] + '.zip'
            zipname = zipname.encode('utf-8')
            print str(i) + '    ' + zipname
            try:
                file_zip = zipfile.ZipFile(zipname, 'r')
                j = 1
                parzip = zipname[0:zipname.find(zipname.split('/')[-1])][:-1]
                for file in file_zip.namelist():
                    if j == 1:
                        srcname = zipname[0:zipname.find(zipname.split('/')[-1])] + '/' + file
                        cur.execute("update gitrelease set src_address = %s where git_release_id = %s",(srcname,list[0]))
                        conn.commit()
                    file_zip.extract(file, parzip)
                    j += 1
                file_zip.close()
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
    unZip()
