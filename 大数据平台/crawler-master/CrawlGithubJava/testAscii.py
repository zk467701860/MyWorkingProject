#coding:utf-8
#完成通用爬虫，抓取一个页面队列中所有图片

import requests
import chardet
from datetime import datetime
from redis import Redis

#headers = {"Authorization": "token c5923ba617da3f6f68cced74f6e4c0b362c09dc4"} my
#headers = {"Authorization": "token e7073841959e88d5142d505c1cea3dcfa820e7d5"} my

headers = {"Authorization": "token 7f5cac92246b4c3ec669561f844990c9eb9c5a71"}

#payload = {"access_token":"c5923ba617da3f6f68cced74f6e4c0b362c09dc4"}

#headers = {"Authorization": "username hxjSE"}
requests.adapters.DEFAULT_RETRIES = 5
def get_appsite_mysql():
    r = Redis(host='10.131.252.160',port=6379)
    language = ''
    languageResponse = requests.get('https://api.github.com/repos/auser/sprinkle/languages', headers=headers)  
    #languageResponse = requests.get('https://api.github.com/repos/liyiorg/weixin-popular/languages', params=payload)
    #repositoryJson = repositoryResponse.json()
    #print repositoryJson[len(repositoryJson)-1]['id']
    #if isinstance(repositoryJson[len(repositoryJson)-1]['id'],int):    
    	#print 'int'
    #if repositoryJson[len(repositoryJson)-1]['id'] > int(r.get('currentindex')):
        #r.set('currentindex',repositoryJson[len(repositoryJson)-1]['id'])
    #print r.get('currentindex')
    #print r.get('currentindex') == '32290'
    #or i in range(len(repositoryJson)):
        #lanUrl = repositoryJson[i]['languages_url']
        #languageResponse = requests.get(lanUrl, headers=headers)
        #languageJson = languageResponse.json()
        #print lanUrl
        #print i
        #print languageJson
    #print languageJson['Java']
    languageJson = languageResponse.json()
    print languageJson
    for i in languageJson:
        language += i + ' '
    language = language[:-1]
    print language
    #if languageJson['Java'].find()

if __name__ == '__main__':
    #get_data('Расписание МГКЭ','https://github.com/queler/holokenmod')
    get_appsite_mysql()
