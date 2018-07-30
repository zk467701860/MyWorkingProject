from git import *
import MySQLdb
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

try:
    conn=MySQLdb.connect(host='localhost',user='root',passwd='123456',port=3306)
    cur=conn.cursor()
    conn.select_db('mozilla')
    count=cur.execute('SELECT cvs_id, bug_id FROM mozilla.bug_cvs_id1 WHERE EXISTS (SELECT * FROM mozilla.security_bugs where mozilla.security_bugs.id = mozilla.bug_cvs_id1.bug_id) order by cvs_id')
    results=cur.fetchall()
    bugs = {}
    for r in results:

        bugs[int(r[0])] = int(r[1])
    repo = Repo("C:\User"+"s"+"\\"+"bingqi\mozilla-cvs-history")
    commit = repo.commit('master~0')
    i = 1
    mozilla = WriteFiles("bug_diff.csv")
    bug_diff = [["bug_id","commit_time","diff_content"]]

    while i <= 212571:
        if bugs.get(i)!=None:
            bug_id = bugs.pop(i)
            #commit_time = time.strftime("%Y-%m-%d %H:%M:%S", time.gmtime(commit.committed_date))
            paths = []
            lines = []
            try:
                diff = repo.git.diff(commit.parents[0], commit).encode()
                bug_diff.append([str(bug_id),str(commit.committed_date),str(diff)])
                print i
                #list = diff.split("diff --git a/")
                #for file_diff in list:
                #    if(file_diff != ""):
                 #       paths.append(re.search(r"^(.*)\sb/",file_diff).group(1))
                  #      lines.append(" ,".join(re.findall(r'@@\s(.+)\s@@',file_diff)))
                #if paths != []:
                #cur.execute("insert into bug_cvs_security(bug_id,commit_time,diff) values(%s, %s, %s)",(str(bug_id), str(commit.committed_date),str(diff)))
                #conn.commit()
            except Exception, e:
                #print commit.message
                #conn.commit()
                 print i,"   wrong"
        commit = commit.parents[0]
        i = i + 1
    mozilla.writeContent(bug_diff)
    conn.commit()
    cur.close()
    conn.close()

except MySQLdb.Error,e:
    print "Mysql Error %d: %s" % (e.args[0], e.args[1])
