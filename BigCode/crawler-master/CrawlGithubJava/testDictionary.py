#coding:utf-8
#完成通用爬虫，抓取一个页面队列中所有图片
str1 = 'token ' + '296f393ac0a814f2ebd2324496c9e11221a4a692'
headers = {"Ruby": 948883, "JavaScript": 8925}
headers1 = {}

language = ''
for i in headers:
    language += i + ' '
language = language[:-1]
print language
