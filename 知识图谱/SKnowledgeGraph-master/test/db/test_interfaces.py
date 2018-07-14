from unittest import TestCase

from db.engine_factory import EngineFactory
from db.model import DocumentAnnotationStatus, DocumentSentenceTextAnnotation


class Test_interfaces(TestCase):
    def test_get_unfinished_doc_list(self):
        session = EngineFactory.create_session()
        unfinished_doc_list = DocumentAnnotationStatus.get_unfinished_doc_list(session=session)
        print unfinished_doc_list
        result = []
        for each in unfinished_doc_list:
            result.append(each[0])
        expected = [i for i in range(1, 94894)]
        self.assertEqual(expected, result)

    def test_get_annotation_by_index(self):
        session = EngineFactory.create_session()
        annotation = DocumentSentenceTextAnnotation.get_annotation_by_index(session, 1, 1)
        expected = -1
        print annotation
        self.assertEqual(expected, annotation)