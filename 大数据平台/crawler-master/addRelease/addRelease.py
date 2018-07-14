#coding:utf-8
#完成通用爬虫，抓取一个页面队列中所有图片

import MySQLdb

def get_appsite_mysql():
    #获得表中有多少条数据
    result = cur.execute("select release_commit_id,git_release_id from gitrelease")
    print 'gitrelease中有 ' + str(result) + ' 条记录'
    #打印表中的多少数据
    lists = cur.fetchall()
    commitId = ''
    releaseId = 0
    repositoryId = 0
    existRepository = set()
    i = 1
    repositoryId = -1
    for list in lists:
        repositoryId = -1
        commitId = list[0]
        releaseId = list[1]
        #print  str(i) + '   ' + str(list[1])
        result = cur.execute("select * from gitcommit where commit_id = %s",(commitId))
        commits = cur.fetchall()
        if commits != ():
            repositoryId = commits[0][2]
            existRepository.add(repositoryId)
        try:
            cur.execute("update gitrelease set repository_id = %s where git_release_id = %s",(repositoryId,releaseId))
            conn.commit()
        except Exception,e:
            print Exception,":",e
        print str(i) + '   ' + str(list[1]) + '   ' + str(repositoryId) + '   ' + str(len(existRepository)) + '   ' + str(len(commits))
        i += 1
    print len(existRepository)
    cur.execute("select * from repository")
    repositories = cur.fetchall()
    repositoriesId = []
    repositoriesPath = []
    for repository in repositories:
        repositoriesId.append(repository[0])
        repositoriesPath.append(repository[4])
    for exist in existRepository:
        try:
            existIndex = repositoriesId.index(exist)
            repositoriesId.remove(repositoriesId[existIndex])
            repositoriesPath.remove(repositoriesPath[existIndex])
        except Exception,e:
            continue
    print len(repositoriesId)
    for i in range(len(repositoriesId)):
        try:
            print i
            cur.execute("insert into gitrelease(src_address,repository_id) values(%s,%s)",(repositoriesPath[i],repositoriesId[i]))
            conn.commit()
        except Exception,e:
            print Exception,":",e
        
conn= MySQLdb.connect(
        host='10.131.252.156',
        port = 3306,
        user='root',
        passwd='root',
        db ='fdroid',
        charset='utf8'
        )
cur = conn.cursor()


if __name__ == '__main__':
    get_appsite_mysql()
