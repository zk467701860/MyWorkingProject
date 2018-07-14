#coding:utf-8
#完成通用爬虫，抓取一个页面队列中所有图片

import sys

def get_appsite_mysql():
    print sys.argv[1]
    print str(sys.argv[1])
    if isinstance(sys.argv[1],str):
    	print 'str'


if __name__ == '__main__':
    #get_data('Расписание МГКЭ','https://github.com/queler/holokenmod')
    get_appsite_mysql()
