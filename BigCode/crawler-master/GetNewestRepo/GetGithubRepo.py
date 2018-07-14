import sys
import os
reload(sys)
sys.setdefaultencoding('UTF-8')
from redis import Redis
import MySQLdb
from github import Github
from git import Repo
import subprocess
import datetime
import traceback

##  redis and mysql connection
r = Redis(host='10.131.252.156',port=6379)

while (r.llen('r_ids') != 0):
    id = r.rpop('r_ids')
    sourcecode = r.lpop('r_sourcecodes')
    print   "      " + id + "          " + sourcecode
    if (sourcecode.find(r"https://github.com/") != -1):
        try:
            ##  connecting  github
            #ACCESS_USERNAME = '467701860@qq.com'
            #ACCESS_PWD = "abcd123456"
            Token = "c5923ba617da3f6f68cced74f6e4c0b362c09dc4"
            client = Github(Token)
            #client = Github(ACCESS_USERNAME, ACCESS_PWD)

            githubId = sourcecode[19:]


            ##  download github repository
            try:
                #localAdress = r'/home/fdse/BigCode/FdroidRepo/' + githubId
                localAdress = r'D:\test/' + githubId
                if(os.path.exists(localAdress)):
                    continue
                retcode = subprocess.call(['git', 'clone', sourcecode, localAdress])
                if(retcode != 0):
                    print 'download error!   ', retcode
                    r.rpush('r_ids', id)
                    r.rpush('r_sourcecodes', sourcecode)
                    continue
            except Exception, e:
                print 'download fail!' + sourcecode
                r.rpush('r_ids', id)
                r.rpush('r_sourcecodes', sourcecode)
                print e.message
                print traceback.print_exc()
                continue



            # #  get commit diff
            # repo = Repo(localAdress)
            # commit = repo.commit('master~0')
            # prepared = [commit]
            # i = 1
            #
            # committed_ids = []
            #
            # while len(prepared) > 0:
            #     commit = prepared.pop()
            #     committed_ids.append(commit.hexsha)
            #
            #     try:
            #         commit_id = commit.hexsha
            #         diff = repo.git.diff(commit.parents[0], commit).encode()
            #         time = commit.committed_date
            #
            #         cur.execute(
            #             "update gitcommit set commit_date=%s,diff=%s where commit_id = %s",
            #             (datetime.datetime.fromtimestamp(time), diff, commit_id))
            #         conn.commit()
            #         print i, commit_id
            #     except Exception, e:
            #         print e.message
            #         print traceback.print_exc()
            #         # print commit.message
            #         print i
            #         cur.close()
            #         conn.close()
            #         conn = MySQLdb.connect(host='localhost', user='root', passwd='root', port=3306)
            #         cur = conn.cursor()
            #         conn.select_db('fdroid')
            #
            #     for parent in commit.parents:
            #         if (parent not in prepared and parent.hexsha not in committed_ids):
            #             prepared.append(parent)
            #     prepared.sort(key=lambda x: x.committed_date)
            #
            #     i = i + 1
            #     print i, "......."

            # conn.commit()
            # cur.close()
            # conn.close()
            print 'complete'
        except Exception, e:
            print '------------------error'
            ACCESS_USERNAME = '467701860@qq.com'
            ACCESS_PWD = "abcd123456"
            client = Github(ACCESS_USERNAME, ACCESS_PWD)
            r.rpush('r_ids', id)
            r.rpush('r_sourcecodes', sourcecode)
            print e.message
            print traceback.print_exc()

