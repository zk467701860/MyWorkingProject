import MySQLdb
import re
from scrapy.selector import HtmlXPathSelector
from scrapy.spider import BaseSpider
from history.items import HistoryItem
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

class HistorySpider(BaseSpider):
    name = "history"
    allowed_domains = ["bugzilla.mozilla.org"]
    start_urls = []



    def __init__(self, *a, **kw):
        super(HistorySpider, self).__init__(*a, **kw)
        url = "https://bugzilla.mozilla.org/show_activity.cgi?id="
        bug_ids = []
        st = 1
        reader = csv.reader(open("relevantSecurityBugs_total.csv"))
        for a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13 in reader:
            if st==1 :
                st = 0
                continue
            bug_ids.append(a1[3:])

        for r in bug_ids:
            self.start_urls.append(url+str(r))

        #conn=MySQLdb.connect(host='localhost',user='root',passwd='123456',port=3306)
        #cur=conn.cursor()
        #conn.select_db('mozilla')
        #cur.execute("SELECT id FROM mozilla.bugs where history is null LIMIT 10000")
        #cur.execute("SELECT bug_id FROM mozilla.securitybug_edit group by bug_id")
        #results=cur.fetchall()
        #for r in results:
            #self.start_urls.append(url+str(r[0]))

    def parse(self, response):
        self.log("crawl page %s" % response.url)
        hxs = HtmlXPathSelector(response)
        trs = hxs.select("//div[@id='bugzilla-body']//table/tr")
        time = None
        history = []
        items = []
        for tr in trs:
            timeExtracted = tr.select("./td[2]/text()").re(r'^([0-9\-:\s]+)\s[A-Z]')
            what = []
            removed = []
            added = []
            if(timeExtracted != []):
                time = timeExtracted[0]
                what = tr.select("./td[3]/text()").re(r"([A-Za-z]+)")
                #removed = tr.select("./td[4]/text()").re(r"([A-Za-z\-]+)")
                added = tr.select("./td[5]/text()").re(r"([A-Za-z\-]+)")
                print time
                if(len(added)==0 or added[0] != "REOPENED"):
                    continue
            else:
                what = tr.select("./td[1]/text()").re(r"([A-Za-z]+)")
                #removed = tr.select("./td[2]/text()").re(r"([A-Za-z\-]+)")
                added = tr.select("./td[3]/text()").re(r"([A-Za-z\-]+)")
                if(len(added)==0 or added[0] != "REOPENED"):
                    continue
            if what != [] and (what[0] == "Status" or what[0] == "Resolution"):
                #history.append(",".join([time, removed[0], added[0]]))
                item = HistoryItem()
                item["bug_id"] = re.search("id=([0-9]+)",response.url).group(1)
                item["history"] = time
                items.append(item)
                print "1111111111111111",item
        return items
        #item = HistoryItem()
        #item["bug_id"] = re.search("id=([0-9]+)",response.url).group(1)
        #item["history"] = history
        #return [item]