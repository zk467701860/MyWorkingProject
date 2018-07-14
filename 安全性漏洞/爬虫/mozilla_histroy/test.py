from git import *
import time
import re
import bisect
import csv
repo = Repo("D:\mygit\mozilla-history")
assert not repo.bare

bug_id = []
reader = csv.reader(open("relevantSecurityBugs.csv"))
for a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13 in reader:
    print a1

heads = repo.heads
master = heads.master
commit = repo.commit('master~0')
parent = commit.parents[0]
#print commit.hexsha,commit.committed_date,commit.committer
#print parent.hexsha,parent.committed_date,parent.Index
if commit.committed_date<=parent.committed_date:
    print "1111111"
diff = repo.git.diff(parent, commit).encode()
list = diff.split("\n")
for line in list:
    if(line[0]=="+" and line[1] !="+"):
        print line
        break
print diff
# i = 0
# while i < 15:
#     commit = repo.commit('master~'+ str(i))
#     print commit.message
#     print commit.env_committer_date
#     i = i + 1

