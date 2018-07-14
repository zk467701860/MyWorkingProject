from scrapy.contrib.spiders import CrawlSpider, Rule
from scrapy.contrib.linkextractors.sgml import SgmlLinkExtractor
from scrapy.selector import HtmlXPathSelector

from security_mozilla.items import BugItem


class MySpider(CrawlSpider):
    name = 'security'
    allowed_domains = ['www.mozilla.org','bugzilla.mozilla.org']
    start_urls = ['https://www.mozilla.org/en-US/security/advisories/',]

    rules = [Rule(SgmlLinkExtractor(allow=('/en-US/security/advisories/mfsa[^/]+/$')), follow=True),
             Rule(SgmlLinkExtractor(allow=('/show_bug.+$')),callback="parse_item", follow=False ),]

    def parse_item(self, response):
         self.log("crawl page %s" % response.url)
         hxs = HtmlXPathSelector(response)
         products = hxs.select("//div[@id='bugzilla-body']")
         items = []
         for product in products:
             bug_id = product.select("//div[@class='bz_alias_short_desc_container edit_form']//a[contains(@href,'id=')]/@href").re(r'id=(\d*)')
             summry = product.select("//div[@class='bz_alias_short_desc_container edit_form']//span[@id='short_desc_nonedit_display']/text()").extract()
             info = product.select("//table[@class = 'edit_form']")
             status = info.select("//span[contains(@id,'status')]//text()").re(r'(\w+)\W*')
             keywords = info.select("//td[@id='bz_show_bug_column_1']//tr[3]/td/text()").re(r'(.*)\n\s*')
             importance = info.select("//td[@id='bz_show_bug_column_1']//tr[11]/td/text()").re(r'--\n\s*([a-z]*).*')
             assigned = info.select("//td[@id='bz_show_bug_column_1']//tr[13]/td//text()").re(r'^:*([a-zA-Z0-9\s@\.]{2,}\b)')
             depend = info.select("//td[@id='bz_show_bug_column_1']//th[@id='field_label_dependson']/parent::*/td//a/@href").re(r'show_bug\.cgi\?id=([0-9]+)')
             block = info.select("//td[@id='bz_show_bug_column_1']//th[@id='field_label_blocked']/parent::*/td//a/@href").re(r'show_bug\.cgi\?id=([0-9]+)')
             reported_time =info.select("//td[@id='bz_show_bug_column_2']/table/tr[1]/td/text()").re('([^a-zA-Z]*)\s[a-zA-Z]+.+')
             reporter = info.select("//td[@id='bz_show_bug_column_2']/table/tr[1]/td/span//text()").re(r'^:*([a-zA-Z0-9\s@\.]{2,})')
             cc = info.select("//td[@id='bz_show_bug_column_2']/table/tr[3]/td/text()").re(r'(^[0-9]+)')
             item = BugItem()
             item['summary'] = summry
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


