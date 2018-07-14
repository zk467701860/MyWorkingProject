from db.engine_factory import EngineFactory
from db.model import APIHTMLText
from db.util.code_text_process import clean_html_text

session = EngineFactory.create_session()


def start_clean_html():
    api_html_text_list = session.query(APIHTMLText).filter_by(clean_text=None).all()
    count = 0
    step = 5000
    for api_html_text in api_html_text_list:

        api_html_text.clean_text = clean_html_text(api_html_text.html)
        count = count + 1
        if count > step:
            session.commit()
            count = 0
    session.commit()


if __name__ == "__main__":
    start_clean_html()
