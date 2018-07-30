# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class BugItem(scrapy.Item):
    summary = scrapy.Field()
    bug_id = scrapy.Field()
    status = scrapy.Field()
    keywords = scrapy.Field()
    importance = scrapy.Field()
    assigned = scrapy.Field()
    depend = scrapy.Field()
    block = scrapy.Field()
    reported_time = scrapy.Field()
    reporter = scrapy.Field()
    cc = scrapy.Field()