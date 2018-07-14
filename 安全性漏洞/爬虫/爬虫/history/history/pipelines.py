# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html
import MySQLdb
import MySQLdb.cursors
from twisted.enterprise import adbapi


class HistoryPipeline(object):
    charset = 'utf8',
    def __init__(self):
        self.dbpool = adbapi.ConnectionPool('MySQLdb',
            db = 'mozilla',
            user = 'root',
            passwd = '123456',
            cursorclass = MySQLdb.cursors.DictCursor,
         use_unicode = False
        )

    def process_item(self, item, spider):
        query = self.dbpool.runInteraction(self._conditional_insert, item)
        return item

    def _conditional_insert(self, tx, item):
        if item.get('bug_id'):
           # tx.execute("update bugs set history = %s where id = %s",(";".join(item["history"]),item["bug_id"]))
	        tx.execute("insert into reopen_bugs_withoutcommit (bug_id,time) values (%s,%s)",(item["bug_id"],item["history"]))
