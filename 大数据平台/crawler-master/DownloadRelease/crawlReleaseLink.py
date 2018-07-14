#coding:utf-8
#完成通用爬虫，抓取一个页面队列中所有图片

import requests
import re
import time
import MySQLdb
from scrapy.spiders import Spider
from scrapy.selector import Selector
import os

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
        name = list[0]
        name = name.replace("\'","\\\'").encode("utf-8")
        print name + '   ' + str(i)
        link = list[7]
        if link == '':
            i += 1
            continue
        if link.find('github') != -1:
            releaselink = link + '/releases'
            resolve_apprelease_link(name,releaselink)   
        i += 1

def resolve_apprelease_link(name,url):
    while 1==1:
        try:
            time.sleep(5)
            sel = Selector(requests.get(url, timeout=10))
        except Exception,e:
            print 'timeout'
            continue
        break
    totals = sel.xpath('//div[@class="repository-content"]/descendant::a')
    url = ''
    downloadLink = ''
    vsql = ''
    try:
        #print totals
        for total in totals:
            url = total.xpath('@href').extract()[0]
            #print url
            if url.find('archive') != -1 and url.find('zip') != -1:
                downloadLink = 'https://github.com' + url
                #print downloadLink    
                sql = "insert into releaseurl(name,website) values('" + str(name) + "','" + str(downloadLink) + "')"
                vsql = unicode(sql, "utf-8")
                cur.execute(vsql)
                conn.commit()
            if url.find('releases?after=') != -1 and total.xpath('text()').extract()[0].find('Next') != -1:
                resolve_apprelease_link(name,url)
    except Exception,e:
        print vsql
        print Exception,":",e
        return -1

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
    #resolve_apprelease_link('pyramid','https://github.com/rengolin/cardgamescores/releases')
    get_appsite_mysql()
