import random
from scrapy import Spider
from scrapy.selector import HtmlXPathSelector
from bugs.items import BugItem


class DmozSpider(Spider):
    name = "all"
    allowed_domains = ["bugzilla.mozilla.org"]
    start_urls = []
    _ids_ = {}

    def __init__(self, *a, **kw):
        super(DmozSpider, self).__init__(*a, **kw)
        url = "https://bugzilla.mozilla.org/show_bug.cgi?id="
        i = 0
        while i < 2000:
            id = random.randint(205000, 700000)
            if(not self._ids_.has_key(id)):
                self.start_urls.append(url+str(id))
                self._ids_[id] = 1
                i = i + 1

    def parse(self, response):
        self.log("crawl page %s" % response.url)
        hxs = HtmlXPathSelector(response)
        products = hxs.select("//div[@id='bugzilla-body']")
        items = []
        for product in products:
            bug_id = product.select("//div[@class='bz_alias_short_desc_container edit_form']//a[contains(@href,'id=')]/@href").re(r'id=(\d*)')
            summary = product.select("//div[@class='bz_alias_short_desc_container edit_form']//span[@id='short_desc_nonedit_display']/text()").extract()
            info = product.select("//table[@class = 'edit_form']")
            status = info.select("//span[contains(@id,'status')]//text()").re(r'(\w+)\W*')
            if 'DUPLICATE' in status:
                continue
            keywords = info.select("//td[@id='bz_show_bug_column_1']//tr[3]/td/text()").re(r'(.*)\n\s*')
            importance = info.select("//td[@id='bz_show_bug_column_1']//tr[11]/td/text()").re(r'--\n\s*([a-z]*).*')
            assigned = info.select("//td[@id='bz_show_bug_column_1']//tr[13]/td//text()").re(r'^:*([a-zA-Z0-9\s@\.]{2,}\b)')
            depend = info.select("//td[@id='bz_show_bug_column_1']//th[@id='field_label_dependson']/parent::*/td//a/@href").re(r'show_bug\.cgi\?id=([0-9]+)')
            block = info.select("//td[@id='bz_show_bug_column_1']//th[@id='field_label_blocked']/parent::*/td//a/@href").re(r'show_bug\.cgi\?id=([0-9]+)')
            reported_time =info.select("//td[@id='bz_show_bug_column_2']/table/tr[1]/td/text()").re('([^a-zA-Z]*)\s[a-zA-Z]+.+')
            reporter = info.select("//td[@id='bz_show_bug_column_2']/table/tr[1]/td/span//text()").re(r'^:*([a-zA-Z0-9\s@\.]{2,})')
            cc = info.select("//td[@id='bz_show_bug_column_2']/table/tr[3]/td/text()").re(r'(^[0-9]+)')
            item = BugItem()
            item['summary'] = summary
            item['bug_id'] = bug_id
            item['status'] = status
            item['keywords'] = keywords
            item['importance'] = importance
            item['assigned'] = assigned
            item['depend'] = depend
            item['block'] = block
            item['reported_time'] = reported_time
            item['reporter'] = reporter
            item['cc'] = cc
            items.append(item)
        return items
