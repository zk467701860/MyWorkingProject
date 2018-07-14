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
import re
import traceback

def get_api_package_list():

    try:
        print 'Begin Download '

        return_1 = download_package_list(r"https://developer.android.com/reference/packages.html")

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


def download_package_list(url):

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







def get_api_package_info():
    print 'Begin Download '
    while (r.llen('api_p_id') != 0):
        api_id = r.lpop('api_p_id')
        api_name = r.lpop('api_p_name')
        api_url = r.lpop('api_p_url')
        api_level = r.lpop('api_p_level')

        return_1 = download_package_info(api_id,api_name,api_url,api_level)

        if return_1 == -1:
            r.lpush('api_p_id', api_id)
            r.lpush('api_p_name', api_name)
            r.lpush('api_p_url', api_url)
            r.lpush('api_p_level', api_level)
            return -1
        else:
            print 'Download  '+api_id+"     "+api_name + ' Success'
    return 0

def download_package_info(api_id,api_name,api_url,api_level):

    try:
        sel = Selector(requests.get(api_url))
        totals = sel.xpath('//div[@id="jd-content"]').extract()

        # get description of package
        des = totals[0]
        des = des[des.index("</h1>") + 5:des.index("<h2>")]

        strinfo = re.compile('(href)(\s)*=(\s)*"(.)*"')
        des = strinfo.sub(' ', des)

        strinfo = re.compile('<!--(\s)*(.\s)*(\s)*-->')
        des = strinfo.sub(' ', des)

        # for all library version, insert into all version package
        version = -1
        if api_level == "O":
            version = 26
        elif api_level == '':
            version = 1
        elif api_level.find(".") > 0:
            version = int(api_level[0:api_level.index(".")])
        else:
            version = int(api_level)

        for num in range(version, 27):
            cur.execute("insert into api_package (name,first_version,description,doc_website,library_id) "
                        "values (%s,%s,%s,%s,%s)", (api_name, api_level, des, api_url, num))
            conn.commit()

        return 0
    except Exception,e:
        return -1
        print traceback.print_exc()



def read_list_from_db():
    cur.execute("select package_name,package_url,api_level,id from api_package_list")
    results = cur.fetchall()
    for result in results:
        r.lpush('api_p_id',result[3])
        r.lpush('api_p_name', result[0])
        r.lpush('api_p_url', result[1])
        r.lpush('api_p_level', result[2])

    return 0

def insert_library():
    for num in range(1,26):
        cur.execute("insert into api_library (name,orgnization,introduction,version,license,doc_website) values (%s,%s,%s,%s,%s,%s)",("Android Platform","Google","Android provides a rich application framework that enables you to develop innovative applications and games for mobile devices in the Java language environment.",num,"Creative Commons Attribution 2.5","https://developer.android.com/reference/packages.html"))
        conn.commit()
    cur.execute(
        "insert into api_library (name,orgnization,introduction,version,license,doc_website) values (%s,%s,%s,%s,%s,%s)",
        ("Android Platform", "Google",
        "Android provides a rich application framework that enables you to develop innovative applications and games for mobile devices in the Java language environment.",
        "O", "Creative Commons Attribution 2.5", "https://developer.android.com/reference/packages.html"))
    conn.commit()

def get_api_class_info():
    print 'Begin Download '
    while (r.llen('api_p_id') != 0):
        api_id = r.lpop('api_p_id')
        api_name = r.lpop('api_p_name')
        api_url = r.lpop('api_p_url')
        api_level = r.lpop('api_p_level')

        return_1 = download_class_info(api_name, api_url)

        if return_1 == -1:
            r.lpush('api_p_id', api_id)
            r.lpush('api_p_name', api_name)
            r.lpush('api_p_url', api_url)
            r.lpush('api_p_level', api_level)
            return -1
        else:
            print 'Download  ' + api_id + "     " + api_name + ' Success'
    return 0

def download_class_info(api_name,api_url):
    try:

        sel = Selector(requests.get(api_url))
        totals = sel.xpath('//div[@id="jd-content"]/h2')

        for total in totals:
            if(len(total.xpath('text()').extract())!=0):
                class_type = total.xpath('text()').extract()[0]
                if(class_type=='Interfaces' or class_type=='Annotations'or class_type=='Classes'or class_type=='Enums'or class_type=='Exceptions'or class_type=='Errors'):
                    for table in total.xpath('following-sibling::table[1]'):
                        for tr in table.xpath('tr'):
                            class_version = tr.xpath('@class').extract()[0]
                            class_version = class_version[class_version.index('apilevel-') + 9:]
                            class_url = ''
                            if len(tr.xpath('td/a/@href').extract())!=0:
                                class_url = tr.xpath('td/a/@href').extract()[0]

                            class_name=''
                            if len(tr.xpath('td/a/text()').extract()) != 0:
                                class_name = tr.xpath('td/a/text()').extract()[0]
                            elif len(tr.xpath('td/text()').extract()) != 0:
                                class_name = tr.xpath('td/text()').extract()[0]

                            # for all library version, insert into all version package
                            version = -1
                            if class_version == "O":
                                version = 26
                            elif class_version == '':
                                version = 1
                            elif class_version.find(".") > 0:
                                version = int(class_version[0:class_version.index(".")])
                            elif class_version.find(" ") > 0:
                                version = int(class_version[0:class_version.index(" ")])
                            else:
                                version = int(class_version)

                            for num in range(version, 27):
                                class_package_id = -1
                                cur.execute(
                                    "select api_package_id from api_package where name = %s and doc_website = %s and library_id = %s",
                                    (api_name, api_url, num))
                                results = cur.fetchall()
                                if len(results) != 0:
                                    class_package_id = int(results[0][0])
                                    cur.execute(
                                        "insert into api_class (name,first_version,type,package_id,doc_website) values (%s,%s,%s,%s,%s)",
                                        (api_name +'.'+ class_name, class_version, class_type, class_package_id, class_url))
                                    conn.commit()



        return 0
    except Exception,e:

        print traceback.print_exc()
        return -1

conn= MySQLdb.connect(
        host='10.131.252.156',
        port = 3306,
        user='root',
        passwd='root',
        db ='fdroid',
        use_unicode=True,
        charset='utf8'
        )
cur = conn.cursor()
r = Redis(host='10.131.252.156',port=6379)

if __name__ == '__main__':
    #get_api_package_list()
    #read_list_from_db()
    get_api_class_info()
    #get_api_package_info()
    #insert_library()

