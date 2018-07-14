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
    count=cur.execute('SELECT cvs_id, bug_id FROM mozilla.bug_cvs_id WHERE EXISTS (SELECT * FROM mozilla.security_bugs where mozilla.security_bugs.id = mozilla.bug_cvs_id.bug_id) order by cvs_id')
    results=cur.fetchall()
    bugs = {}
    for r in results:
        bugs[int(r[0])] = int(r[1])
    repo = Repo("D:\mygit\mozilla-history")
    commit = repo.commit('master~0')
    prepared = [commit]
    i = 1
    mozilla = WriteFiles("bug_diff.csv")
    bug_diff = [["commit_id","cvs_id","bug_id","commit_time","diff_content"]]

    while i <= 279274:
        commit = prepared.pop()
        if bugs.get(i)!=None:
            bug_id = bugs.pop(i)
            #commit_time = time.strftime("%Y-%m-%d %H:%M:%S", time.gmtime(commit.committed_date))
            paths = []
            lines = []
            try:
                diff = repo.git.diff(commit.parents[0], commit).encode()
                bug_diff.append([commit.hexsha,i,bug_id,commit.committed_date,diff])
                print i
                #list = diff.split("diff --git a/")
                #for file_diff in list:
                #    if(file_diff != ""):
                 #       paths.append(re.search(r"^(.*)\sb/",file_diff).group(1))
                  #      lines.append(" ,".join(re.findall(r'@@\s(.+)\s@@',file_diff)))
                #if paths != []:
                 #   cur.execute("insert into bug_comments(cvs_id, bug_id, diff_file, diff_line, commit_time) values(%s, %s, %s, %s, %s)",(i, bug_id, " ;".join(paths), " ;".join(lines), commit_time))
            except Exception, e:
                #print commit.message
                #conn.commit()
                 print i,"   wrong"
        for parent in commit.parents:
            if(parent not in prepared):
                prepared.append(parent)
        prepared.sort(key = lambda x:x.committed_date)
        i = i + 1
    mozilla.writeContent(bug_diff)
    conn.commit()
    cur.close()
    conn.close()

except MySQLdb.Error,e:
    print "Mysql Error %d: %s" % (e.args[0], e.args[1])
