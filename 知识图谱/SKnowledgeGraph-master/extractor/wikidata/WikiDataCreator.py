#!/usr/bin/python
# -*- coding: utf-8 -*-


from WikiDataItem import WikiDataItem
from WikiDataPropertyUtil import PropertyCleanUtil
from shared.logger_util import Logger
from skgraph.graph.accessor.factory import NodeBuilder
from skgraph.graph.accessor.graph_accessor import GraphClient
from skgraph.graph.accessor.graph_client_for_wikidata import WikiDataGraphAccessor

_logger = Logger(logger="wikidata_node_creator").get_log()


class WikiDataNodeCreator:
    propertyCleanUtil = PropertyCleanUtil()

    def __init__(self, graphClient=None):
        self.propertyCleanUtil.load()
        self.logger = _logger
        self.graphClient = graphClient

    def create_item_nodes(self, *wd_item_id_list):
        for id in wd_item_id_list:
            try:
                self.create_item_node(id)
            except Exception, error:
                self.logger.warn('%s has exception', id)
                self.logger.exception("-----------")

    def create_item_node(self, wd_item_id):
        item = WikiDataItem(wd_item_id)
        if item.exist():
            property_name_list = item.get_wikidata_item_property_name_list()
            self.update_property_map(property_name_list)
            item_property_dict = item.get_wikidata_item_property_dict()
            property_dict = self.propertyCleanUtil.replace_property_id(item_property_dict, property_name_list)
            self.create_node_from_dict(property_dict)
        else:
            print wd_item_id, ' can not be get!'
            self.logger.warn('%s can not be get!', wd_item_id)

    def create_item_node_from_WikiDataItem_object(self, item):
        wd_item_id = item.wd_item_id
        if item.exist():
            property_name_list = item.get_wikidata_item_property_name_list()
            self.update_property_map(property_name_list)
            item_property_dict = item.get_wikidata_item_property_dict()
            property_dict = self.propertyCleanUtil.replace_property_id(item_property_dict, property_name_list)
            self.create_node_from_dict(property_dict)
        else:
            print wd_item_id, ' can not be get!'
            self.logger.warn('%s can not be get!', wd_item_id)

    def create_property_nodes(self, *wd_item_id_list):
        for id in wd_item_id_list:
            self.create_property_node(id)
        self.propertyCleanUtil.save()

    def create_property_node(self, wd_item_id):
        item = WikiDataItem(wd_item_id)
        if item.exist():

            property_name_list = item.get_wikidata_item_property_name_list()
            item_property_dict = item.get_wikidata_item_property_dict()
            property_dict = self.propertyCleanUtil.replace_property_id(item_property_dict, property_name_list)

            self.create_node_from_dict(property_dict)
            try:
                self.propertyCleanUtil.add(property_dict['wd_item_id'], property_dict['labels_en'])
            except Exception, error:
                self.logger.warn('-%s- fail for labels_en not exist', wd_item_id)
                self.logger.exception('this is an exception message')
                print wd_item_id, Exception
        else:
            self.logger.warn('-%s- fail for wikidata item not exist', wd_item_id)
            print wd_item_id, ' not exist'

    def update_property_map(self, property_id_list):
        property_id_list = self.propertyCleanUtil.filter_not_in_property_name_list(property_id_list)
        self.create_property_nodes(*property_id_list)

    @staticmethod
    def get_node_labels(property_dict):
        labels = ['wikidata']
        if property_dict['wd_item_id'][0] == 'P':
            labels.append('wd_property')
            labels.append('relation')
            return labels
        else:
            labels.append('entity')
        return labels

    def create_node_from_dict(self, property_dict):
        graphClient = self.__get_graph_client()

        labels = WikiDataNodeCreator.get_node_labels(property_dict)
        builder = NodeBuilder()
        builder = builder.add_label_wikidata().add_property(**property_dict).add_labels(*labels)

        node = builder.build()
        try:
            graphClient.create_or_update_wikidata_node(node=node)
            self.logger.info('create node for wikidata item %s', property_dict['wd_item_id'])
        except Exception, error:
            self.logger.warn('-%s- fail for create node for wikidata item ', property_dict['wd_item_id'])
            self.logger.exception('this is an exception message')

    def __get_graph_client(self):
        if not self.graphClient:
            self.graphClient = WikiDataGraphAccessor(GraphClient(server_number=1))
        graphClient = self.graphClient
        return graphClient


if __name__ == "__main__":
    wikiDataNodeCreator = WikiDataNodeCreator()
    wikiDataNodeCreator.create_item_node(wd_item_id="Q2642722")
