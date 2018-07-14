from script.api_linking.api_linker_operation import APILinkerOperation
from db.api_linker import APILinker
from shared.logger_util import Logger
from skgraph.graph.accessor.graph_accessor import GraphClient
from skgraph.graph.accessor.graph_client_for_api import APIGraphAccessor
from skgraph.graph.graph_operator import GraphOperator

client = GraphClient(server_number=1)
graphOperator = GraphOperator(client)
api_linker = APILinker()
logger = Logger("API_Name_Linking").get_log()
accessor = APIGraphAccessor(GraphClient())
operation = APILinkerOperation(logger, accessor, api_linker)
graphOperator.operate_on_all_nodes(500, ['api'], operation)
