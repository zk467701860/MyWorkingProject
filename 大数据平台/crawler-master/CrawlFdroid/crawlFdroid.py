#coding:utf-8
#完成通用爬虫，抓取一个页面队列中所有图片

import requests
import re
import time
from redis import Redis
import MySQLdb
from scrapy.spiders import Spider
from scrapy.selector import Selector

def download(url,name):
    licensevalue = ''
    website = ''
    issue = ''
    source = ''
    changelog = ''
    introduction = ''
    introend = 0
    allend = 0
    vsql = ''
    try:
        print name + '   ' + url
        sel = Selector(requests.get(url)) 
        #headers = sel.xpath('//div[@id="appheader"]')
        #appname = headers[0].xpath('p/span/text()').extract()	
        #print appname[0]   
        totals = sel.xpath('//div[@class="post-entry"]/p')
        for total in totals:
            if allend == 1:
                break;
            totalsb = total.xpath('b')
            if totalsb != []:
                linkIndex = 0
                for totalb in totalsb:
                    text = totalb.xpath('text()').extract();
                    #print text[0]
                    if text[0].find('License') != -1:
                        introend = 1                 
                        #print total.xpath('a').extract()
                        #print total.xpath('a').extract()!= []  
                        #print total.xpath('text()').extract()
                        if total.xpath('a').extract()!= []:
                            licensevalue = total.xpath('a/text()').extract()[linkIndex]
                        else:
                            licensevalue = total.xpath('text()').extract()[linkIndex]
                    if text[0].find('Website') != -1:                      
                        website = total.xpath('a/text()').extract()[linkIndex]
                    if text[0].find('Issue') != -1:                       
                        issue = total.xpath('a/text()').extract()[linkIndex]
                    if text[0].find('Source') != -1:                            
                        source = total.xpath('a/text()').extract()[linkIndex]
                    if text[0].find('Change') != -1:                            
                        changelog = total.xpath('a/text()').extract()[linkIndex]
                    linkIndex += 1
            else:
                if introend == 1:
                    allend = 1
            if introend == 0:
                #print '111'
                introLinkIndex = 0
                totalChild = total.xpath('child::*')
                if totalChild.extract() != []:
                    if totalChild[0].xpath('text()').extract()[0].find('Non-Free') != -1:
                        continue
                #for child in totalChild:
                #print totalChild[0].xpath('text()').extract()
                #print '222'
                totalslink = total.xpath('text()').extract()
                for link in totalslink:
                    #print "link:" + link
                    if introLinkIndex > 0:
                        introduction += totalChild[introLinkIndex-1].xpath('text()').extract()[0].encode("utf-8","ignore")
                    introduction += link.encode("utf-8")     
                    if link.find('Application not found') != -1:
                        allend = 1
                        break
                    introLinkIndex += 1
                #print 'introduction: ' + str(i) + ' ' + introduction
        #print 'introduction: ' + introduction
        #print 'License: ' + licensevalue
        #print 'Website: ' + website
        #print 'Issue: ' + issue
        #print 'Source: ' + source
        #print 'Changelog: ' + changelog
        introduction = introduction.replace("\\","\\\\")
        introduction = introduction.replace("\'","\\\'")
        name = name.replace("\\","\\\\")
        name = name.replace("\'","\\\'")
        #introduction.replace('"','\"')
        sql = "update collections set license = '" + str(licensevalue) + "',website = '" + str(website) + "',issuetracker = '" + str(issue) + "',sourcecode = '" + str(source) + "',changelog = '" + str(changelog) + "',introduction = '" + str(introduction) + "' where name = '" + name + "'"
        vsql = unicode(sql, "utf-8")
        #vsql.replace("\'","\\\'")
        #print sql
        #print vsql
        cur.execute(vsql)
        conn.commit()
    except Exception,e:
    	print name
    	print vsql
    	print Exception,":",e
    	return -1

def get_appsite_mysql():
    r = Redis()
    print r.keys('*')
    #获得表中有多少条数据
    result = cur.execute("select * from collections")
    print 'collections中有 ' + str(result) + ' 条记录'
    #打印表中的多少数据
    lists = cur.fetchall()
    for list in lists:
        r.lpush('fdroidname',list[0])
        r.lpush('fdroidlink',list[1])
    print 'redis中fdroidname有 ' + str(r.llen('fdroidname')) + ' 条记录'

def get_app_introduction():
    r = Redis()
    print r.keys('*')
    downloadIndex = 0
    while r.llen('fdroidname') != 0:
        try:
            #url = r.lpop('fdroidlink')
            url = r.lindex('fdroidlink',0)
            name = r.lindex('fdroidname',0)
            #name = r.lpop('fdroidname')
            print 'Begin Download ' + str(downloadIndex)
            return_1 = download(url,name)
            #conn.commit()
            if return_1 == -1:
                return -1
            else:
                r.lpop('fdroidlink')
                r.lpop('fdroidname')
                print 'Download ' + str(downloadIndex) + ' Success'
            time.sleep(1)
            #print url
        except Exception,e:
            cur.close()
            conn.commit()
            conn.close()
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
    #download('https://f-droid.org/repository/browse/?fdcategory=Navigation&fdid=fr.nocle.passegares&fdpage=3','PasseGares')
    #get_appsite_mysql()
    get_app_introduction()
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
