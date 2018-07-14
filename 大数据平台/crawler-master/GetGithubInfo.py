import sys
reload(sys)
sys.setdefaultencoding('UTF-8')
from redis import Redis
import MySQLdb
from github import Github
from git import Repo
import subprocess
import  datetime
import traceback


##  redis and mysql connection
r = Redis(host='10.131.252.156',port=6379)



while(r.llen('ids')!=0):
    id = r.lpop('ids')
    sourcecode = r.lpop('sourcecodes')
    print "      "+id+"          "+sourcecode
    if(sourcecode.find(r"https://github.com/")!=-1):
        conn = MySQLdb.connect(host='localhost', user='root', passwd='root', port=3306,use_unicode=True, charset="utf8")
        cur = conn.cursor()
        conn.select_db('fdroid')
        try:
            ##  connecting  github
            ACCESS_USERNAME = '467701860@qq.com'
            ACCESS_PWD = "abcd123456"
            client = Github(ACCESS_USERNAME, ACCESS_PWD)

            githubId = sourcecode[19:]
            repo = client.get_repo(githubId)

            
            localAdress = r'/home/fdse/BigCode/FdroidRepo/' + githubId

            # ##  download github repository
            # try:
            #     localAdress = r'/home/fdse/BigCode/FdroidRepo/' + githubId
            #     #localAdress = r'D:\test/' + githubId
            #     subprocess.call(['git', 'clone', sourcecode, localAdress])
            # except Exception, e:
            #     print 'download fail!' + sourcecode
            #     r.rpush('ids', id)
            #     r.rpush('sourcecodes', sourcecode)
            #     print e.message
            #     print traceback.print_exc()
            #     continue

            ##  get repository info
            repoDis = None
            if( repo.description):
                repoDis = repo.description.encode('utf-8')
            try:
                cur.execute(
                    "insert into repository(repository_name,git_address,issue_address,local_address,description,added_date) values(%s,%s,%s,%s,%s,%s)",
                    (repo.name, sourcecode, repo.issues_url, localAdress,
                     repoDis,
                     repo.created_at))
                conn.commit()
            except Exception, e:
                print e.message
                print traceback.print_exc()
                print 'insert repo fail!' + repo.name
                r.rpush('ids', id)
                r.rpush('sourcecodes', sourcecode)
                cur.close()
                conn.close()
                conn = MySQLdb.connect(host='localhost', user='root', passwd='root', port=3306,use_unicode=True, charset="utf8")
                cur = conn.cursor()
                conn.select_db('fdroid')
                continue

            aa = cur.execute('select repository_id from repository where git_address = %s', sourcecode)
            repoId = int(cur.fetchone()[0])

            issueList = repo.get_issues()
            commitList = repo.get_commits()
            ##  get issue info from github, including event and comment
            for issue in issueList:
                try:
                    issueEvents = issue.get_events()
                    issueComments = issue.get_comments()
                    issueLabels = ""
                    for issueLabel in issue.labels:
                        issueLabels += "; " + issueLabel.name

                    ##  add issue info into mysql

                    assigneeName = None
                    assigneeId = -1
                    issueClosedTime = None
                    if (issue.assignee):
                        assigneeId = issue.assignee.id
                        assigneeName = issue.assignee.name
                    if(issue.closed_at):
                        issueClosedTime = issue.closed_at.strftime("%Y-%m-%d %H:%M:%S")
                    cur.execute(
                        "insert into issue(repository_id,issue_id,created_at,closed_at,assignee_name,assignee_id,state,number,title,content,labels) values(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)",
                        (repoId, issue.id, issue.created_at.strftime("%Y-%m-%d %H:%M:%S"),
                         issueClosedTime, assigneeName, assigneeId, issue.state,
                         issue.number, issue.title.encode('utf-8'), issue.body.encode('utf-8'), issueLabels))
                    conn.commit()
                    print 'issue'

                    ## add issueEvent into mysql


                    for issueEvent in issueEvents:
                        try:
                            issueEventActId = -1
                            issueEventActName = None

                            if (issueEvent.actor):
                                issueEventActId = issueEvent.actor.id
                                issueEventActName = issueEvent.actor.name

                            cur.execute(
                                "insert into issueevent(repository_id,issue_id,event_id,about_commit_id,event,created_at,actor_id,actor_name) values(%s,%s,%s,%s,%s,%s,%s,%s)",
                                (
                                repoId, issue.id, issueEvent.id, issueEvent.commit_id, issueEvent.event,
                                issueEvent.created_at.strftime("%Y-%m-%d %H:%M:%S"), issueEventActId,
                                issueEventActName))
                            conn.commit()
                        except Exception, e:
                            print e.message
                            print traceback.print_exc()
                            print 'insert issueEvent fail!' + repo.name
                            cur.close()
                            conn.close()
                            conn = MySQLdb.connect(host='localhost', user='root', passwd='root', port=3306,use_unicode=True, charset="utf8")
                            cur = conn.cursor()
                            conn.select_db('fdroid')
                            continue
                        print 'event'

                    for issueComment in issueComments:
                        issueCommentName = None
                        issueCommentId = -1

                        if (issueComment.user):
                            issueCommentId = issueComment.user.id
                            issueCommentName = issueComment.user.name
                        try:
                            cur.execute(
                                "insert into issuecomment(repository_id,issue_id,comment_id,author_id,author_name,content,created_at,updated_at) values(%s,%s,%s,%s,%s,%s,%s,%s)",
                                (repoId, issue.id, issueComment.id, issueCommentId, issueCommentName,
                                 issueComment.body,
                                 issueComment.created_at.strftime("%Y-%m-%d %H:%M:%S"),
                                 issueComment.updated_at))
                            conn.commit()
                        except Exception, e:
                            print e.message
                            print traceback.print_exc()
                            print 'insert issueComment fail!' + repo.name
                            cur.close()
                            conn.close()
                            conn = MySQLdb.connect(host='localhost', user='root', passwd='root', port=3306,use_unicode=True, charset="utf8")
                            cur = conn.cursor()
                            conn.select_db('fdroid')
                            continue

                        print 'comment'
                except Exception, e:
                    print e.message
                    print traceback.print_exc()
                    cur.close()
                    conn.close()
                    conn = MySQLdb.connect(host='localhost', user='root', passwd='root', port=3306,use_unicode=True, charset="utf8")
                    cur = conn.cursor()
                    conn.select_db('fdroid')
                    print 'insert issue fail!' + repo.name
                    continue

                    ##  get commit info from github
            for commit in commitList:
                try:
                    committerName = None
                    committerId = -1

                    if (commit.committer):
                        committerId = commit.committer.id
                        committerName = commit.committer.name
                    cur.execute(
                        "insert into gitcommit(commit_id,repository_id,author_id,author_name,message,additions,deletions) values(%s,%s,%s,%s,%s,%s,%s)",
                        (commit.sha, repoId, committerId, committerName, commit.commit.message.encode('utf-8'),
                         commit.stats.additions, commit.stats.deletions))
                    for commitParent in commit.parents:
                        cur.execute(
                            "insert into commitparent(commit_id,parent_id) values(%s,%s)",
                            (commit.sha, commitParent.sha))
                    conn.commit()
                except Exception, e:
                    print e.message
                    print traceback.print_exc()
                    cur.close()
                    conn.close()
                    conn = MySQLdb.connect(host='localhost', user='root', passwd='root', port=3306,use_unicode=True, charset="utf8")
                    cur = conn.cursor()
                    conn.select_db('fdroid')
                    print 'insert commitinfo fail!' + repo.name
                    continue

                print 'commit'

            ##  get commit diff
            # repo = Repo(localAdress)
            # commit = repo.commit('master~0')
            # prepared = [commit]
            # i = 1

            # committed_ids = []

            # while len(prepared) > 0:
            #     commit = prepared.pop()
            #     committed_ids.append(commit.hexsha)

            #     try:
            #         commit_id = commit.hexsha
            #         diff = repo.git.diff(commit.parents[0], commit).encode()
            #         time = commit.committed_date

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
            #         conn = MySQLdb.connect(host='localhost', user='root', passwd='root', port=3306,use_unicode=True, charset="utf8")
            #         cur = conn.cursor()
            #         conn.select_db('fdroid')

            #     for parent in commit.parents:
            #         if (parent not in prepared and parent.hexsha not in committed_ids):
            #             prepared.append(parent)
            #     prepared.sort(key=lambda x: x.committed_date)

            #     i = i + 1
            #     print i, "......."

            conn.commit()
            cur.close()
            conn.close()
            print 'complete'
        except Exception,e:
            print '------------------error'
            ACCESS_USERNAME = '467701860@qq.com'
            ACCESS_PWD = "abcd123456"
            client = Github(ACCESS_USERNAME, ACCESS_PWD)
            r.rpush('ids', id)
            r.rpush('sourcecodes', sourcecode)
            print e.message
            print traceback.print_exc()

