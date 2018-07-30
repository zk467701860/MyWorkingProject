import MySQLdb
from git import Repo
import re
import time
import csv
import sys
reload(sys)
sys.setdefaultencoding('utf-8')

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
repo = Repo("D:\git_pj\history")
commit = repo.commit('master~0')
prepared = [commit]
i = 1
mozilla = WriteFiles("bug_diff2.csv")
bug_diff = [["commit_id","cvs_id","bug_id","committer","commit_msg","commit_time","diff_content"]]

bug_ids = []
commit_ids = []
committed_ids = []
st = 1
reader = csv.reader(open("relevantSecurityBugs_total.csv"))
for a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13 in reader:
    if st==1 :
        st = 0
        continue
    bug_ids.append(a1[3:])


while len(prepared)>0 :
    commit = prepared.pop()
    committed_ids.append(commit.hexsha)
    matchObj = re.search(r'([0-9]{6,})[^0-9a-zA-Z]', commit.message)
    if matchObj:
        bug_id = matchObj.group(1)
        if (bug_id in bug_ids) and commit.hexsha not in commit_ids:
            try:
                commit_id = commit.hexsha
                committer = commit.committer
                diff = repo.git.diff(commit.parents[0], commit).encode()
                time = commit.committed_date
                #try:
                #    bug_diff.append([str(commit_id),str(i),str(bug_id),str(committer),str(commit.message),str(time),"\""+str(diff).replace("\"","\"\"")+"\""])
                #    commit_ids.append(commit_id)
                #except Exception, e:
                #    bug_diff.pop()
                #    print "www"
                cur.execute("insert into t_security_commit(commit_id,cvs_id,bug_id,committer,commit_msg,commit_time,diff_content) values(%s,%s,%s,%s,%s,%s,%s)",(commit_id,i,bug_id,committer,commit.message,time,diff))
                conn.commit()
                print i,bug_id
            except Exception, e:
                #print commit.message
                print i
                cur.close()
                conn.close()
                conn=MySQLdb.connect(host='localhost',user='root',passwd='119038',port=3306)
                cur=conn.cursor()
                conn.select_db('mozilla')

    #if(i % 50000 == 0):
    #    mozilla.writeContent(bug_diff)
    #    mozilla = WriteFiles("bug_diff"+str(i)+".csv")
    #    bug_diff = [["commit_id","cvs_id","bug_id","committer","commit_msg","commit_time","diff_content"]]

    for parent in commit.parents:
        if(parent not in prepared and parent.hexsha not in committed_ids):
            prepared.append(parent)
    prepared.sort(key = lambda x:x.committed_date)

    i = i + 1
    print i,"......."
mozilla.writeContent(bug_diff)
conn.commit()
cur.close()
conn.close()

