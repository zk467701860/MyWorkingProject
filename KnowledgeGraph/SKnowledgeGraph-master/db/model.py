import traceback
from datetime import datetime

from sqlalchemy import Column, Integer, String, Text, MetaData, ForeignKey, DateTime, Index, Boolean, func, Table, \
    SmallInteger
from sqlalchemy import text
from sqlalchemy.dialects.mysql import LONGTEXT
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship

from engine_factory import EngineFactory
from sqlalchemy_fulltext import FullText

Base = declarative_base()


class Log(Base):
    __tablename__ = 'logs'
    id = Column(Integer, primary_key=True)  # auto incrementing
    logger = Column(String(256))  # the name of the logger.
    level = Column(String(50))  # info, debug, or error?
    trace = Column(LONGTEXT())  # the full traceback printout
    msg = Column(LONGTEXT())  # any custom log you may have included
    path = Column(String(64))
    ip = Column(String(32))
    method = Column(String(64))
    created_at = Column(DateTime, default=func.now())  # the current timestamp

    def __init__(self, logger=None, level=None, trace=None, msg=None, path=None, ip=None, method=None):
        self.logger = logger
        self.level = level
        self.trace = trace
        self.msg = msg
        self.path = path
        self.ip = ip
        self.method = method

    def __unicode__(self):
        return self.__repr__()

    def __repr__(self):
        return "<Log: %s - %s>" % (self.created_at.strftime('%m/%d/%Y-%H:%M:%S'), self.msg[:50])


class WikiDataProperty(Base):
    __tablename__ = 'wikidata_property'
    id = Column(Integer, primary_key=True, autoincrement=True)
    wd_item_id = Column(String(256), nullable=False, unique=True)
    property_name = Column(String(512), nullable=False)
    data_json = Column(LONGTEXT())

    __table_args__ = ({
        "mysql_charset": "utf8",
    })

    def __init__(self, wd_item_id, property_name, data_json):
        self.wd_item_id = wd_item_id
        self.property_name = property_name
        self.data_json = data_json

    def find_or_create(self, session, autocommit=True):
        remote_instance = self.get_remote_object(session)
        if not remote_instance:
            session.add(self)
            if autocommit:
                session.commit()
            return self
        else:
            return remote_instance

    def get_remote_object(self, session):
        if self.id:
            return self
        else:
            try:
                return session.query(WikiDataProperty).filter(WikiDataProperty.wd_item_id == self.wd_item_id).first()
            except Exception:
                traceback.print_exc()
                return None

    @staticmethod
    def get_property_by_wd_item_id(session, wd_item_id):
        try:
            property_object = session.query(WikiDataProperty).filter(WikiDataProperty.wd_item_id == wd_item_id).first()
            return property_object
        except Exception:
            traceback.print_exc()
            return None


class EntityHeat(Base):
    __tablename__ = 'api_heat'
    id = Column(Integer, primary_key=True, autoincrement=True)
    heat = Column(Integer, index=True)
    api_id = Column(Integer, nullable=False, index=True)

    def __init__(self, heat, api_id):
        self.heat = heat
        self.api_id = api_id

    def __unicode__(self):
        return self.__repr__()

    def __repr__(self):
        return '<APIHeat: %r: api_id=%s >' % (self.heat, self.api_id)


class APIRelation(Base):
    RELATION_TYPE_BELONG_TO = 1
    RELATION_TYPE_EXTENDS = 2
    RELATION_TYPE_IMPLEMENTS = 3
    RELATION_TYPE_SEE_ALSO = 4
    RELATION_TYPE_THROW_EXCEPTION = 5
    RELATION_TYPE_RETURN_VALUE = 6

    __tablename__ = 'java_api_relation'
    id = Column(Integer, primary_key=True, autoincrement=True)
    start_api_id = Column(Integer, ForeignKey('java_all_api_entity.id'), nullable=False, index=True)
    end_api_id = Column(Integer, ForeignKey('java_all_api_entity.id'), nullable=False, index=True)
    relation_type = Column(Integer, index=True)

    __table_args__ = (Index('unique_index', start_api_id, end_api_id, relation_type),
                      Index('all_relation_index', start_api_id, end_api_id),
                      {
                          "mysql_charset": "utf8",
                      })

    def __init__(self, start_api_id, end_api_id, relation_type):
        self.start_api_id = start_api_id
        self.end_api_id = end_api_id
        self.relation_type = relation_type

    def exist_in_remote(self, session):
        try:
            if session.query(APIRelation.id).filter_by(start_api_id=self.start_api_id,
                                                       end_api_id=self.end_api_id,
                                                       relation_type=self.relation_type).first():
                return True
            else:
                return False
        except Exception:
            traceback.print_exc()
            return False

    def get_remote_object(self, session):
        if self.id:
            return self
        else:
            try:
                return session.query(APIRelation).filter_by(start_api_id=self.start_api_id,
                                                            end_api_id=self.end_api_id,
                                                            relation_type=self.relation_type).first()
            except Exception:
                # traceback.print_exc()
                return None

    def find_or_create(self, session, autocommit=True):
        remote_instance = self.get_remote_object(session)
        if not remote_instance:
            session.add(self)
            if autocommit:
                session.commit()
            return self
        else:
            return remote_instance

    def __repr__(self):
        return '<APIRelation: %r-%r: type=%r >' % (self.start_api_id, self.end_api_id, self.relation_type)


has_alias_table = Table('has_alias', Base.metadata,
                        Column('api_id', Integer, ForeignKey('java_all_api_entity.id')),
                        Column('alias_id', Integer, ForeignKey('java_api_alias.id'))
                        )


class APIAlias(Base, FullText):
    __tablename__ = 'java_api_alias'
    __fulltext_columns__ = ('alias',)

    id = Column(Integer, primary_key=True, autoincrement=True)
    alias = Column(String(1024), nullable=False, index=True)
    type = Column(Integer, nullable=True, index=True)

    all_apis = relationship(
        "APIEntity",
        secondary=has_alias_table,
        back_populates="all_aliases")

    ALIAS_TYPE_QUALIFIER_NAME = 1

    # all api only with simple name, for example android.view.Button -> Button
    ALIAS_TYPE_SIMPLE_NAME = 2
    # all api only with api type + simple name, for example android.view.Button -> class Button
    ALIAS_TYPE_SIMPLE_NAME_WITH_TYPE = 3
    # only for method. qualifier type. etc. append(java.lang.Object)
    ALIAS_TYPE_SIMPLE_METHOD_WITH_QUALIFIER_PARAMETER_TYPE = 4
    ALIAS_TYPE_SIMPLE_CLASS_NAME_METHOD_WITH_QUALIFIER_PARAMETER_TYPE = 5
    ALIAS_TYPE_SIMPLE_NAME_METHOD_WITH_SIMPLE_PARAMETER_TYPE = 6
    ALIAS_TYPE_SIMPLE_CLASS_NAME_METHOD_WITH_SIMPLE_PARAMETER_TYPE = 7

    # for field and other,javax.xml.transform.OutputKeys.DOCTYPE_SYSTEM->OutputKeys.DOCTYPE_SYSTEM, save the last two
    ALIAS_TYPE_SIMPLE_PARENT_API_NAME_WITH_SIMPLE_NAME = 8
    # @nullable
    ALIAS_TYPE_ANNOTATION_REFERENCE = 9

    ALIAS_TYPE_CAMEL_CASE_TO_SPACE = 10
    ALIAS_TYPE_UNDERLINE_TO_SPACE = 11

    __table_args__ = (Index('alias_type_index', alias, type), {
        "mysql_charset": "utf8",
    })

    def __init__(self, alias, type):
        self.alias = alias
        self.type = type

    def find_or_create(self, session, autocommit=True):
        remote_instance = self.get_remote_object(session)
        if not remote_instance:
            session.add(self)
            if autocommit:
                session.commit()
            return self
        else:
            return remote_instance

    def get_remote_object(self, session):
        if self.id:
            return self
        else:
            try:
                return session.query(APIAlias).filter(APIAlias.alias == func.binary(self.alias),
                                                      APIAlias.type == self.type,
                                                      ).first()
            except Exception:
                traceback.print_exc()
                return None

    @staticmethod
    def aliases_to_apis(aliases):
        api_entity_set = set()

        for alias in aliases:
            for api in alias.all_apis:
                api_entity_set.add(api)
        return api_entity_set

    def __eq__(self, other):
        if isinstance(other, APIAlias):
            return self.id == other.id
        else:
            return False

    def __hash__(self):
        return hash(self.id)

    def __repr__(self):
        return '<APIAlias: id=%r alias=%r type=%r >' % (self.id, self.alias, self.type)


class APIDocumentWebsite(Base):
    __tablename__ = 'java_api_document_website'
    id = Column(Integer, primary_key=True, autoincrement=True)
    api_id = Column(Integer, ForeignKey('java_all_api_entity.id'), nullable=False)
    document_website = Column(String(512), nullable=False)
    __table_args__ = (Index('api_document_website_index', api_id, document_website), {
        "mysql_charset": "utf8",
    })

    def __init__(self, api_id, document_website):
        self.api_id = api_id
        self.document_website = document_website

    def find_or_create(self, session, autocommit=True):
        remote_instance = self.get_remote_object(session)
        if not remote_instance:
            session.add(self)
            if autocommit:
                session.commit()
            return self
        else:
            return remote_instance

    def get_remote_object(self, session):
        if self.id:
            return self
        else:
            try:
                return session.query(APIDocumentWebsite).filter(APIDocumentWebsite.api_id == self.api_id,
                                                                APIDocumentWebsite.document_website == func.binary(
                                                                    self.document_website)).first()
            except Exception:
                traceback.print_exc()
                return None

    @staticmethod
    def get_api_id_by_website(session, website):
        try:
            api_id = session.query(APIDocumentWebsite.api_id).filter_by(document_website=website).scalar()
            return api_id
        except Exception:
            traceback.print_exc()
            return None


class APIEntity(Base):
    API_TYPE_ALL_API_ENTITY = -1

    API_TYPE_UNKNOWN = 0
    API_TYPE_PACKAGE = 1
    API_TYPE_CLASS = 2
    API_TYPE_INTERFACE = 3
    API_TYPE_EXCEPTION = 4
    API_TYPE_ERROR = 5
    API_TYPE_FIELD = 6
    API_TYPE_CONSTRUCTOR = 7
    API_TYPE_ENUM_CLASS = 8
    API_TYPE_ANNOTATION = 9
    API_TYPE_XML_ATTRIBUTE = 10
    API_TYPE_METHOD = 11
    API_TYPE_ENUM_CONSTANTS = 12
    API_TYPE_PRIMARY_TYPE = 13
    __tablename__ = 'java_all_api_entity'
    id = Column(Integer, primary_key=True, autoincrement=True)
    api_type = Column(Integer, default=API_TYPE_UNKNOWN, index=True)
    qualified_name = Column(String(1024), index=True)
    full_declaration = Column(String(1024), nullable=True, index=True)
    short_description = Column(Text(), nullable=True)
    added_in_version = Column(String(128), nullable=True)
    document_websites = relationship("APIDocumentWebsite", foreign_keys=[APIDocumentWebsite.api_id], backref="api")

    out_relation = relationship('APIRelation', foreign_keys=[APIRelation.start_api_id],
                                backref='start_api')
    in_relation = relationship('APIRelation', foreign_keys=[APIRelation.end_api_id],
                               backref='end_api')

    all_aliases = relationship(
        "APIAlias",
        secondary=has_alias_table,
        back_populates="all_apis")

    __table_args__ = {
        "mysql_charset": "utf8"
    }

    def __init__(self, qualified_name, api_type, full_declaration=None, short_description=None, added_in_version=None):
        self.api_type = api_type
        self.qualified_name = qualified_name
        self.full_declaration = full_declaration
        self.short_description = short_description
        self.added_in_version = added_in_version

    def find_or_create(self, session, autocommit=True):
        remote_instance = self.get_remote_object(session)
        if not remote_instance:
            session.add(self)
            if autocommit:
                session.commit()
            return self
        else:
            return remote_instance

    def get_remote_object(self, session):
        if self.id:
            return self
        else:
            try:
                return session.query(APIEntity).filter(
                    APIEntity.qualified_name == func.binary(self.qualified_name)).first()
            except Exception:
                traceback.print_exc()
                return None

    @staticmethod
    def exist(session, qualified_name):
        try:
            if session.query(APIEntity.id).filter(APIEntity.qualified_name == func.binary(qualified_name)).first():
                return True
            else:
                return False
        except Exception:
            traceback.print_exc()
            return None

    @staticmethod
    def find_by_id(session, api_entity_id):
        try:
            return session.query(APIEntity).filter(APIEntity.id == api_entity_id).first()
        except Exception:
            return None

    @staticmethod
    def find_by_qualifier(session, qualified_name):
        try:
            return session.query(APIEntity).filter(APIEntity.qualified_name == func.binary(qualified_name)).first()
        except Exception:
            traceback.print_exc()
            return None

    @staticmethod
    def get_api_type_string(type):
        if type == APIEntity.API_TYPE_UNKNOWN:
            return []
        if type == APIEntity.API_TYPE_PACKAGE:
            return ["package"]
        if type == APIEntity.API_TYPE_CLASS:
            return ["class"]
        if type == APIEntity.API_TYPE_INTERFACE:
            return ["interface", "class"]
        if type == APIEntity.API_TYPE_EXCEPTION:
            return ["exception", "class"]
        if type == APIEntity.API_TYPE_ERROR:
            return ["error", "class"]
        if type == APIEntity.API_TYPE_FIELD:
            return ["field", "constant"]
        if type == APIEntity.API_TYPE_CONSTRUCTOR:
            return ["constructor", "constructor method"]
        if type == APIEntity.API_TYPE_ENUM_CLASS:
            return ["enum", "constant", "enum class"]
        if type == APIEntity.API_TYPE_ANNOTATION:
            return ["annotation"]
        if type == APIEntity.API_TYPE_XML_ATTRIBUTE:
            return ["XML attribute", "attribute"]
        if type == APIEntity.API_TYPE_METHOD:
            return ["API", "method"]

        if type == APIEntity.API_TYPE_ENUM_CONSTANTS:
            return ["constant", "enum constant"]
        return []

    def __repr__(self):
        return '<APIEntity: id=%r name=%r>' % (self.id, self.qualified_name)

    def __eq__(self, other):
        if isinstance(other, APIEntity):
            return self.id == other.id
        else:
            return False

    def __hash__(self):
        return hash(self.id)

    @staticmethod
    def type_string_to_api_type_constant(api_type_string):
        if not api_type_string:
            return APIEntity.API_TYPE_UNKNOWN
        api_type_string = api_type_string.strip()
        if not api_type_string:
            return APIEntity.API_TYPE_UNKNOWN
        api_type_string = api_type_string.lower()
        if api_type_string == "package":
            return APIEntity.API_TYPE_PACKAGE
        if api_type_string == "class":
            return APIEntity.API_TYPE_CLASS
        if api_type_string == "interface":
            return APIEntity.API_TYPE_INTERFACE
        if api_type_string == "error":
            return APIEntity.API_TYPE_ERROR
        if api_type_string == "enum":
            return APIEntity.API_TYPE_ENUM_CLASS
        if api_type_string == "exception":
            return APIEntity.API_TYPE_EXCEPTION
        if api_type_string == "annotation type" or api_type_string == "annotation":
            return APIEntity.API_TYPE_ANNOTATION

        if api_type_string == "method":
            return APIEntity.API_TYPE_METHOD
        if api_type_string == "constructor":
            return APIEntity.API_TYPE_CONSTRUCTOR
        if api_type_string == "nested" or api_type_string == "nested class":
            return APIEntity.API_TYPE_CLASS
        if api_type_string == "required":
            return APIEntity.API_TYPE_FIELD
        if api_type_string == "optional":
            return APIEntity.API_TYPE_FIELD
        if api_type_string == "field":
            return APIEntity.API_TYPE_FIELD
        if api_type_string == "enum constant":
            return APIEntity.API_TYPE_ENUM_CONSTANTS

        return APIEntity.API_TYPE_UNKNOWN

    @staticmethod
    def api_type_belong_to_relation(api_type, subject_api_type):
        if api_type == subject_api_type:
            return True
        if subject_api_type == APIEntity.API_TYPE_METHOD:
            if api_type == APIEntity.API_TYPE_CONSTRUCTOR:
                return True

        if subject_api_type == APIEntity.API_TYPE_CLASS:
            if api_type == APIEntity.API_TYPE_INTERFACE:
                return True
            if api_type == APIEntity.API_TYPE_ERROR:
                return True
            if api_type == APIEntity.API_TYPE_ENUM_CLASS:
                return True
            if api_type == APIEntity.API_TYPE_EXCEPTION:
                return True
        if subject_api_type == APIEntity.API_TYPE_FIELD:

            if api_type == APIEntity.API_TYPE_ENUM_CONSTANTS:
                return True

        return False


class APIEntityProperty(Base):
    __tablename__ = 'java_api_property'
    id = Column(Integer, primary_key=True, autoincrement=True)
    api_id = Column(Integer, ForeignKey('java_all_api_entity.id'), nullable=False, index=True)
    property_name = Column(String(512), nullable=False, index=True)
    property_value = Column(LONGTEXT(), nullable=True)  # text with no html tags

    __table_args__ = (Index('api_id_property_name_index', api_id, property_name, unique=True), {
        "mysql_charset": "utf8",
    })

    def __init__(self, api_id, property_name, property_value):
        self.api_id = api_id
        self.property_name = property_name
        self.property_value = property_value

    def create(self, session, autocommit=True):
        session.add(self)
        if autocommit:
            session.commit()
        return self


class APIHTMLText(Base):
    __tablename__ = 'java_api_html_text'
    id = Column(Integer, primary_key=True, autoincrement=True)
    api_id = Column(Integer, ForeignKey('java_all_api_entity.id'), nullable=False)
    html = Column(LONGTEXT(), nullable=False)
    clean_text = Column(LONGTEXT(), nullable=True)  # text with no html tags
    reserve_part_tag_text = Column(LONGTEXT(), nullable=True)  # text with only code tags text
    html_type = Column(Integer, nullable=True)

    __table_args__ = (Index('api_id_text_type_index', api_id, html_type), {
        "mysql_charset": "utf8",
    })

    HTML_TYPE_UNKNOWN = 0
    HTML_TYPE_API_DECLARATION = 1
    HTML_TYPE_API_SHORT_DESCRIPTION = 2
    HTML_TYPE_API_DETAIL_DESCRIPTION = 3
    HTML_TYPE_METHOD_RETURN_VALUE_DESCRIPTION = 4

    def __init__(self, api_id, html, html_type=HTML_TYPE_UNKNOWN):
        self.api_id = api_id
        self.html = html
        self.html_type = html_type

    def create(self, session, autocommit=True):
        session.add(self)
        if autocommit:
            session.commit()
        return self


class DocumentSourceRecord(Base):
    __tablename__ = 'java_document_source_record'
    id = Column(Integer, primary_key=True, autoincrement=True)
    doc_id = Column(Integer, ForeignKey('java_api_document_text.id'), unique=True, nullable=False)
    kg_table_id = Column(Integer, nullable=False)
    kg_table_primary_key = Column(Integer, nullable=False)

    __table_args__ = (Index('source_table_index', kg_table_id, kg_table_primary_key), {
        "mysql_charset": "utf8",
    })

    def __init__(self, doc_id, kg_table_id, kg_table_primary_key):
        self.doc_id = doc_id
        self.kg_table_id = kg_table_id
        self.kg_table_primary_key = kg_table_primary_key

    def create(self, session, autocommit=True):
        session.add(self)
        if autocommit:
            session.commit()
        return self

    @staticmethod
    def exist_import_record(session, kg_table_id, kg_table_primary_key):
        """
        check if the start_row_id has map in end_knowledge_table

        """

        try:
            team = session.query(DocumentSourceRecord).filter_by(kg_table_id=kg_table_id,
                                                                 kg_table_primary_key=kg_table_primary_key).first()
            if team:
                return True
            else:
                return False
        except Exception:
            traceback.print_exc()
            return False


class DocumentText(Base):
    __tablename__ = 'java_api_document_text'
    id = Column(Integer, primary_key=True, autoincrement=True)
    html_text_id = Column(Integer, ForeignKey('java_api_html_text.id'), unique=True, nullable=False)
    text = Column(LONGTEXT(), nullable=True)  # text with no html tags

    __table_args__ = (Index('api_id_text_type_index', html_text_id), {
        "mysql_charset": "utf8",
    })

    def __init__(self, html_text_id, text):
        self.html_text_id = html_text_id
        self.text = text

    def create(self, session, autocommit=True):
        session.add(self)
        if autocommit:
            session.commit()
        return self

    @staticmethod
    def get_doc_id_list(session):
        try:
            doc_id_list = session.query(DocumentText.id).all()
            return doc_id_list
        except Exception:
            traceback.print_exc()
            return None


class DocumentSentenceText(Base):
    __tablename__ = 'java_api_document_sentence_text'
    id = Column(Integer, primary_key=True, autoincrement=True)
    doc_id = Column(Integer, ForeignKey('java_api_document_text.id'), nullable=False, index=True)
    sentence_index = Column(Integer, nullable=True)  # text with no html tags
    text = Column(Text(), nullable=True)  # text with no html tags

    __table_args__ = (Index('doc_id_sentence_index', doc_id, sentence_index), {
        "mysql_charset": "utf8",
    })

    def __init__(self, doc_id, sentence_index, text):
        self.doc_id = doc_id
        self.sentence_index = sentence_index
        self.text = text

    def create(self, session, autocommit=True):
        session.add(self)
        if autocommit:
            session.commit()
        return self

    @staticmethod
    def exist_import_record(session, doc_id, sentence_index):
        """
        check if the start_row_id has map in end_knowledge_table

        """

        try:
            team = session.query(DocumentSentenceText).filter_by(doc_id=doc_id,
                                                                 sentence_index=sentence_index).first()
            if team:
                return True
            else:
                return False
        except Exception:
            traceback.print_exc()
            return False

    @staticmethod
    def get_sentence_list_by_doc_id(session, doc_id):
        try:
            sentence_list = session.query(DocumentSentenceText).filter_by(doc_id=doc_id).all()
            return sentence_list
        except Exception:
            traceback.print_exc()
            return None


class DocumentAnnotationStatus(Base):
    __tablename__ = "java_api_document_annotation_status"
    id = Column(Integer, primary_key=True, autoincrement=True)
    doc_id = Column(Integer, ForeignKey('java_api_document_text.id'), nullable=False, index=True)
    status = Column(Integer, nullable=False, index=True, default=0)

    STATUS_TO_ANNOTATE = 0  # not start yet
    STATUS_UNFINISHED = 1  # begin to annotate but not completed
    STATUS_ANNOTATED = 2  # have been annotated
    STATUS_SB_DOING = 3  # somebody is annotating now

    def __init__(self, doc_id, status):
        self.doc_id = doc_id
        self.status = status

    def create(self, session, autocommit=True):
        session.add(self)
        if autocommit:
            session.commit()
        return self

    @staticmethod
    def exist_import_record(session, doc_id):
        """
        check if the start_row_id has map in end_knowledge_table
        """
        try:
            team = session.query(DocumentSentenceText).filter_by(doc_id=doc_id).first()
            if team:
                return True
            else:
                return False
        except Exception:
            traceback.print_exc()
            return False

    @staticmethod
    def get_unfinished_doc_list(session):
        try:
            unfinished_doc_list = session.query(DocumentAnnotationStatus.doc_id).filter(DocumentAnnotationStatus.status.in_([0, 1])).all()
            return unfinished_doc_list
        except Exception:
            traceback.print_exc()
            return None


class DocumentSentenceTextAnnotation(Base):
    __tablename__ = 'java_api_document_sentence_text_annotation'
    id = Column(Integer, primary_key=True, autoincrement=True)
    doc_id = Column(Integer, ForeignKey('java_api_document_text.id'), nullable=False, index=True)
    sentence_index = Column(Integer, nullable=True)  # text with no html tags
    text = Column(Text(), nullable=True)  # text with no html tags
    type = Column(Integer, nullable=True, index=True)  # annotated type

    __table_args__ = (Index('doc_id_sentence_index', doc_id, sentence_index), {
        "mysql_charset": "utf8",
    })

    ANNOTATED_TYPE_FUNCTIONALITY = 1
    ANNOTATED_TYPE_DIRECTIVE = 2
    ANNOTATED_TYPE_OTHERS = 0

    def __init__(self, doc_id, sentence_index, text, type):
        self.doc_id = doc_id
        self.sentence_index = sentence_index
        self.text = text
        self.type = type

    def create(self, session, autocommit=True):
        session.add(self)
        if autocommit:
            session.commit()
        return self

    @staticmethod
    def exist_import_record(session, doc_id, sentence_index):
        """
        check if the start_row_id has map in end_knowledge_table
        """
        try:
            team = session.query(DocumentSentenceText).filter_by(doc_id=doc_id,
                                                                 sentence_index=sentence_index).first()
            if team:
                return True
            else:
                return False
        except Exception:
            traceback.print_exc()
            return False

    @staticmethod
    def get_annotation_by_index(session, doc_id, sentence_index):
        try:
            annotation_type = session.query(DocumentSentenceTextAnnotation.type).filter_by(doc_id=doc_id, sentence_index=sentence_index).scalar()
            if annotation_type:
                return annotation_type
            else:
                return -1
        except Exception:
            traceback.print_exc()
            return -1


class APIInstanceEntityRelation(Base):
    __tablename__ = 'java_api_value_instance_entity_relation'
    id = Column(Integer, primary_key=True, autoincrement=True)
    start_instance_id = Column(Integer, ForeignKey('java_api_value_instance_entity.id'), nullable=False, index=True)
    end_instance_id = Column(Integer, ForeignKey('java_api_value_instance_entity.id'), nullable=False, index=True)
    relation_type = Column(Integer, index=True)

    __table_args__ = (Index('unique_index', start_instance_id, end_instance_id, relation_type),
                      Index('all_relation_index', start_instance_id, end_instance_id),
                      {
                          "mysql_charset": "utf8",
                      })

    def __init__(self, start_instance_id, end_instance_id, relation_type):
        self.start_instance_id = start_instance_id
        self.end_instance_id = end_instance_id
        self.relation_type = relation_type

    def exist_in_remote(self, session):
        try:
            if session.query(APIInstanceEntityRelation.id).filter_by(start_instance_id=self.start_instance_id,
                                                                     end_instance_id=self.end_instance_id,
                                                                     relation_type=self.relation_type).first():
                return True
            else:
                return False
        except Exception:
            traceback.print_exc()
            return False

    def get_remote_object(self, session):
        if self.id:
            return self
        else:
            try:
                return session.query(APIInstanceEntityRelation).filter_by(start_instance_id=self.start_instance_id,
                                                                          end_instance_id=self.end_instance_id,
                                                                          relation_type=self.relation_type).first()
            except Exception:
                # traceback.print_exc()
                return None

    def find_or_create(self, session, autocommit=True):
        remote_instance = self.get_remote_object(session)
        if not remote_instance:
            session.add(self)
            if autocommit:
                session.commit()
            return self
        else:
            return remote_instance

    def __repr__(self):
        return '<APIInstanceEntityRelation: %r-%r: type=%r >' % (
            self.start_instance_id, self.end_instance_id, self.relation_type)


class APIInstanceEntity(Base):
    TYPE_UNKNOWN = 0
    TYPE_RETURN_VALUE = 1
    TYPE_PARAMETER = 2

    __tablename__ = 'java_api_value_instance_entity'
    id = Column(Integer, primary_key=True, autoincrement=True)
    instance_type = Column(Integer, default=TYPE_UNKNOWN, index=True)
    simple_type = Column(String(1024), index=True)
    qualified_type = Column(String(1024), index=True)
    formal_parameter_name = Column(String(1024), nullable=True, index=True)
    qualified_full_name = Column(String(1024), index=True)
    simple_full_name = Column(String(1024), index=True)
    short_description = Column(Text(1024), nullable=True, index=True)
    out_relation = relationship('APIInstanceEntityRelation', foreign_keys=[APIInstanceEntityRelation.start_instance_id],
                                backref='start_api_instance')
    in_relation = relationship('APIInstanceEntityRelation', foreign_keys=[APIInstanceEntityRelation.end_instance_id],
                               backref='end_api_instance')

    __table_args__ = {
        "mysql_charset": "utf8"
    }

    def __init__(self, simple_type, qualified_type, formal_parameter_name, qualified_full_name, simple_full_name):
        self.simple_type = simple_type
        self.qualified_type = qualified_type
        self.formal_parameter_name = formal_parameter_name
        self.qualified_full_name = qualified_full_name
        self.simple_full_name = simple_full_name

    def find_or_create(self, session, autocommit=True):
        remote_instance = self.get_remote_object(session)
        if not remote_instance:
            session.add(self)
            if autocommit:
                session.commit()
            return self
        else:
            return remote_instance

    def get_remote_object(self, session):
        if self.id:
            return self
        else:
            try:
                return session.query(APIInstanceEntity).filter(
                    APIInstanceEntity.qualified_full_name == func.binary(self.qualified_full_name)).first()
            except Exception:
                traceback.print_exc()
                return None

    def __repr__(self):
        return '<APIInstanceEntity: id=%r qualified_full_name=%r>' % (self.id, self.qualified_full_name)

    def __hash__(self):
        return hash(self.id)


class APIInstanceToAPIEntityRelation(Base):
    DIRECTION_INSTANCE_TO_API = 0
    DIRECTION_API_TO_INSTANCE = 1

    RELATION_TYPE_TYPE_OF = 1
    RELATION_TYPE_HAS_PARAMETER = 2
    RELATION_TYPE_RETURN = 3

    __tablename__ = 'instance_entity_to_api_relation'
    id = Column(Integer, primary_key=True, autoincrement=True)
    instance_entity_id = Column(Integer, ForeignKey('java_api_value_instance_entity.id'), nullable=False, index=True)
    api_id = Column(Integer, ForeignKey('java_all_api_entity.id'), nullable=False, index=True)
    relation_type = Column(Integer, index=True)
    relation_direction = Column(Integer, default=DIRECTION_INSTANCE_TO_API, index=True)

    __table_args__ = (Index('unique_index', instance_entity_id, api_id, relation_type, relation_direction),
                      Index('all_relation_index', instance_entity_id, api_id),
                      {
                          "mysql_charset": "utf8",
                      })

    def __init__(self, start_instance_id, end_instance_id, relation_type):
        self.start_instance_id = start_instance_id
        self.end_instance_id = end_instance_id
        self.relation_type = relation_type

    def exist_in_remote(self, session):
        try:
            if session.query(APIInstanceEntityRelation.id).filter_by(start_instance_id=self.start_instance_id,
                                                                     end_instance_id=self.end_instance_id,
                                                                     relation_type=self.relation_type).first():
                return True
            else:
                return False
        except Exception:
            traceback.print_exc()
            return False

    def get_remote_object(self, session):
        if self.id:
            return self
        else:
            try:
                return session.query(APIInstanceEntityRelation).filter_by(start_instance_id=self.start_instance_id,
                                                                          end_instance_id=self.end_instance_id,
                                                                          relation_type=self.relation_type).first()
            except Exception:
                # traceback.print_exc()
                return None

    def find_or_create(self, session, autocommit=True):
        remote_instance = self.get_remote_object(session)
        if not remote_instance:
            session.add(self)
            if autocommit:
                session.commit()
            return self
        else:
            return remote_instance

    def __repr__(self):
        return '<APIInstanceEntityRelation: %r-%r: type=%r >' % (
            self.start_instance_id, self.end_instance_id, self.relation_type)


class LibraryEntity(Base):
    __tablename__ = 'library_entity'
    id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String(256), index=True)
    version = Column(String(128), nullable=True)
    short_description = Column(Text(), nullable=True)
    url = Column(String(512), nullable=True, index=True)

    __table_args__ = (Index('name_url_index', "name", "url"), {
        "mysql_charset": "utf8",
    })

    def __init__(self, name, version, short_description, url):
        self.name = name
        self.version = version
        self.short_description = short_description
        self.url = url

    def find_or_create(self, session, autocommit=True):
        remote_instance = self.get_remote_object(session)
        if not remote_instance:
            session.add(self)
            if autocommit:
                session.commit()
            return self
        else:
            return remote_instance

    def get_remote_object(self, session):
        if self.id:
            return self
        else:
            try:
                return session.query(LibraryEntity).filter_by(name=self.name, url=self.url).first()
            except Exception:
                # traceback.print_exc()
                return None


class APIBelongToLibraryRelation(Base):
    __tablename__ = 'api_belong_to_library_relation'
    id = Column(Integer, primary_key=True, autoincrement=True)
    api_id = Column(Integer, ForeignKey('java_all_api_entity.id'), nullable=False)
    library_id = Column(Integer, ForeignKey('library_entity.id'), nullable=False)
    __table_args__ = (Index('belong_to_index', api_id, library_id), {
        "mysql_charset": "utf8",
    })

    def __init__(self, api_id, library_id):
        self.api_id = api_id
        self.library_id = library_id

    def find_or_create(self, session, autocommit=True):
        remote_instance = self.get_remote_object(session)
        if not remote_instance:
            session.add(self)
            if autocommit:
                session.commit()
            return self
        else:
            return remote_instance

    def get_remote_object(self, session):
        if self.id:
            return self
        else:
            try:
                return session.query(APIBelongToLibraryRelation).filter_by(api_id=self.api_id,
                                                                           library_id=self.library_id).first()
            except Exception:
                traceback.print_exc()
                return None


class KnowledgeTable(Base):
    __tablename__ = 'knowledge_table'
    id = Column(Integer, primary_key=True, autoincrement=True)
    ip = Column(String(30), nullable=False)
    schema = Column(String(128), nullable=False)
    table_name = Column(String(128), nullable=False, index=True)
    description = Column(Text(), nullable=True)
    create_time = Column(DateTime(), nullable=True)

    __table_args__ = {
        "mysql_charset": "utf8"
    }

    def __init__(self, ip, schema, table_name, description):
        self.ip = ip
        self.schema = schema
        self.table_name = table_name
        self.description = description
        self.create_time = datetime.now()

    def find_or_create(self, session, autocommit=True):
        remote_instance = self.get_remote_object(session)
        if not remote_instance:
            session.add(self)
            if autocommit:
                session.commit()
            return self
        else:
            return remote_instance

    def get_remote_object(self, session):
        if self.id:
            return self
        else:
            try:
                return session.query(KnowledgeTable).filter_by(ip=self.ip, schema=self.schema,
                                                               table_name=self.table_name).one()
            except Exception:
                # traceback.print_exc()
                return None


class KnowledgeTableRowMapRecord(Base):
    __tablename__ = 'knowledge_table_row_map'
    id = Column(Integer, primary_key=True, autoincrement=True)
    start_table_id = Column(Integer, ForeignKey('knowledge_table.id'), nullable=False)
    end_table_id = Column(Integer, ForeignKey('knowledge_table.id'), nullable=False)
    start_row_id = Column(Integer, nullable=False, index=True)
    end_row_id = Column(Integer, nullable=False, index=True)
    valid = Column(Boolean, nullable=False, index=True, default=True)
    create_time = Column(DateTime(), nullable=False, index=True)

    __table_args__ = (Index('start_id_index', "start_table_id", "end_table_id", start_row_id),
                      Index('end_id_index', "start_table_id", "end_table_id", end_row_id), {
                          "mysql_charset": "utf8",
                      })

    def __init__(self, start_knowledge_table, end_knowledge_table, start_row_id, end_row_id):
        self.start_table_id = start_knowledge_table.id
        self.end_table_id = end_knowledge_table.id
        self.start_row_id = start_row_id
        self.end_row_id = end_row_id
        self.create_time = datetime.now()

    def create(self, session, autocommit=True):
        session.add(self)
        if autocommit:
            session.commit()
        return self

    @staticmethod
    def exist_import_record(session, start_knowledge_table, end_knowledge_table, start_row_id):
        """
        check if the start_row_id has map in end_knowledge_table
        :param session:
        :param start_knowledge_table:
        :param end_knowledge_table:
        :param start_row_id:
        :return:
        """

        try:
            team = session.query(KnowledgeTableRowMapRecord).filter_by(start_table_id=start_knowledge_table.id,
                                                                       end_table_id=end_knowledge_table.id,
                                                                       start_row_id=start_row_id).first()
            if team:
                return True
            else:
                return False
        except Exception:
            # traceback.print_exc()
            return False

    @staticmethod
    def get_end_row_id(session, start_knowledge_table, end_knowledge_table, start_row_id):
        """
        check if the start_row_id has map in end_knowledge_table
        :param session:
        :param start_knowledge_table:
        :param end_knowledge_table:
        :param start_row_id:
        :return:
        """

        try:
            end_row_id = session.query(KnowledgeTableRowMapRecord.end_row_id).filter_by(
                start_table_id=start_knowledge_table.id,
                end_table_id=end_knowledge_table.id,
                start_row_id=start_row_id).scalar()
            return end_row_id
        except Exception:
            traceback.print_exc()
            return None

    @staticmethod
    def get_transformed_table_data(session, start_knowledge_table, end_knowledge_table):
        try:
            data_list = session.query(KnowledgeTableRowMapRecord).filter_by(
                start_table_id=start_knowledge_table.id,
                end_table_id=end_knowledge_table.id).all()
            return data_list
        except Exception:
            traceback.print_exc()
            return None


class PostsRecord(Base, FullText):
    __tablename__ = 'posts'
    __fulltext_columns__ = ('title',)

    id = Column(Integer, primary_key=True, autoincrement=True, name="Id")
    post_type_id = Column(SmallInteger, name="PostTypeId")
    accepted_answer_id = Column(Integer, name="AcceptedAnswerId")
    parent_id = Column(Integer, name="ParentId")
    score = Column(Integer, name="Score")
    view_count = Column(Integer, name="ViewCount")
    body = Column(Text(), name="Body")
    owner_user_id = Column(Integer, name="OwnerUserId")
    owner_display_name = Column(String(256), name="OwnerDisplayName")
    last_editor_user_id = Column(Integer, name="LastEditorUserId")
    last_edit_date = Column(DateTime(), name="LastEditDate")
    last_activity_date = Column(DateTime(), name="LastActivityDate")
    title = Column(String(256), name="Title")
    tags = Column(String(256), name="Tags")
    answer_count = Column(Integer, name="AnswerCount")
    comment_count = Column(Integer, name="CommentCount")
    favorite_count = Column(Integer, name="FavoriteCount")
    creation_date = Column(DateTime(), name="CreationDate")

    __table_args__ = ({
        "mysql_charset": "utf8",
    })

    def __init__(self, session=None):
        self.session = session
        if self.session is None:
            self.session = self.get_so_session()

    def get_so_session(self):
        if not self.session:
            self.session = EngineFactory.create_so_session()

        return self.session

    def get_post_by_id(self, id_num):
        post_id_node = self.session.query(PostsRecord).get(id_num)

        post = {
            "id": post_id_node.id,
            "post_type_id": post_id_node.post_type_id,
            "accepted_answer_id": post_id_node.accepted_answer_id,
            "parent_id": post_id_node.parent_id,
            "score": post_id_node.score,
            "body": post_id_node.body,
            "owner_user_id": post_id_node.owner_user_id,
            "owner_display_name": post_id_node.owner_display_name,
            "last_editor_user_id": post_id_node.last_editor_user_id,
            "last_activity_date": post_id_node.last_activity_date,
            "title": post_id_node.title,
            "tags": post_id_node.tags,
            "answer_count": post_id_node.answer_count,
            "comment_count": post_id_node.comment_count,
            "favorite_count": post_id_node.favorite_count,
            "creation_date": post_id_node.creation_date,
        }
        return post

    def query_related_posts_by_string(self, str, top_number=10):
        # 'What is unit testing?'
        # todo ? limit the number of result
        post_nodes = self.session.query(PostsRecord).from_statement(
            text("SELECT * FROM posts where Title=:Title")).params(
            Title=str)
        post_results_id = []
        for p in post_nodes:
            post_results_id.append({"id": p.id})
        return post_results_id

    def __repr__(self):
        return '<POSTS: id=%r score=%r title=%r tags=%r>' % (self.id, self.score, self.title, self.tags)


class KnowledgeTableColumnMapRecord(Base):
    __tablename__ = 'knowledge_table_row_column_map'
    id = Column(Integer, primary_key=True, autoincrement=True)
    start_table_id = Column(Integer, ForeignKey('knowledge_table.id'), nullable=False)
    end_table_id = Column(Integer, ForeignKey('knowledge_table.id'), nullable=False)
    start_row_name = Column(String(128), nullable=False, index=True)
    start_row_id = Column(Integer, nullable=False, index=True)
    end_row_id = Column(Integer, nullable=False, index=True)
    valid = Column(Boolean, nullable=False, index=True, default=True)
    create_time = Column(DateTime(), nullable=False, index=True)

    __table_args__ = (Index('start_id_index', start_table_id, end_table_id, start_row_id, start_row_name),
                      Index('end_id_index', start_table_id, end_table_id, end_row_id, start_row_name),
                      {
                          "mysql_charset": "utf8",
                      })

    def __init__(self, start_knowledge_table, end_knowledge_table, start_row_id, end_row_id, start_row_name):
        self.start_table_id = start_knowledge_table.id
        self.end_table_id = end_knowledge_table.id
        self.start_row_id = start_row_id
        self.end_row_id = end_row_id
        self.start_row_name = start_row_name
        self.create_time = datetime.now()

    def create(self, session, autocommit=True):
        session.add(self)
        if autocommit:
            session.commit()
        return self

    @staticmethod
    def exist_import_record(session, start_knowledge_table, end_knowledge_table, start_row_id, start_row_name):
        """
        check if the start_row_id has map in end_knowledge_table
        :param session:
        :param start_knowledge_table:
        :param end_knowledge_table:
        :param start_row_id:
        :return:
        """

        try:
            team = session.query(KnowledgeTableColumnMapRecord).filter_by(start_table_id=start_knowledge_table.id,
                                                                          end_table_id=end_knowledge_table.id,
                                                                          start_row_id=start_row_id,
                                                                          start_row_name=start_row_name).first()
            if team:
                return True
            else:
                return False
        except Exception:
            traceback.print_exc()
            return False

    @staticmethod
    def get_end_row_id(session, start_knowledge_table, end_knowledge_table, start_row_id, start_row_name):
        """
        check if the start_row_id has map in end_knowledge_table
        :param session:
        :param start_knowledge_table:
        :param end_knowledge_table:
        :param start_row_id:
        :return:
        """

        try:
            end_row_id = session.query(KnowledgeTableColumnMapRecord.end_row_id).filter_by(
                start_table_id=start_knowledge_table.id,
                end_table_id=end_knowledge_table.id,
                start_row_id=start_row_id,
                start_row_name=start_row_name).scalar()
            return end_row_id
        except Exception:
            # traceback.print_exc()
            return None


def parse_api_type_string_to_api_type_constant(api_type_string):
    if api_type_string == "Method":
        return APIEntity.API_TYPE_METHOD
    if api_type_string == "Constructor":
        return APIEntity.API_TYPE_CONSTRUCTOR
    if api_type_string == "Nested":
        return APIEntity.API_TYPE_CLASS
    if api_type_string == "Required":
        return APIEntity.API_TYPE_FIELD
    if api_type_string == "Optional":
        return APIEntity.API_TYPE_FIELD
    if api_type_string == "Field":
        return APIEntity.API_TYPE_FIELD
    if api_type_string == "Enum":
        return APIEntity.API_TYPE_ENUM_CONSTANTS

    api_type = APIEntity.API_TYPE_UNKNOWN
    return api_type


# if __name__ == "__main__":
#     # create table in 75
#     engine = EngineFactory.create_graphdata_engine_to_center()
#     metadata = MetaData(bind=engine)
#     Base.metadata.create_all(bind=engine)


if __name__ == "__main__":
    engine = EngineFactory.create_engine_to_center()
    metadata = MetaData(bind=engine)
    # delete all table
    # Base.metadata.drop_all(bind=engine)

    # create the table
    Base.metadata.create_all(bind=engine)
