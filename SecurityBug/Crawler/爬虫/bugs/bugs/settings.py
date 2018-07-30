# -*- coding: utf-8 -*-

# Scrapy settings for bugs project
#
# For simplicity, this file contains only the most important settings by
# default. All the other settings are documented here:
#
#     http://doc.scrapy.org/en/latest/topics/settings.html
#

BOT_NAME = 'bugs'

SPIDER_MODULES = ['bugs.spiders']
NEWSPIDER_MODULE = 'bugs.spiders'
ITEM_PIPELINES = ['bugs.pipelines.BugsPipeline']
# Crawl responsibly by identifying yourself (and your website) on the user-agent
#USER_AGENT = 'bugs (+http://www.yourdomain.com)'
