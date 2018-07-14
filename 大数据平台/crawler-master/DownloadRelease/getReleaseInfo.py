#coding:utf-8
#完成通用爬虫，抓取一个页面队列中所有图片

import requests
import MySQLdb
import chardet
from datetime import datetime

headers = {"Authorization": "token c5923ba617da3f6f68cced74f6e4c0b362c09dc4"}
def get_appsite_mysql():
    #获得表中有多少条数据
    result = cur.execute("select * from collections")
    print 'collections中有 ' + str(result) + ' 条记录'
    #打印表中的多少数据
    lists = cur.fetchall()
    link = ''
    releaselink = ''
    name = ''
    i = 0
    for list in lists:      
        name = list[0].encode('utf-8')
        print 'start: ' + str(i) + ' ,name(utf8): '+ name
        link = list[7]
        if link == '':
            i += 1
            continue
        if link.find('github') != -1:
            if i > 1031:
                get_data(name,link)
        i += 1
    
def get_data(name,url):
    #https://api.github.com/repos/LonamiWebs/Klooni1010/releases
    releaseAPI = 'https://api.github.com/repos/' + url[url.index('hub.com/')+8:] + '/releases'
    tagAPI = 'https://api.github.com/repos/' + url[url.index('hub.com/')+8:] + '/tags'
    releaseResponse = requests.get(releaseAPI, headers=headers)
    releaseJson = releaseResponse.json()
    tagResponse = requests.get(tagAPI, headers=headers)
    tagJson = tagResponse.json()
    tag = ''
    created_at = ''
    content = ''
    commitid = ''
    address = ''
    appname = name.translate(None, "|\\?*<\":>+[]/'")
    properTag = 0
    if 'message' in releaseJson:
        return -1
    if len(releaseJson) == 0:
        return -2
    for i in range(len(releaseJson)):
        tag = releaseJson[i]['tag_name']
        for j in range(len(tagJson)):
            if tag == tagJson[j]['name']:
                properTag = j
                break
        created_at = releaseJson[i]['created_at'].replace('T',' ')
        created_at = created_at.replace('Z','')
        created_at = datetime.strptime(created_at,"%Y-%m-%d %H:%M:%S")
        if releaseJson[i]['body'] == None:
            content = ''
        else:
            content = releaseJson[i]['body'].encode('utf-8')
        address = '/home/fdse/Downloads/AndroidRelease/' + appname + '/' + releaseJson[i]['zipball_url'].split('/')[-1].encode('utf-8')
        commitid = tagJson[properTag]['commit']['sha']
        #print 'tag ' + tag
        #print 'created_at ' + created_at
        #print 'content ' + content
        #print 'address ' + address
        #print 'commitid ' + commitid
        #content = content.replace("\\","\\\\")
        #content = content.replace("\'","\\\'")
        #sql = "insert into gitrelease(content) values('" + content + "')"
        #vsql = unicode(sql, "utf-8")
        #cur.execute(sql)
        try:
            cur.execute("insert into gitrelease(tag,created_at,content,release_commit_id,address) values(%s,%s,%s,%s,%s)",(tag,created_at,content,commitid,address))
            conn.commit()
        except Exception,e:
            print Exception,":",e
            print address
            content = 'wrong unicode character, please scan website'
            cur.execute("insert into gitrelease(tag,created_at,content,release_commit_id,address) values(%s,%s,%s,%s,%s)",(tag,created_at,content,commitid,address))
            conn.commit()
    print 'end   ' + str(len(releaseJson))

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
    #get_data('Расписание МГКЭ','https://github.com/queler/holokenmod')
    get_appsite_mysql()
