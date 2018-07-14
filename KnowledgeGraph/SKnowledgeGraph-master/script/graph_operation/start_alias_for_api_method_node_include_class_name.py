from skgraph.graph.accessor.graph_accessor import GraphClient
from skgraph.graph.graph_operator import GraphOperator
from skgraph.graph.operation.java_method_add_alias_include_method import \
    JavaMethodAliasPropertyIncludeClassNameCreateOperation

graphOperator = GraphOperator(GraphClient(server_number=1))
graphOperator.operate_on_all_nodes(1000, ['java method'], JavaMethodAliasPropertyIncludeClassNameCreateOperation())