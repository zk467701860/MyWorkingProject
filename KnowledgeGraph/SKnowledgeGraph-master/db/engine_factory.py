import MySQLdb
from sqlalchemy import create_engine, MetaData
from sqlalchemy.orm import sessionmaker

metadata = MetaData()
print metadata


class EngineFactory:
    @staticmethod
    def create_engine_to_center():
        engine = create_engine("mysql+pymysql://root:root@10.141.221.73/codehub?charset=utf8", encoding='utf-8',
                               echo=True)
        return engine

    @staticmethod
    def create_graphdata_engine_to_center():
        engine = create_engine("mysql+pymysql://root:root@10.141.221.75/graphdata?charset=utf8", encoding='utf-8',
                               echo=True)
        return engine

    @staticmethod
    def create_engine_by_schema_name(schema_name):
        if schema_name == 'stackoverflow':
            engine = create_engine("mysql+pymysql://root:root@10.131.252.160/stackoverflow?charset=utf8", encoding='utf-8',
                                   echo=True)
            return engine

    @staticmethod
    def create_session(engine=None,autocommit=False):
        if engine is None:
            engine = EngineFactory.create_engine_to_center()

        Session = sessionmaker(bind=engine, autocommit=autocommit)
        session = Session()
        return session

    @staticmethod
    def create_so_session(engine=None):
        schema_name = 'stackoverflow'
        if engine is None:
            engine = EngineFactory.create_engine_by_schema_name(schema_name)
        Session = sessionmaker(bind=engine, autocommit=True)
        session = Session()
        return session

    @staticmethod
    def create_heat_session(engine=None,autocommit=True):
        if engine is None:
            engine = EngineFactory.create_engine_to_center()
        Session = sessionmaker(bind=engine, autocommit=autocommit)
        session = Session()
        return session

    @staticmethod
    def create_log_session(engine=None):
        if engine is None:
            engine = EngineFactory.create_engine_to_center()
        Session = sessionmaker(bind=engine, autocommit=True)
        session = Session()
        return session

class ConnectionFactory:
    @staticmethod
    def create_cursor_for_jdk_importer():
        conn = MySQLdb.connect(
            host='10.131.252.160',
            port=3306,
            user='root',
            passwd='root',
            db='api_doc',
            charset="utf8"
        )
        cur = conn.cursor()
        return cur

    @staticmethod
    def create_cursor_by_knowledge_table(knowledge_table):
        conn = MySQLdb.connect(
            host=knowledge_table.ip,
            port=3306,
            user='root',
            passwd='root',
            db=knowledge_table.schema,
            charset="utf8"
        )
        cur = conn.cursor()
        return cur

    @staticmethod
    def create_cursor_for_android_importer():
        conn = MySQLdb.connect(
            host='10.141.221.75',
            port=3306,
            user='root',
            passwd='root',
            db='knowledgeGraph',
            charset="utf8"
        )
        cur = conn.cursor()
        return cur
