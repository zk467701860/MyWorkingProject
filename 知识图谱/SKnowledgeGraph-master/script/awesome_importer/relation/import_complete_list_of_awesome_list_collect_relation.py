import codecs
import json

from py2neo import Relationship

from skgraph.graph.accessor.graph_accessor import GraphClient
from skgraph.graph.accessor.graph_client_for_awesome import AwesomeGraphAccessor
from shared.logger_util import Logger

_logger = Logger("AwesomeImporter").get_log()

awesomeGraphAccessor = AwesomeGraphAccessor(GraphClient(server_number=0))
baseGraphClient = awesomeGraphAccessor.graph

file_name = "complete_list_of_awesome_list_collect_relation.json"
with codecs.open(file_name, 'r', 'utf-8') as f:
    relation_list = json.load(f)
for relation in relation_list:
    start_url = relation["start_url"]
    relation_str = relation["relation"]
    end_url = relation["end_url"]
    start_node = awesomeGraphAccessor.find_awesome_list_entity(start_url)
    end_node = awesomeGraphAccessor.find_awesome_list_entity(end_url)
    if start_node is not None and end_node is not None:
        relationship = Relationship(start_node, relation_str, end_node)
        baseGraphClient.merge(relationship)
        _logger.info("create or merge relation" + str(relation))
    else:
        _logger.warn("fail create relation" + str(relation))
