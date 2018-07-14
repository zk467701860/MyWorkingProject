import sys
import os
reload(sys)
sys.setdefaultencoding('UTF-8')
from redis import Redis
import subprocess
import traceback

##  redis and mysql connection
r = Redis(host='10.131.252.156',port=6379)

# file_object = open('git.txt')
# all = file_object.read()
# lines = all.split('\r')
# for line in lines:
#     r.lpush('cc_url',line)

while (r.llen('cc_url') != 0):
    id = r.lpop('cc_url')
    print   "      " + id
    #if (sourcecode.find(r"https://github.com/") != -1):

    try:
        ##  connecting  github

        index = id.find('.git')
        githubId = id[19:index]

        ##  download github repository
        try:
            localAdress = r'/root/BigCode/cc_githubRepo/' + githubId
            # localAdress = r'D:\test/' + githubId
            if (os.path.exists(localAdress)):
                continue
            retcode = subprocess.call(['git', 'clone', id, localAdress])
            if (retcode != 0):
                print 'download error!   111   '+ retcode +id
                r.rpush('cc_url', id)
                continue
        except Exception, e:
            print 'download fail!  222   ' + id
            r.rpush('cc_url', id)
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
        r.rpush('cc_url', id)
        print e.message
        print traceback.print_exc()

