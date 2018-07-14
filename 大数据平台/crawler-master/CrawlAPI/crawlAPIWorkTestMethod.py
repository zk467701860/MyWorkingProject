#coding:utf-8
#完成通用爬虫，抓取一个页面队列中所有图片

import requests
import re
import time
from redis import Redis
import MySQLdb
from scrapy.spiders import Spider
from scrapy.selector import Selector

def download1(url,id):
    classDescription = ''
    extendClass = ''
    contantName = []
    contantTypeString = []
    contantFirstVersion = []
    fieldName = []
    fieldTypeString = []
    fieldFirstVersion = []
    try:
        print str(id) + '   ' + url
        while 1==1:
            try:
                time.sleep(5)
                sel = Selector(requests.get(url, timeout=10))
            except Exception,e:
                print 'timeout'
                continue
            break  
        totalPubmethods = sel.xpath('//table[@id="pubmethods"]/tr[@class]')
        resolveMethod1(totalPubmethods,id)
        print 'store pubmethods success'
        totalPromethods = sel.xpath('//table[@id="promethods"]/tr[@class]')
        resolveMethod1(totalPromethods,id)
        print 'store promethods success'
    except Exception,e:  
        print Exception,":",e
        return -1

def download(url,id,localclassidlist,localclassversionlist):
    classDescription = ''
    extendClass = ''
    contantName = []
    contantTypeString = []
    contantFirstVersion = []
    fieldName = []
    fieldTypeString = []
    fieldFirstVersion = []
    try:
        print str(id) + '   ' + url
        while 1==1:
            try:
                time.sleep(5)
                sel = Selector(requests.get(url, timeout=10))
            except Exception,e:
                print 'timeout'
                continue
            break  
        totals = sel.xpath('//table[@class="jd-inheritance-table"]/tr/td[@class="jd-inheritance-class-cell"]/a/text()')
        if totals.extract() != []:
            extendClass = totals.extract()[-1]
            #print extendClass
        else:
            extendClass = ''
        contents = sel.xpath('//div[@id="jd-content"]/child::*')
        begin = 0
        end = 0
        apilevel = ''
        for content in contents:
            if content.xpath('@class').extract() != [] and content.xpath('@class').extract()[0].find('api-section') != -1 and content.xpath('text()').extract()[0].find('Summary') != -1:
                end = 1
                break
            if begin == 1 and end == 0:
                classDescription  += content.extract()
            if content.xpath('@class').extract() != [] and content.xpath('@class').extract()[0].find('jd-inheritance-table') != -1:
                begin = 1
        #print classDescription
        if extendClass == '':
            for i in range(len(localclassidlist)):
                cur.execute("update api_class set description = %s where api_class_id = %s",(classDescription,localclassidlist[i]))
                conn.commit()
        else:
            cur.execute("select api_class_id,package_id from api_class where name = %s",(extendClass))
            lists = cur.fetchall()
            for i in range(len(localclassidlist)):
                for list in lists:
                    cur.execute("select library_id from api_package where api_package_id = %s",(list[1]))
                    librarylists = cur.fetchall()
                    cur.execute("select version from api_library where library_id = %s",(librarylists[0][0]))
                    classversionlists = cur.fetchall()
                    if localclassversionlist[i] == classversionlists[0][0]:
                        extendClassId = list[0]
                        break
                #print extendClassId
                cur.execute("update api_class set description = %s,extend_class = %s where api_class_id = %s",(classDescription,extendClassId,localclassidlist[i]))
                conn.commit()
        print 'store classDescription,extendClassId success'
        totalConstants = sel.xpath('//table[@id="constants"]/tr[@class]')
        if totalConstants.extract() != []:
            for contant in totalConstants:
                apilevel = contant.xpath('@class').extract()[0]
                apilevel = apilevel[apilevel.find('-')+1:]
                contantFirstVersion.append(apilevel)
                totalTd = contant.xpath('td')
                totalDescendant = totalTd[0].xpath('descendant::*')
                for desc in totalDescendant:
                    if desc.xpath('text()').extract() != []:
                        contantTypeString.append(desc.xpath('text()').extract())
                contantName.append(totalTd[1].xpath('code/a/text()').extract())
            for i in range(len(contantName)):
                for j in range(len(localclassidlist)):
                    if compareversion(localclassversionlist[j],''.join(contantFirstVersion[i])) >= 0:
                        #print localclassversionlist[j] + ' ' + ''.join(contantFirstVersion[i])
                        cur.execute("insert into api_parameter(name,class_id,type_string,first_version) values(%s,%s,%s,%s)",(''.join(contantName[i]),localclassidlist[j],''.join(contantTypeString[i]),''.join(contantFirstVersion[i])))
                        conn.commit()
            print 'store constant success'
            #print contantName
            #print contantTypeString
            #print contantFirstVersion
        totalFields = sel.xpath('//table[@id="lfields"]/tr[@class]')
        if totalFields.extract() != []:
            for field in totalFields:
                apilevel = field.xpath('@class').extract()[0]
                apilevel = apilevel[apilevel.find('-')+1:]
                fieldFirstVersion.append(apilevel)
                totalTd = field.xpath('td')
                methodreturn = totalTd[0].xpath('code/text()').extract()[0]
                methodreturn = methodreturn.replace("\n", "")
                methodreturn = methodreturn.strip()
                if totalTd[0].xpath('code/a/text()').extract() != []:
                    if len(totalTd[0].xpath('code/a/text()').extract()) == 2:
                        methodreturn += ' ' + totalTd[0].xpath('code/a/text()').extract()[0] +  totalTd[0].xpath('code/text()').extract()[1] + totalTd[0].xpath('code/a/text()').extract()[1] +  totalTd[0].xpath('code/text()').extract()[2]
                    else:
                        methodreturn += ' ' + totalTd[0].xpath('code/a/text()').extract()[0]
                methodreturn = ' '.join(methodreturn.split())
                fieldTypeString.append(methodreturn)
                fieldName.append(totalTd[1].xpath('code/a/text()').extract()[0])
            for i in range(len(fieldName)):
                for j in range(len(localclassidlist)):
                    if compareversion(localclassversionlist[j],''.join(fieldFirstVersion[i])) >= 0:
                        cur.execute("insert into api_parameter(name,class_id,type_string,first_version) values(%s,%s,%s,%s)",(''.join(fieldName[i]),localclassidlist[j],''.join(fieldTypeString[i]),''.join(fieldFirstVersion[i])))
                        conn.commit()
            print 'store constant success'
        totalPubmethods = sel.xpath('//table[@id="pubmethods"]/tr[@class]')
        resolveMethod(totalPubmethods,id,localclassidlist,localclassversionlist)
        print 'store pubmethods success'
        totalPromethods = sel.xpath('//table[@id="promethods"]/tr[@class]')
        resolveMethod(totalPromethods,id,localclassidlist,localclassversionlist)
        print 'store promethods success'
    except Exception,e:  
        print Exception,":",e
        return -1
def resolveMethod1(totalmethods,id):
    if totalmethods.extract() != []:
        for method in totalmethods:
            apilevel = method.xpath('@class').extract()[0]
            apilevel = apilevel[apilevel.find('-')+1:]
            totalTd = method.xpath('td')
            methodreturn = totalTd[0].xpath('code/text()').extract()[0]
            methodreturn = methodreturn.replace("\n", "")
            methodreturn = methodreturn.strip()
            print len(totalTd[0].xpath('code/text()').extract())
            print len(totalTd[0].xpath('code/a/text()').extract())
            print methodreturn
            if methodreturn.split(' ')[0] != methodreturn.split(' ')[-1]:
                methodreturn = methodreturn.split(' ')[0] + ' ' + methodreturn.split(' ')[-1]
            print methodreturn
            if totalTd[0].xpath('code/a/text()').extract() != []:
                if len(totalTd[0].xpath('code/a/text()').extract()) == 2:
                    text1 = totalTd[0].xpath('code/text()').extract()[1]
                    text1 = text1.replace("\n", "")
                    text1 = text1.strip()
                    if len(totalTd[0].xpath('code/text()').extract()) == 2:
                        methodreturn += ' ' + totalTd[0].xpath('code/a/text()').extract()[0] +  text1 + totalTd[0].xpath('code/a/text()').extract()[1]
                    else:
                        methodreturn += ' ' + totalTd[0].xpath('code/a/text()').extract()[0] +  text1 + totalTd[0].xpath('code/a/text()').extract()[1] +  totalTd[0].xpath('code/text()').extract()[2]
                else:
                    methodreturn += ' ' + totalTd[0].xpath('code/a/text()').extract()[0]
            print methodreturn
            currentmethodstatic = 0
            if len(methodreturn.split(' ')) != 1:
                if methodreturn.split(' ')[0].find('static') != -1:
                    currentmethodstatic = 1
                currentmethodreturnstring = methodreturn[methodreturn.find(' ')+1:]
            else:
                currentmethodreturnstring = methodreturn
            print 'method'
            print currentmethodstatic
            print currentmethodreturnstring
            currentmethodname = totalTd[1].xpath('code/a/text()').extract()[0]
            print currentmethodname
            #methodidlist = []
            #for i in range(len(localclassidlist)):
                #if compareversion(localclassversionlist[i],apilevel) >= 0:
                    #cur.execute("insert into api_method(name,return_string,first_version,is_static,class_id) values(%s,%s,%s,%s,%s)",(currentmethodname,currentmethodreturnstring,apilevel,currentmethodstatic,localclassidlist[i]))
                    #currentmethodid = conn.insert_id()
                    #conn.commit()
                    #methodidlist.append(currentmethodid)
            temp = ''
            if len(totalTd[1].xpath('code/a').extract()) == 1:
                temp = totalTd[1].xpath('code/text()').extract()[1]
            else:
                for i in range(1,len(totalTd[1].xpath('code/a').extract())):
                    temp += totalTd[1].xpath('code/text()').extract()[i]
                    temp += totalTd[1].xpath('code/a/text()').extract()[i]
                temp += totalTd[1].xpath('code/text()').extract()[-1]
            temp = temp.replace("\n", "")
            temp = temp.strip()
            temp = temp[1:-1]
            if temp != '':
                templist = temp.split(', ')
                print 'parameter'
                for j in range(len(templist)):
                    currentparametertypestring = templist[j].split(' ')[0]
                    currentparametername = templist[j].split(' ')[1]
                    print currentparametertypestring
                    print currentparametername
                        #cur.execute("insert into api_parameter(name,method_id,type_string,first_version) values(%s,%s,%s,%s)",(currentparametername,methodidlist[i], currentparametertypestring,apilevel))
                        #conn.commit()

def resolveMethod(totalmethods,id,localclassidlist,localclassversionlist):
    if totalmethods.extract() != []:
        for method in totalmethods:
            apilevel = method.xpath('@class').extract()[0]
            apilevel = apilevel[apilevel.find('-')+1:]
            totalTd = method.xpath('td')
            methodreturn = totalTd[0].xpath('code/text()').extract()[0]
            methodreturn = methodreturn.replace("\n", "")
            methodreturn = methodreturn.strip()
            if methodreturn.split(' ')[0] != methodreturn.split(' ')[-1]:
                methodreturn = methodreturn.split(' ')[0] + ' ' + methodreturn.split(' ')[-1]
            if totalTd[0].xpath('code/a/text()').extract() != []:
                if len(totalTd[0].xpath('code/a/text()').extract()) == 2:
                    methodreturn += ' ' + totalTd[0].xpath('code/a/text()').extract()[0] +  totalTd[0].xpath('code/text()').extract()[1] + totalTd[0].xpath('code/a/text()').extract()[1] +  totalTd[0].xpath('code/text()').extract()[2]
                else:
                    methodreturn += ' ' + totalTd[0].xpath('code/a/text()').extract()[0]
            print methodreturn
            currentmethodstatic = 0
            if len(methodreturn.split(' ')) != 1:
                if methodreturn.split(' ')[0].find('static') != -1:
                    currentmethodstatic = 1
                currentmethodreturnstring = methodreturn.split(' ')[1]
            else:
                currentmethodreturnstring = methodreturn
            currentmethodname = totalTd[1].xpath('code/a/text()').extract()[0]
            methodidlist = []
            for i in range(len(localclassidlist)):
                if compareversion(localclassversionlist[i],apilevel) >= 0:
                    #cur.execute("insert into api_method(name,return_string,first_version,is_static,class_id) values(%s,%s,%s,%s,%s)",(currentmethodname,currentmethodreturnstring,apilevel,currentmethodstatic,localclassidlist[i]))
                    #currentmethodid = conn.insert_id()
                    #conn.commit()
                    methodidlist.append(currentmethodid)
            temp = ''
            if len(totalTd[1].xpath('code/a').extract()) == 1:
                temp = totalTd[1].xpath('code/text()').extract()[1]
            else:
                for i in range(1,len(totalTd[1].xpath('code/a').extract())):
                    temp += totalTd[1].xpath('code/text()').extract()[i]
                    temp += totalTd[1].xpath('code/a/text()').extract()[i]
                temp += totalTd[1].xpath('code/text()').extract()[-1]
            temp = temp.replace("\n", "")
            temp = temp.strip()
            temp = temp[1:-1]
            if temp != '':
                templist = temp.split(', ')
                for i in range(len(methodidlist)):
                    for j in range(len(templist)):
                        currentparametertypestring = templist[j].split(' ')[0]
                        currentparametername = templist[j].split(' ')[1]
                        #cur.execute("insert into api_parameter(name,method_id,type_string,first_version) values(%s,%s,%s,%s)",(currentparametername,methodidlist[i], currentparametertypestring,apilevel))
                        #conn.commit()
def get_appsite_mysql():
    r = Redis(host='10.131.252.156',port=6379)
    if r.llen('apiclassid') == 0:
        print r.keys('*')
        #获得表中有多少条数据
        result = cur.execute("select api_class_id,doc_website from api_class")
        print 'api_class中有 ' + str(result) + ' 条记录'
        #打印表中的多少数据
        lists = cur.fetchall()
        for list in lists:
            r.lpush('apiclassid',list[0])
            r.lpush('apiclasslink',list[1])
    print 'redis中api有 ' + str(r.llen('apiclassid')) + ' 条记录'

def compareversion(str1,str2):
    if str1 != 'O' and str2 != 'O':
        if len(str1) != len(str2):
            if len(str1) < len(str2):
                return -1
            else:
                return 1
        else:
            return cmp(str1,str2)
    else:
        return cmp(str1,str2)
def get_app_introduction():
    r = Redis(host='10.131.252.156',port=6379)
    print r.keys('*')
    downloadIndex = 1
    donext = 1
    classidlen = r.llen('apiclassid')
    localclassidlist = []
    localclassversionlist = []
    for classid in range(classidlen):
        try:
            donext = 1
            doingclasslen = r.llen('doingclassid')
            if doingclasslen != 0:
                for i in range(doingclasslen):
                    if r.lindex('apiclassid',classid) == r.lindex('doingclassid',i):
                        donext = 0
                        break
            if donext == 1:
                localclassidlist = []
                localclassversionlist = []
                url = r.lindex('apiclasslink',classid)
                id = r.lindex('apiclassid',classid)
                print 'Begin Download： ' + str(downloadIndex) + '  id:  ' + id
                cur.execute("select name from api_class where api_class_id = %s",(id))
                lists = cur.fetchall()
                cur.execute("select api_class_id,package_id from api_class where name = %s",(lists[0][0]))
                lists = cur.fetchall()
                for list in lists:
                    r.lpush('doingclassid',list[0])
                    localclassidlist.append(list[0])
                    cur.execute("select library_id from api_package where api_package_id = %s",(list[1]))
                    librarylists = cur.fetchall()
                    cur.execute("select version from api_library where library_id = %s",(librarylists[0][0]))
                    classversionlists =  cur.fetchall()
                    localclassversionlist.append(classversionlists[0][0])
                return_1 = download(url,id,localclassidlist,localclassversionlist)
                if return_1 == -1:
                    r.lpush('failapilink',url)
                    r.lpush('failapiid',id)
                    return -1
                else:
                    for list in lists:
                        r.lpush('doneclassid',list[0])
                    print 'Download ' + str(downloadIndex) + ' Success'
                time.sleep(1)
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
    download1('https://developer.android.com/reference/android/accessibilityservice/AccessibilityService.html',1)
    #get_appsite_mysql()
    #get_app_introduction()
