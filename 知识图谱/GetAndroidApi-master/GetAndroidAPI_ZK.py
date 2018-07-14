#coding:utf-8
#完成通用爬虫，抓取一个页面队列中所有图片
#安卓API页面包括以下内容：Overview,Interfaces,Classes,Enums,Exceptions

import requests
import re
import time
from redis import Redis
import MySQLdb
from scrapy.spiders import Spider
from scrapy.selector import Selector

def download_package(url):

    vsql = ''
    try:
        sel = Selector(requests.get(url)) 

        totals = sel.xpath('//div[@id="body-content"]/div/table/tr')
        for total in totals:

            package_version = total.xpath('@class').extract()[0][13:]
            package_name = total.xpath('td/a/text()').extract()[0]
            package_url = total.xpath('td/a/@href').extract()[0]
            package_des = ""
            try:

                package_des = total.xpath('td/p/text()').extract()[0]

            except Exception, e:
                tmp = total.xpath('td[@class="jd-descrcol"]/text()').extract()
                if len(tmp)!=0:
                    package_des = tmp[0]

            sql = "insert into package_list (package_name,package_url,api_level,package_des) values ( \"" + str(package_name) + "\", \"" + str(
                package_url) + "\", \"" + str(package_version) + "\",\"" + str(
                package_des).replace("\"","\\\"") + "\")"
            vsql = unicode(sql, "utf-8")

            cur.execute(vsql)
            conn.commit()







    except Exception,e:
        print vsql
        print Exception,":",e
        return -1



def get_api_package_list():

    try:
        print 'Begin Download '

        return_1 = download_package(r"https://developer.android.com/reference/packages.html")

        if return_1 == -1:
            return -1
        else:
            print 'Download '  + ' Success'
        #print url
    except Exception,e:
        cur.close()
        conn.commit()
        conn.close()
        print Exception,":",e
        time.sleep(10)

    return 0


conn= MySQLdb.connect(
        host='localhost',
        port = 3306,
        user='root',
        passwd='root',
        db ='android_api',
        charset='utf8'
        )
cur = conn.cursor()


if __name__ == '__main__':
    get_api_package_list()

