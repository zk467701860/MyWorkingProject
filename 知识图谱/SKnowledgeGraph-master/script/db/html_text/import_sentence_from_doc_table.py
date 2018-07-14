import nltk

from db.engine_factory import EngineFactory
from db.model import DocumentText, DocumentSentenceText

session = EngineFactory.create_session()


def import_sentence_from_doc_table():
    tokenizer = nltk.data.load('tokenizers/punkt/english.pickle')

    # get all-version library
    document_text_list = session.query(DocumentText).filter_by().all()

    for doc_text in document_text_list:
        text = doc_text.text.strip()
        sentences = tokenizer.tokenize(text)
        for sentence_index,sentence  in enumerate(sentences):

            if DocumentSentenceText.exist_import_record(session, doc_id=doc_text.id, sentence_index=sentence_index):
                print("doc_id=%d sentence index=%d has been import to new table", (doc_text.id, sentence_index))
            else:
                doc_sentence_text = DocumentSentenceText(doc_id=doc_text.id, sentence_index=sentence_index,
                                                         text=sentence)
                doc_sentence_text.create(session, autocommit=False)
    session.commit()
    "complete import"


if __name__ == "__main__":
    import_sentence_from_doc_table()
