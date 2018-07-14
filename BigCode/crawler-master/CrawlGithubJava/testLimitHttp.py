#coding:utf-8
#完成通用爬虫，抓取一个页面队列中所有图片

import requests
requests.adapters.DEFAULT_RETRIES = 5
def get_appsite_mysql():
    for i in range(61):
        languageResponse = requests.get('https://api.github.com/repos/liyiorg/weixin-popular/languages')  
        print languageResponse.json()
        print languageResponse.headers

if __name__ == '__main__':
    get_appsite_mysql()
