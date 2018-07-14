# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html
import MySQLdb
import MySQLdb.cursors
from twisted.enterprise import adbapi


class SecurityMozillaPipeline(object):
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
             tx.execute("insert into security_bugs(id, summary, assigned_to, creation_time, depends_on, keywords, status, severity, blocks, reporter, cc) "
                        "values(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
                        (' '.join(item['bug_id']), " ".join(item['summary'])," ".join(item['assigned'])," ".join(item['reported_time'])," ".join(item['depend'])
                         ," ".join(item['keywords'])," ".join(item['status'])," ".join(item['importance'])," ".join(item['block'])," ".join(item['reporter'])," ".join(item['cc'])))