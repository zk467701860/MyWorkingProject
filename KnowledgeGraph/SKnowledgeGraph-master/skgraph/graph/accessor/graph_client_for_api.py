from graph_accessor import GraphAccessor
from shared.logger_util import Logger

_logger = Logger("APIGraphAccessor").get_log()


class APIGraphAccessor(GraphAccessor):
    '''
    a GraphAccessor for API node query
    '''

    def get_parameter_nodes_of_method(self, method_node_id):
        '''
        get all parameter nodes belong to one method
        :param method_node_id: method node id
        :return: parameter nodes list
        '''
        try:
            query = 'Match  (n:`java method parameter`)-[r:`belong to`]->(m:`java method`)  where ID(m)={method_node_id} return distinct n'.format(
                method_node_id=method_node_id)
            node_list = []
            result = self.graph.run(query)
            for n in result:
                node_list.append(n['n'])
            return node_list

        except Exception, error:
            _logger.exception()
            return []

    def get_parent_class_node_for_method_node(self, method_node_id):
        '''
                get parent class node that one method belong to
                :param method_node_id: method node id
                :return: class node
                '''
        try:
            query = 'Match  (n:`java method`)-[r:`belong to`]->(m:`java class`)  where ID(n)={method_node_id} return m limit 1'.format(
                method_node_id=method_node_id)
            return self.graph.evaluate(query)

        except Exception, error:
            _logger.exception()
            return None

    def get_parent_class_node_for_field_node(self, field_node_id):
        '''
        get parent class node that one field node belong to
        :param field_node_id:
        :return: class node
        '''
        try:
            query = 'Match  (n:`java field`)-[r:`belong to`]->(m:`java class`)  where ID(n)={field_node_id} return m limit 1'.format(
                field_node_id=field_node_id)
            return self.graph.evaluate(query)

        except Exception, error:
            _logger.exception()
            return None

    def get_parent_api_node_for_api_node(self, api_node_id):
        '''
        get parent class node that one field node belong to
        :param field_node_id:
        :return: class node
        '''
        try:
            query = 'Match  (n:`api`)-[r:`belong to`]->(m:`api`)  where ID(n)={api_node_id} return m limit 1'.format(
                api_node_id=api_node_id)

            return self.graph.evaluate(query)

        except Exception, error:
            _logger.exception()
            return None
