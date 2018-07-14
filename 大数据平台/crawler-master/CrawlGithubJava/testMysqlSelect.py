#coding:utf-8
#完成通用爬虫，抓取一个页面队列中所有图片

import requests
import MySQLdb
from redis import Redis

lists = ()
def get_token():
    r = Redis(host='10.131.252.160',port=6379)
    result = cur.execute("select token from github_token")
    global lists
    lists = cur.fetchall()
    #if isinstance(lists,list):
        #print "list"
    #if isinstance(lists,tuple):
        #print "tuple"
    #print lists
    #token = {"Authorization": "token " + lists[110][0]}
    #print token
    #number = 10
    #print number
    #number = int(r.get('currentindex'))
    #print number
    #r.set('currenttoken', 0)
    #print int(r.get('currenttoken')) == 0
    #r.set('currenttoken', int(r.get('currenttoken'))+1)
    #print int(r.get('currenttoken')) == 0
    #repositoryResponse = requests.get('https://api.github.com/repositories', headers=token)   
    #print repositoryResponse.headers
    #for i in range(110):
        #print lists[i]

def get_data():
    r = Redis(host='10.131.252.160',port=6379)
    for i in range(110):
        token = {"Authorization": "token " + lists[i][0]}
        #print str(i+1) + '   ' + str(token)
        repositoryAPI = 'https://api.github.com/repositories?since=' + str(r.get('currentindex'))
        repositoryResponse = requests.get(repositoryAPI, headers=token)
        if int(repositoryResponse.headers['X-RateLimit-Limit']) == 60:
            print str(i+1) + '  60  ' +  repositoryResponse.headers['X-RateLimit-Remaining']
        elif int(repositoryResponse.headers['X-RateLimit-Limit']) == 5000:
            print str(i+1) + '  5000  ' +  repositoryResponse.headers['X-RateLimit-Remaining']
    #print r.get('currentindex') == '42520'
    #print r.get('currentindex') == 42520
    #r.set('currentindex',100)
    #print r.get('currentindex') == '100'
    #print r.get('currentindex') == 100
    #if int(r.get('currenttoken')) != 110:
        #r.set('token', str(int(r.get('currenttoken'))+1))
    #else:
        #r.set('token', '0')
    #print int(r.get('currenttoken')) == 

conn= MySQLdb.connect(
        host='10.131.252.160',
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
