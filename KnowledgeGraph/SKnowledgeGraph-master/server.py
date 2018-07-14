# -*- coding: utf-8 -*-
import inspect
import logging
import sys

from flask import Flask
from flask import jsonify
from flask import request
from flask_cors import CORS

from db.engine_factory import EngineFactory
from db.heat_handler import SQLHeatHandler
from db.model import PostsRecord, APIEntity, DocumentSentenceText, DocumentAnnotationStatus, \
    DocumentSentenceTextAnnotation
from db.search import SOPostSearcher, APISearcher
from qa.new_qa.qa_system import QuestionAnswerSystem
from server_util.search import SearchUtil
from shared.logger_util import Logger
from shared.logger_util import SQLAlchemyHandler
from skgraph.graph.accessor.graph_accessor import DefaultGraphAccessor, GraphClient
from skgraph.graph.apientitylinking import APIEntityLinking
from skgraph.graph.label_util import LabelUtil
from skgraph.graph.node_cleaner import GraphJsonParser

reload(sys)
sys.setdefaultencoding("utf-8")

app = Flask(__name__)
CORS(app)
db_handler = SQLAlchemyHandler()
db_handler.setLevel(logging.WARN)  # Only serious messages
app.logger.addHandler(db_handler)

logger = Logger("neo4jServer").get_log()
logger.info("create logger")

graphClient = DefaultGraphAccessor(GraphClient(server_number=1))
logger.info("create graphClient")

api_entity_linker = APIEntityLinking()
logger.info("create api_entity_linker object")

questionAnswerSystem = QuestionAnswerSystem()
logger.info("create questionAnswerSystem")

dbSOPostSearcher = SOPostSearcher(EngineFactory.create_so_session(),logger=app.logger)
logger.info("create SO POST Searcher")

api_entity_session = EngineFactory.create_session(autocommit=True)
apiSearcher = APISearcher(session=api_entity_session,logger=app.logger)
logger.info("create API Searcher")

sql_heat_handler = SQLHeatHandler(session=EngineFactory.create_heat_session(),logger=app.logger)
logger.info("create SQL HeatHandler")

graphJsonParser = GraphJsonParser(graph_accessor=graphClient)
logger.info("create graphJsonParser Json Parser")

search_util = SearchUtil(graphClient, apiSearcher)


NEWEST_NODE = graphClient.get_newest_nodes(100)
logger.info("init newest node")

labelUtil = LabelUtil()

ALL_LABELS_LIST = labelUtil.get_all_public_label_name_list
logger.info("init all labels")

METADATA = {"Entity": graphClient.count_nodes(), "Relation": graphClient.count_relations(),
            "Relation Type": graphClient.count_relation_type(),
            "API Library": graphClient.count_library_nodes(), "API Package": graphClient.count_package_nodes(),
            "API Class": graphClient.count_class_nodes(), "API Method": graphClient.count_method_nodes()}

logger.info("init all metadata")


## todo: 1. add logger to all interface record all api calling and fail. the time of calling.

@app.route('/')
def hello():
    return "Hello World!\n"


if __name__ == '__main__':
    from werkzeug.contrib.fixers import ProxyFix

    app.wsgi_app = ProxyFix(app.wsgi_app)
    app.run()
    logger.info("app.run()")


@app.route('/getStartNode/', methods=['POST', 'GET'])
def getStartNode():
    node = graphClient.find_node_by_id(7686)
    r = graphJsonParser.parse_node_to_public_json(node)
    return jsonify(r)


@app.route('/getNodeByID/', methods=['POST', 'GET'])
def getNodeByID():
    if not request.json:
        app.logger.warn("getNodeByID fail empty json")
        return "fail"
    if not 'id' in request.json:
        logger.warn("getNodeByID fail" + str(request.json))
        app.logger.warn("getNodeByID fail" + str(request.json))
        return "fail"
    apit_id = request.json['id']
    sql_heat_handler.like(apit_id)
    node = graphClient.find_node_by_id(apit_id)
    r = graphJsonParser.parse_node_to_public_json(node)
    return jsonify(r)


@app.route('/getRelationsBetweenTwoNodes/', methods=['POST', 'GET'])
def getRelationsBwtweenTwoNodes():
    if not request.json:
        app.logger.warn("getRelationsBwtweenTwoNodes fail empty json")
        return "fail"
    sid = request.json['start_id']
    eid = request.json['end_id']
    subgraph = graphClient.get_relations_between_two_nodes_in_subgraph(sid, eid)
    relations_json = graphJsonParser.parse_relations_in_subgraph_to_public_json(subgraph)
    return jsonify({'relations': relations_json})


@app.route('/expandNode/', methods=['POST', 'GET'])
def expandNode():
    if not request.json:
        app.logger.warn("expandNode fail empty json")
        return "fail"
    if not 'id' in request.json:
        app.logger.warn("expandNode fail" + str(request.json))
        return "fail"
    limit = 50
    if 'limit' in request.json:
        limit = request.json["limit"]

    apit_id = request.json['id']
    sql_heat_handler.like(apit_id)
    subgraph = graphClient.expand_node_for_adjacent_nodes_to_subgraph(apit_id, limit=limit)

    subgraph_json = graphJsonParser.parse_subgraph_to_public_json(subgraph)

    return jsonify(subgraph_json)


@app.route('/searchNodesByKeyWords/', methods=['POST', 'GET'])
def searchNodesByKeyWords():
    if not request.json:
        app.logger.warn("searchNodesByKeyWords fail empty json")
        return "fail"
    if not 'keywords' in request.json:
        app.logger.warn("searchNodesByKeyWords fail" + str(request.json))
        return "fail"

    top_number = 10
    if 'top_number' in request.json:
        top_number = request.json['top_number']

    node_list=search_util.search(request.json['keywords'], top_number)

    nodes = graphJsonParser.parse_node_list_to_json(node_list)

    return jsonify({'nodes': nodes})


@app.route('/answer/', methods=['POST', 'GET'])
def answer():
    if not request.json or not 'question' in request.json:
        return "fail"
    returns = []
    top_number = 10
    if 'top_number' in request.json:
        top_number = request.json['top_number']

    answer_collection = questionAnswerSystem.full_answer(request.json['question'], top_number)
    if answer_collection is None:
        return 'fail'
    return jsonify(answer_collection.parse_json())


@app.route('/shortestPathToID/', methods=['POST', 'GET'])
def shortestPathToID():
    if not request.json or not 'id' in request.json or not 'end_id' in request.json:
        return "fail"
    DEFAULT_PATH_DEGREE = 6
    DEFAULT_TOP_NUM = 3
    top_number = DEFAULT_TOP_NUM
    max_len = DEFAULT_PATH_DEGREE

    if 'top_number' in request.json:
        top_number = request.json['top_number']
    if 'max_len' in request.json:
        max_len = request.json['max_len']
        if request.json['max_len'] < 1 or request.json['max_len'] > DEFAULT_PATH_DEGREE:
            max_len = DEFAULT_PATH_DEGREE

    subgraph = graphClient.get_shortest_path_in_subgraph(start_id=request.json['id'],
                                                         end_id=request.json['end_id'],
                                                         max_degree=max_len,
                                                         limit=top_number)
    subgraph_json = graphJsonParser.parse_subgraph_to_public_json(subgraph)

    return jsonify(subgraph_json)


@app.route('/shortestPathToName/', methods=['POST', 'GET'])
def shortestPathToName():
    if not request.json or not 'id' in request.json or not 'name' in request.json:
        return "fail"
    DEFAULT_PATH_DEGREE = 6
    top_number = 3
    if 'top_number' in request.json:
        top_number = request.json['top_number']
    max_len = DEFAULT_PATH_DEGREE
    if 'max_len' in request.json:
        max_len = request.json['max_len']
        if request.json['max_len'] < 1 or request.json['max_len'] > DEFAULT_PATH_DEGREE:
            max_len = DEFAULT_PATH_DEGREE

    subgraph = graphClient.get_shortest_path_to_name_in_subgraph(request.json['id'],
                                                                 request.json['name'],
                                                                 max_len, top_number)
    subgraph_json = graphJsonParser.parse_subgraph_to_public_json(subgraph)

    return jsonify(subgraph_json)


@app.route('/GetKnowledgeGraphMetaData/', methods=['POST', 'GET'])
def GetKnowledgeGraphMetaData():
    return jsonify(METADATA)


@app.route('/EntityLink/', methods=['POST', 'GET'])
def entity_link():
    if not request.json:
        return "fail"
    returns = []
    j = request.json
    if 'str' in j:
        returns = api_entity_linker.get_link(j['str'])

    return jsonify(returns)


@app.route('/GetOutRelation/', methods=['POST', 'GET'])
def get_out_relation():
    # todo change the to "page_index" and define it value start from 0
    if not request.json:
        return "fail"
    j = request.json
    if 'id' in j and 'page_number' in j:
        returns = graphJsonParser.parse_relation_list_to_json(
            graphClient.find_out_relation_list(j['id'], j['page_number']))
        return jsonify({'relations': returns})

    return 'fail'


@app.route('/GetInRelation/', methods=['POST', 'GET'])
def get_in_relation():
    # todo change the to "page_index" and define it value start from 0

    if not request.json:
        return "fail"
    j = request.json
    if 'id' in j and 'page_number' in j:
        returns = graphJsonParser.parse_relation_list_to_json(
            graphClient.find_in_relation_list(j['id'], j['page_number']))
        return jsonify({'relations': returns})

    return 'fail'


@app.route('/GetNewNodes/', methods=['POST', 'GET'])
def get_new_nodes():
    top_number = 5
    if request.json:
        j = request.json
        if 'top_number' in j:
            top_number = int(j['top_number'])
    nodes = NEWEST_NODE[:top_number]

    returns = graphJsonParser.parse_node_list_to_json(nodes)
    return jsonify({'nodes': returns})


@app.route('/GetIntroduction/', methods=['POST', 'GET'])
def get_introduction():
    returns = []
    returns.append({'text': '''Our API-centric knowledge graph propose to bridge the knowledge gap between software documents. The knowledge graph is built by extracting entities and relations from multiple heterogeneous information sources and fusing the knowledge through entity resolution.
It is buit by Fudan Software Engineering Laboratory. We have constructed a knowledge graph for JDK and Android APIs and we will develop an integrated platform that provides API-centric knowledge services such as question answering and knowledge recommendation for the public.'''})
    return jsonify(returns)


@app.route('/GetPopularNodes/', methods=['POST', 'GET'])
def get_popular_nodes():
    top_number = 3
    if request.json:
        j = request.json
        if 'top_number' in j:
            top_number = int(j['top_number'])
    entity_heat_list = sql_heat_handler.get_most_popular_entity_id_list(top_number=top_number)
    nodes = []

    for heat in entity_heat_list:
        entity_id = heat["api_id"]
        node = graphClient.find_node_by_id(entity_id)
        nodes.append(node)

    returns = graphJsonParser.parse_node_list_to_json(nodes)
    return jsonify({'nodes': returns})


@app.route('/GetAllLabels/', methods=['POST', 'GET'])
def get_all_labels():
    """
    get all labels name
    :return:
    """
    returns = ALL_LABELS_LIST
    return jsonify({'labels': returns})


@app.route('/SearchAPI/', methods=['POST', 'GET'])
def search_api():
    if not request.json:
        return "fail"
    j = request.json

    if not 'keyword' in j:
        return "fail"

    keyword = j['keyword']
    label = 'api'
    top_number = 10
    if 'label' in j:
        label = j['label']
    if 'top_number' in j:
        top_number = j['top_number']

    returns = graphJsonParser.parse_nodes_in_subgraph_to_public_json(
        graphClient.search_nodes_by_keyword(keyword, label, top_number))
    return jsonify({'nodes': returns})


@app.route('/SearchAPIID/', methods=['POST', 'GET'])
def search_api_id():
    request_json = request.json

    if not check_completeness_api_with_parameters(inspect.stack()[0][3], request_json, 'keyword'):
        return "fail"

    keyword = request_json['keyword']
    api_type = APIEntity.API_TYPE_ALL_API_ENTITY
    top_number = 10
    if 'api_type' in request_json:
        api_type = request_json['api_type']
    if 'top_number' in request_json:
        top_number = request_json['top_number']

    api_entity_list = apiSearcher.search_api_entity(query=keyword, result_limit=top_number, api_type=api_type)
    api_id_list = []
    for api_entity in api_entity_list:
        api_id_list.append(api_entity.id)
    returns = graphClient.get_api_entity_map_to_node_id(api_id_list)
    return jsonify(returns)


@app.route('/SearchAPINode/', methods=['POST', 'GET'])
def search_api_node():
    request_json = request.json

    if not check_completeness_api_with_parameters(inspect.stack()[0][3], request_json, 'keyword'):
        return "fail"

    keyword = request_json['keyword']
    api_type = APIEntity.API_TYPE_ALL_API_ENTITY
    top_number = 10
    if 'api_type' in request_json:
        api_type = request_json['api_type']
    if 'top_number' in request_json:
        top_number = request_json['top_number']

    api_entity_list = apiSearcher.search_api_entity_with_order(query=keyword, result_limit=top_number, api_type=api_type)
    api_id_list = []
    for api_entity in api_entity_list:
        api_id_list.append(api_entity.id)
    nodes = graphClient.get_api_entity_map_to_node(api_id_list)
    returns = graphJsonParser.parse_node_list_to_json(nodes)
    return jsonify(returns)


@app.route('/APILinking/', methods=['POST', 'GET'])
def api_linking():
    if not request.json:
        return "fail"
    j = request.json

    if not 'api_string' in j:
        return "fail"

    api_string = j['api_string']
    api_type = 1
    top_number = 5
    declaration = ''
    parent_api = ''
    if 'top_number' in j:
        top_number = j['top_number']
    if 'api_type' in j:
        api_type = j['api_type']
    if 'declaration' in j:
        declaration = j['declaration']
    if 'parent_api' in j:
        parent_api = j['parent_api']
    returns = graphClient.api_linking(api_string, top_number, api_type, declaration, parent_api)
    return jsonify(returns)


# KGIDToMySQLID
@app.route('/KGIDToMySQLID/', methods=['POST', 'GET'])
def kg_id2mysql_id():
    if not request.json:
        return "fail"
    j = request.json

    if not 'kg_id' in j:
        return "fail"

    kg_id = j['kg_id']
    returns = graphClient.kg_id2mysql_id(kg_id)
    return jsonify(returns)


# MySQLIDToKGID
@app.route('/MySQLIDToKGID/', methods=['POST', 'GET'])
def mysql_id2kg_id():
    if not request.json:
        return "fail"
    j = request.json

    if not 'mysql_id' in j:
        return "fail"

    mysql_id = j['mysql_id']
    returns = graphClient.mysql_id2kg_id(mysql_id)
    return jsonify(returns)


@app.route('/PostID/', methods=['POST', 'GET'])
def post_get_by_id():
    if not request.json:
        return "fail"
    j = request.json
    if not 'id' in j:
        return "fail"
    id = j['id']
    posts_record = PostsRecord()
    returns = posts_record.get_post_by_id(id)
    return jsonify(returns)


@app.route('/RelatedPosts/', methods=['POST', 'GET'])
def query_related_posts_by_string():
    request_json = request.json
    if not check_completeness_api_with_parameters(inspect.stack()[0][3], request_json, "str"):
        return "fail"
    str = request_json['str']
    if not str or not str.strip():
        return "fail"

    returns = dbSOPostSearcher.search_post_in_simple_format(str, 10)
    return jsonify(returns)


@app.route('/GetRelationDescription/', methods=['POST', 'GET'])
def get_relation_description():
    request_json = request.json
    if not check_completeness_api_with_parameters(inspect.stack()[0][3], request_json, "name"):
        return "fail"

    name = request_json['name']
    if not name or not name.strip():
        return "fail"

    returns = graphJsonParser.relationUtil.get_description_by_relation_type(name)
    return jsonify(returns)


def getType(number=0):
    if number == 0:
        return 'api'
    elif number == 1:
        return 'java package'
    elif number == 2:
        return 'java class'
    elif number == 3:
        return 'java class'
    elif number == 4:
        return 'java class'
    elif number == 5:
        return 'java class'
    elif number == 6:
        return 'java field'
    elif number == 7:
        return 'java constructor'
    elif number == 11:
        return 'java method'


def check_parameters_completeness(request_json, *parameters):
    for p in parameters:
        if p not in request_json:
            return False, p

    return True, ""


def check_is_empty_json(request_json):
    if request_json is None:
        return True
    else:
        return False


def check_completeness_api_with_parameters(method_name, request_json, *parameters):
    if check_is_empty_json(request_json):
        app.logger.warn("empty json in %s", method_name)
        return False
    completeness, error_string = check_parameters_completeness(request_json, *parameters)
    if not completeness:
        app.logger.warn("parameter %s is missing in %s", error_string, method_name)
        return False

    return True


@app.route('/Log/', methods=['POST', 'GET'])
def test_log():
    # app.logger.exception("Failed !")
    app.logger.warn("Failed !")
    return ""


@app.route('/GetPublicLabels/', methods=['POST', 'GET'])
def getPublicLabels():
    """
    get public labels detail info
    :return:
    """
    returns = labelUtil.get_all_public_label_json_list()
    return jsonify(returns)


@app.route('/like/', methods=['POST', 'GET'])
def like():
    if not request.json:
        return "fail"
    j = request.json
    if not 'id' in j:
        return "fail"
    api_id = j['id']
    returns = sql_heat_handler.like(api_id)
    return jsonify(returns)


@app.route('/getHeat/', methods=['POST', 'GET'])
def get_api_heat_by_apiID():
    if not request.json:
        return "fail"
    j = request.json
    if not 'id' in j:
        return "fail"

    api_id = j['id']
    returns = sql_heat_handler.get_api_heat_by_apiID(api_id)
    return jsonify(returns)


@app.route('/getDocById/', methods=['POST', 'GET'])
def get_doc_by_id():
    if not request.json:
        return "fail"
    j = request.json
    if not 'doc_id' in j:
        return "fail"
    doc_id = j['doc_id']
    sentence_list = DocumentSentenceText.get_sentence_list_by_doc_id(api_entity_session, doc_id)
    sentence_list_json = []
    for each in sentence_list:
        temp = {
            "id": each.id,
            "doc_id": each.doc_id,
            "sentence_index": each.sentence_index,
            "text": each.text
        }
        sentence_list_json.append(temp)
    return jsonify(sentence_list_json)


@app.route('/getUnfinishedDocs/', methods=['POST', 'GET'])
def get_unfinished_docs():
    unfinished_doc_tuple = DocumentAnnotationStatus.get_unfinished_doc_list(api_entity_session)
    unfinished_doc_list = []
    for each in unfinished_doc_tuple:
        unfinished_doc_list.append(each[0])
    result = {
        "unfinished_doc_list": unfinished_doc_list
    }
    return jsonify(result)


@app.route('/getAnnotationByIndex/', methods=['POST', 'GET'])
def get_annotation_by_index():
    if not request.json:
        return "fail"
    j = request.json
    if 'doc_id' not in j and "sentence_id" not in j:
        return "fail"
    doc_id = j["doc_id"]
    sentence_id = j["sentence_id"]
    sentence_annotation = DocumentSentenceTextAnnotation.get_annotation_by_index(api_entity_session, doc_id, sentence_id)
    if sentence_annotation:
        return jsonify({"annotation_type": sentence_annotation})
    else:
        return jsonify({"annotation_type": -1})
