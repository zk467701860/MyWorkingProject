from skgraph.graph.operation.graph_operation import GraphOperation


class APILinkerOperation(GraphOperation):
    name = 'APILinkerOperation'

    def __init__(self, logger, graph_accessor, api_linker):
        GraphOperation.__init__(self)

        self.MIN_PROBABILITY = 0.25
        self.success_num = 0
        self.fail_num = 0

        self.logger = logger
        self.graph_accessor = graph_accessor
        self.api_linker = api_linker

    def operate(self, node):
        apy_type = node["api type"]
        node_id = self.graph_accessor.get_id_for_node(node)
        parent_node = self.graph_accessor.get_parent_api_node_for_api_node(node_id)

        parent_api_name = None
        if parent_node:
            parent_api_name = parent_node["api_qualifier_name"]
        if not parent_api_name:
            parent_api_name = parent_node["name"]

        context = {"api_type": apy_type, "parent_api": parent_api_name, "declaration": node["declaration"]}

        if "name" in node:
            api_name = node["name"]
            if self.try_to_link_api(api_name, context, node):
                return node, node
        if "full type method name" in node:
            api_name = node["full type method name"]
            if self.try_to_link_api(api_name, context, node):
                return node, node
        self.log_fail(node)

        return node, node

    def try_to_link_api(self, api_name, context, node):
        link_result = self.api_linker.link(api_name, context)
        final_linking_candidate = None
        if len(link_result) == 1:
            final_linking_candidate = link_result[0]
        if len(link_result) > 1:
            linking_first_candidate = link_result[0]
            linking_second_candidate = link_result[1]

            probability_first = linking_first_candidate["probability"]
            probability_second = linking_second_candidate["probability"]

            if probability_first > self.MIN_PROBABILITY and probability_first > probability_second:
                final_linking_candidate = linking_first_candidate

        if final_linking_candidate == None:
            self.log_fail_link_result_for_try_to_link(api_name, link_result, node)
            return False
        else:
            node["api_id"] = final_linking_candidate["api_id"]
            node["api_qualifier_name"] = final_linking_candidate["api_qualifier_name"]
            self.log_success(api_name, link_result, node)
            return True

    def log_fail_link_result_for_try_to_link(self, api_name, link_result, node):
        self.logger.info("----fail linking result for %s", api_name)
        self.logger.info(link_result)
        self.logger.info(node)
        self.logger.warn("-----fail end------")

    def log_fail(self, node):
        self.logger.warn("-----fail info start------")
        self.logger.warn(node)
        self.fail_num = self.fail_num + 1
        self.logger.info("fail num=%d", self.fail_num)
        self.logger.warn("-----fail info end------")

    def log_success(self, api_name, link_result, node):
        self.logger.info("******success linking result for %s", api_name)
        self.logger.info(link_result)
        self.logger.info(node)
        self.success_num = self.success_num + 1
        self.graph_accessor.push_node(node)
        self.logger.info("success num=%d", self.success_num)
        self.logger.info("************************")
