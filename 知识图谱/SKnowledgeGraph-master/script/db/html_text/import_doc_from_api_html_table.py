from db.engine_factory import EngineFactory
from db.model import APIHTMLText, DocumentText, DocumentSourceRecord
from db.model_factory import KnowledgeTableFactory

session = EngineFactory.create_session()


def create_doc_text(html_text):
    doc_text = DocumentText(html_text_id=html_text.id, text=html_text.clean_text)  # text with no html tags
    return doc_text


def import_doc_from_api_html_table():
    api_html_knowledge_table = KnowledgeTableFactory.find_knowledge_table_by_name(session, APIHTMLText.__tablename__)

    # get all-version library
    html_text_list = session.query(APIHTMLText).filter_by(html_type=APIHTMLText.HTML_TYPE_API_DETAIL_DESCRIPTION).all()

    result_tuples = []
    for html_text in html_text_list:

        if DocumentSourceRecord.exist_import_record(session, api_html_knowledge_table.id, html_text.id):
            print("%d has been import to new table", (html_text.id,))
        else:
            doc_text = create_doc_text(html_text)
            doc_text.create(session, autocommit=False)
            result_tuples.append((doc_text, html_text.id))
    session.commit()
    for doc_text, html_text_id in result_tuples:
        record = DocumentSourceRecord(doc_id=doc_text.id, kg_table_id=api_html_knowledge_table.id,
                                      kg_table_primary_key=html_text_id)
        record.create(session, autocommit=False)
    session.commit()

    "complete import"


if __name__ == "__main__":
    import_doc_from_api_html_table()
