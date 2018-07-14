#coding:utf-8
#完成通用爬虫，抓取一个页面队列中所有图片

import requests
import time
import MySQLdb
from redis import Redis

lists = ()
requests.adapters.DEFAULT_RETRIES = 5
def get_token():
    result = cur.execute("select token from github_token")
    global lists
    lists = cur.fetchall()
def get_data():
    r = Redis(host='10.131.252.160',port=6379)
    number = 1
    notdoneindex = 0
    doingindex = ''
    while 1 == 1:
        repositoryAPI = ''
        if r.get('state').find('yes') != -1:
            if notdoneindex != 0:
                doingindex = str(notdoneindex)
                repositoryAPI = 'https://api.github.com/repositories?since=' + str(notdoneindex)
            else:
                doingindex = r.get('currentindex')
                repositoryAPI = 'https://api.github.com/repositories?since=' + str(r.get('currentindex'))
            r.set('state','no')
            try:
                token = {"Authorization": "token " + lists[int(r.get('currenttoken'))][0]}
            except Exception,e:
                r.set('currenttoken', '10')
                token = {"Authorization": "token " + lists[int(r.get('currenttoken'))][0]}
            try:
                repositoryResponse = requests.get(repositoryAPI, headers=token)
                repositoryJson = repositoryResponse.json()
            except Exception,e:
                time.sleep(2)
                r.set('state','yes')
                continue
            if len(repositoryJson) == 0:
                r.set('state','yes')
                break
            elif len(repositoryJson) == 2:
                if 'message' in repositoryJson:
                    if 'block' not in languageJson:
                        print 'repository fail'
                        print repositoryResponse.headers
                        print repositoryJson
                        if int(r.get('currenttoken')) < 109:
                            r.set('currenttoken', str(int(r.get('currenttoken'))+1))
                        else:
                            r.set('currenttoken', '10')
                    notdoneindex = int(doingindex)
                    r.set('state','yes')
            else:
                print str(number) + '  ' + doingindex
                if repositoryJson[len(repositoryJson)-1]['id'] > int(r.get('currentindex')):
                    r.set('currentindex',repositoryJson[len(repositoryJson)-1]['id'])
                r.set('state','yes')
                count = 0
                for i in range(len(repositoryJson)):
                    lanUrl = repositoryJson[i]['languages_url']
                    try:
                        languageResponse = requests.get(lanUrl, headers=token)
                        languageJson = languageResponse.json()
                    except Exception,e:
                        print 'fist language fail:  ' + lanUrl
                        #print Exception,":",e
                        languageJson = {}
                    id = repositoryJson[i]['id']
                    name = repositoryJson[i]['name'] 
                    if isinstance(repositoryJson[i]['owner'],dict):
                        userId = repositoryJson[i]['owner']['id']
                        userName = repositoryJson[i]['owner']['login']
                    else:
                        userId = -1                
                        userName = ''
                    website = repositoryJson[i]['html_url']
                    language = ''
                    if len(languageJson) == 2:
                        if 'message' in languageJson:
                            if 'block' not in languageJson:
                                #print languageResponse.headers
                                #print languageJson
                                if int(r.get('currenttoken')) < 109:
                                    r.set('currenttoken', str(int(r.get('currenttoken'))+1))
                                else:
                                    r.set('currenttoken', '10')
                                time.sleep(1)
                                try:
                                    token = {"Authorization": "token " + lists[int(r.get('currenttoken'))][0]}
                                except Exception,e:
                                    r.set('currenttoken', '10')
                                    token = {"Authorization": "token " + lists[int(r.get('currenttoken'))][0]}
                                try:
                                    languageResponse = requests.get(lanUrl, headers=token)
                                    languageJson = languageResponse.json()
                                    #print languageJson
                                    for lanJs in languageJson:
                                        language += lanJs + ' '
                                    language = language[:-1]
                                except Exception,e:
                                    print 'second language fail:  ' + lanUrl
                                    #print Exception,":",e
                                    language = ''
                        else:
                            for lanJs in languageJson:
                                language += lanJs + ' '
                            language = language[:-1]
                    else:
                        for lanJs in languageJson:
                            language += lanJs + ' '
                        language = language[:-1]
                    try:
                        cur.execute("insert into github_repository(github_id,name,user_id,user_name,language,website) values(%s,%s,%s,%s,%s,%s)",(id,name,userId,userName,language,website))
                        conn.commit()
                        if 'Java' in languageJson:
                            count += 1
                            cur.execute("insert into java_github_repository(github_id,name,user_id,user_name,website) values(%s,%s,%s,%s,%s)",(id,name,userId,userName,website))
                            conn.commit()
                    except Exception,e:
                        print str(i) + '   ' + str(id)
                        print Exception,":",e
                print str(number) + '  contains  ' + str(count) + '  Java Repository'
                number += 1
                notdoneindex = 0

conn= MySQLdb.connect(
        host='localhost',
        port = 3306,
        user='root',
        passwd='123456',
        db ='fdroid',
        charset='utf8'
        )
cur = conn.cursor()

if __name__ == '__main__':
    get_token()
    get_data()
