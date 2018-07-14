#coding:utf-8
#完成通用爬虫，抓取一个页面队列中所有图片

import time
import os
import urllib
from redis import Redis
import MySQLdb

def reporthook(blocks_read,block_size,total_size):  
    percent = (100.0 * blocks_read * block_size) / total_size
    if percent > 100:
        percent = 100
    print '%.2f%%' % percent
        
def download(url,appname):
    try:
        appname = appname.translate(None, "|\\?*<\":>+[]/'")
        if os.path.exists(os.path.join(os.path.abspath('.'),appname)) == False:
            os.mkdir('./' + appname)
        filename = url.split('/')[-1]
        urllib.urlretrieve(url,'./' + appname + '/' + filename,reporthook)
    except Exception,e:
        print Exception,":",e
        return -1

def get_releasesite_mysql():
    r = Redis()
    if r.llen('appname') == 0:
        print r.keys('*')
        #获得表中有多少条数据
        result = cur.execute("select * from releaseurl")
        print 'releaseurl中有 ' + str(result) + ' 条记录'
        #打印表中的多少数据
        lists = cur.fetchall()
        for list in lists:
            r.lpush('appname',list[1].encode("utf-8"))
            r.lpush('sourcecodelink',list[2])
        print 'redis中appname有 ' + str(r.llen('appname')) + ' 条记录'
        print 'redis中sourcecodelink有 ' + str(r.llen('sourcecodelink')) + ' 条记录'

def get_app_introduction():
    r = Redis()
    print r.keys('*')
    downloadIndex = 0
    while(r.llen('appname') != 0):
        try:
            name = r.lpop('appname')
            url = r.lpop('sourcecodelink')
            #name = r.lindex('appname',0)
            #url = r.lindex('sourcecodelink',0)
            print 'Begin Download ' + str(downloadIndex) + '   ' + url
            return_1 = download(url,name)
            #conn.commit()
            if return_1 == -1:
                r.lpush('wrongname',name)
                r.lpush('wronglink',url)
                print 'Download ' + str(downloadIndex) +  '   ' + url + '  failed'
                #return -1
            else:
                print 'Download ' + str(downloadIndex) +  '   ' + url + '  Success'
            time.sleep(1)
            #print url
        except Exception,e:
            print "redis出错"
            print Exception,":",e
            time.sleep(10)
            break
        downloadIndex += 1
    return 0


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
    download('https://github.com/ankidroid/Anki-Android/archive/v2.5.2.zip','Anki-Android')
    #get_releasesite_mysql()
    #get_app_introduction()
#    url = 'http://www.meizitu.com/a/list_1_'
#    print "begin"
#    push_redis_list(4200)#开启则加任务队列.其中的值请限制在5400以内。不过是用于计算页码的
#    get_big_img_url()#开启则运行爬取任务
#    push_redis_list(4500)#开启则加任务队列.其中的值请限制在5400以内。不过是用于计算页码的
#    get_big_img_url()#开启则运行爬取任务
#    push_redis_list(4400)#开启则加任务队列.其中的值请限制在5400以内。不过是用于计算页码的
#    get_big_img_url()#开启则运行爬取任务
#    push_redis_list(4300)#开启则加任务队列.其中的值请限制在5400以内。不过是用于计算页码的
#    get_big_img_url()#开启则运行爬取任务
#    push_redis_list(4200)#开启则加任务队列.其中的值请限制在5400以内。不过是用于计算页码的
#    get_big_img_url()#开启则运行爬取任务
