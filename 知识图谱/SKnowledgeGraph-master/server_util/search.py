"""
    combine multiply search method, to get a search result.
"""
import gevent

from db.model import APIEntity
from skgraph.graph.accessor.graph_accessor import DefaultGraphAccessor


class SearchUtil:

    def __init__(self, graph_client, api_searcher):
        self.graph_accessor = DefaultGraphAccessor(graph_client)
        self.api_searcher = api_searcher

    def search(self, keywords, top_number):
        result_node_list = []
        jobs = []

        api_db_search_job = gevent.spawn(self.api_searcher.search_api_entity_with_order, keywords, top_number)
        jobs.append(api_db_search_job)
        graph_search_job = gevent.spawn(self.graph_accessor.search_nodes_by_name_in_list, keywords, top_number)
        jobs.append(graph_search_job)

        gevent.joinall(jobs, timeout=2000)
        api_entity_list = api_db_search_job.value

        api_id_list = []
        for api_entity in api_entity_list:
            api_id_list.append(api_entity.id)
        api_node_list = self.graph_accessor.get_api_entity_map_to_node(api_id_list)
        for api_node in api_node_list:
            if api_node not in result_node_list:
                result_node_list.append(api_node)

        graph_node_result_list = graph_search_job.value
        for graph_node in graph_node_result_list:
            if graph_node not in result_node_list:
                result_node_list.append(graph_node)
        ## todo, change the node search to a more generate way, for example, a scorer is necessary
        node_score = {}
        for node in result_node_list:
            node_id = self.graph_accessor.get_id_for_node(node)
            node_score[node_id] = 0
        for node in result_node_list:
            node_id = self.graph_accessor.get_id_for_node(node)
            if node in api_node_list and node in graph_node_result_list:
                node_score[node_id] = node_score[node_id] + 10
            if node.has_label("extended knowledge"):
                node_score[node_id] = node_score[node_id] - 3
            if node.has_label("java class") or node.has_label("wikidata"):
                node_score[node_id] = node_score[node_id] + 1
            if node.has_label("java constructor"):
                node_score[node_id] = node_score[node_id] - 1

        left_nodes = []
        for node in result_node_list:
            left_nodes.append(node)

        sorted_node_list = []
        while len(left_nodes) > 0:
            max_score = -10000
            max_node = None
            for node in left_nodes:
                node_id = self.graph_accessor.get_id_for_node(node)
                if node_score[node_id] > max_score:
                    max_score = node_score[node_id]
                    max_node = node
            sorted_node_list.append(max_node)
            left_nodes.remove(max_node)

        return sorted_node_list[:top_number]
