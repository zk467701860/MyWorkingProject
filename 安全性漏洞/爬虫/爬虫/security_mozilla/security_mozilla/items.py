# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy


class BugItem(scrapy.Item):
    # define the fields for your item here like:
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


class IdItem(scrapy.Item):
    bug_id = scrapy.Field()

class ReopenedBugItem(scrapy.Item):
    bug_id = scrapy.Field()
    assigned = scrapy.Field()
    summary = scrapy.Field()
    changed = scrapy.Field()