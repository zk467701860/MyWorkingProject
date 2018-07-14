from db.engine_factory import EngineFactory
from db.model import DocumentText, DocumentAnnotationStatus
from shared.logger_util import Logger

logger = Logger("import_return_value_relation_for_jdk_method").get_log()
session = EngineFactory.create_session()


def import_jdk_document_annotation_status():
    doc_id_list = DocumentText.get_doc_id_list(session)
    if doc_id_list is not None:
        for each in doc_id_list:
            doc_id = each[0]
            print doc_id
            document_annotation_status = DocumentAnnotationStatus(doc_id, DocumentAnnotationStatus.STATUS_TO_ANNOTATE)
            print document_annotation_status
            document_annotation_status.create(session, autocommit=False)
    session.commit()


if __name__ == "__main__":
    import_jdk_document_annotation_status()