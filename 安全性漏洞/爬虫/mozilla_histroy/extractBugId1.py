import MySQLdb
from git import Repo
import re
import time
import sys
reload(sys)
sys.setdefaultencoding('utf-8')
import csv

class WriteFiles:
    def __init__(self,files):
        self.files = files

    def writeContent(self,bug_content):
        with open(self.files, 'wb') as f:
            writer = csv.writer(f)
            writer.writerows(bug_content)

conn=MySQLdb.connect(host='localhost',user='root',passwd='123456',port=3306)
cur=conn.cursor()
conn.select_db('mozilla')
repo = Repo("C:\User"+"s"+"\\"+"bingqi\mozilla-cvs-history")
commit = repo.commit('master~0')
i = 1
mozilla = WriteFiles("bug_diff_history1.csv")
bug_diff = [["commit_id","cvs_id","bug_id","committer","commit_msg","commit_time","diff_content"]]
while i<=212571 :
    matchObj = re.search(r'([0-9]{6,})[^0-9a-zA-Z]', commit.message)
    if matchObj:
        bug_id = matchObj.group(1)
        try:
            commit_id = commit.hexsha
            committer = commit.committer
            diff = repo.git.diff(commit.parents[0], commit).encode()
            time = commit.committed_date
            bug_diff.append([str(commit_id),str(i),str(bug_id),str(committer),str(commit.message),str(time),str(diff)])
            #cur.execute("insert into bug_cvs_id1(cvs_id,bug_id) values(%s,%s)",(i,bug_id))
            #conn.commit()
            print i,"...."
        except Exception, e:
            #print commit.message
            print i
            #cur.close()
            #conn.close()
            #conn=MySQLdb.connect(host='localhost',user='root',passwd='123456',port=3306)
            #cur=conn.cursor()
            #conn.select_db('mozilla')
    if(i % 50000 == 0):
        mozilla.writeContent(bug_diff)
        mozilla = WriteFiles("bug_diff_history"+str(i)+".csv")
        bug_diff = [["commit_id","cvs_id","bug_id","committer","commit_msg","commit_time","diff_content"]]
    commit = commit.parents[0]

    i = i + 1
conn.commit()
cur.close()
conn.close()

