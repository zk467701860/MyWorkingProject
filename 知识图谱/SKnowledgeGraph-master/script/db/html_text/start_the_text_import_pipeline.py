from script.db.html_text.import_doc_from_api_html_table import import_doc_from_api_html_table
from script.db.html_text.import_sentence_from_doc_table import import_sentence_from_doc_table
from script.db.html_text.start_clean_html_text import start_clean_html

if __name__ == "__main__":
    start_clean_html()
    import_doc_from_api_html_table()
    import_sentence_from_doc_table()
