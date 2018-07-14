
#coding=gbk
from bs4 import BeautifulSoup
import sys
reload(sys)
sys.setdefaultencoding('utf-8')
import csv
import urllib2
from multiprocessing import Process
class WriteFiles:
    def __init__(self,files):
        self.files = files
    ###写文件内容
    def writeContent(self,bug_content):
        with open(self.files, 'wb') as f:
            writer = csv.writer(f)
            writer.writerows(bug_content)
class GetData:
    def __init__(self,start,end):
        ###bug编号
        #self.bug_num = range(249332,1214345)
        self.start = start
        self.end = end
        self.bug_num = range(start,end)
        self.bug_path = "https://bugzilla.mozilla.org/show_bug.cgi?id="
    def getBug(self):
        ###要抓取的内容
        bug_content = [["bug_ID","bug_Msg","status","component","product","assigned","Duplicates","Depends on","Blocks","report_time","reporter","modified_time","CCList"]]
        for i in range(len(self.bug_num)):
            url = self.bug_path + str(self.bug_num[i])
            try:
                c = urllib2.urlopen(url,timeout=240).read()
                soup = BeautifulSoup(c)
                bug = ["bug"+str(self.bug_num[i])]
                print bug
                bug_Msg=soup.find("div",attrs={"class":"bz_alias_short_desc_container edit_form"}).find("span",attrs={"id":"short_desc_nonedit_display"}).getText()
                bug.append(str(bug_Msg))
                table = soup.find("table",attrs={"class":"edit_form"})
                ###列表左边数据
                column1 = table.find("td",attrs={"id":"bz_show_bug_column_1"})
                left_tr = column1.table.findAll("tr")
                column2 = table.find("td",attrs={"id":"bz_show_bug_column_2"})
                right_tr = column2.table.findAll("tr")
            except:
                continue
            depends = ""
            Blocks = ""
            Duplicates = ""            
            for item in left_tr:
                try:
                    th = str(item.find("th").getText())
                except:
                    th = "null"
                if "Status" in th:
                    bug_status = item.td.getText()
                    bug.append(str(bug_status))                    
                if "Component" in th:
                    bug_component = item.td.getText()
                    bug.append(str(bug_component))                    
                if "Product" in th:
                    bug_product = item.td.getText()
                    bug.append(str(bug_product))                  
                if "Assigned To" in th:
                    bug_assigned = item.td.getText()
                    bug.append(str(bug_assigned)) 
                if "Duplicates" in th:
                    allDuplicates = item.td.findAll("a")
                    for itemd in allDuplicates:
                        Duplicates = Duplicates + itemd.getText() + "\n"
                if "Depends on" in th:
                    alldepends = item.td.findAll("a")
                    for itemd in alldepends:
                        depends = depends + itemd.getText() + "\n"
                if "Blocks" in th:
                    allBlocks = item.td.findAll("a")
                    for itemd in allBlocks:
                        Blocks = Blocks + itemd.getText() + "\n"
            if depends=="":
                depends = "NULL"
            if Blocks == "":
                Blocks = "NULL"
            if Duplicates == "":
                Duplicates = "NULL"
            bug.append(str(Duplicates))
            bug.append(str(depends))
            bug.append(str(Blocks)) 
            for item in right_tr:
                try:
                    th = str(item.find("th").getText())
                except:
                    th = "null"
                if "Reported" in th:
                    name_time = item.td.getText()
                    if len(name_time.split(" PDT by ")) == 1:
			            bug_report_time = name_time.split(" PST by ")[0]
			            bug_reporter = name_time.split(" PST by ")[1]
                    else:
			            bug_report_time = name_time.split(" PDT by ")[0]
			            bug_reporter = name_time.split(" PDT by ")[1]
                    bug.append(str(bug_report_time))
                    bug.append(str(bug_reporter))
                if "Modified" in th:
                    bug_modified_time = item.td.getText().split(" PST")[0]
                    bug.append(str(bug_modified_time))                    
                if "CC List" in th:
                    try:
                        CC_List = item.find("div",attrs={"id":"cc_edit_area"}).find("select",attrs={"id":"cc"}).findAll("option")
                        cc = ""
                        for option in CC_List:
                            cc_option = str(option.getText())
                            cc = cc + cc_option + "\n"
                    except:
                        cc = "NULL"
                    bug.append(str(cc))                    
            bug_content.append(bug)
        return bug_content
class RunProcess:
    def runPro(self,start,end,files):
        getData = GetData(start,end)
        bug_content = getData.getBug()
        mozilla = WriteFiles(files)
        writer = mozilla.writeContent(bug_content)  
if __name__ == '__main__':
    #range(400000,500000)
    start = 400000
    end = 500000
    getData = GetData(start,end)
    bug_content = getData.getBug()
    mozilla = WriteFiles(str(start)+"~"+str(end)+".csv")
    writer = mozilla.writeContent(bug_content)
    #subProcess = RunProcess()
    #Process(target=subProcess.runPro,args=(249332, 249343, "249332~249343.csv")).start()
    #Process(target=subProcess.runPro,args=(249352, 249373, "249352~249373.csv")).start()
    