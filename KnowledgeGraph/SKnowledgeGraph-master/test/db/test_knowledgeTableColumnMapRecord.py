from unittest import TestCase

from db.engine_factory import EngineFactory
from db.model import KnowledgeTableColumnMapRecord
from db.model_factory import KnowledgeTableFactory


class TestKnowledgeTableColumnMapRecord(TestCase):
    def test_exist_import_record(self):
        session = EngineFactory.create_session()
        jdk_method_knowledge_table = KnowledgeTableFactory.get_jdk_method_table(session)
        api_relation_table = KnowledgeTableFactory.get_api_relation_table(session)

        api_knowledge_table = KnowledgeTableFactory.get_api_entity_table(session)

        result= KnowledgeTableColumnMapRecord.exist_import_record(session, jdk_method_knowledge_table, api_relation_table,
                                                             1,
                                                             "class_id")
        print result

        self.assertEqual(result,True)