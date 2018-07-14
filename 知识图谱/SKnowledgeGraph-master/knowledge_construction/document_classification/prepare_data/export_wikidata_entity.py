import codecs
import json

from knowledge_construction.document_classification.prepare_data.exporter_accessor import ExporterAccessor
from skgraph.graph.accessor.graph_accessor import GraphClient


class WikiDataExporter:
    def __init__(self):
        pass

    def get_main_property_of_node(self, node):
        property_list = []
        for property in node.keys():
            if property.startswith("aliases_") \
                    or property.startswith("descriptions_") \
                    or property.startswith("labels_") \
                    or property.startswith("site:"):
                continue
            property_list.append((property, node[property]))

        return property_list

    def start_export(self, output_file):
        accessor = ExporterAccessor(GraphClient(server_number=1))
        wikidata_ndoe_list = accessor.get_all_wikidata_node(limit=0)

        record_list = []
        for node in wikidata_ndoe_list:
            record = {}
            record["graph_id"] = accessor.get_id_for_node(node)
            record["wikidata_name"] = node["labels_en"]
            record["wikidata_id"] = node["wd_item_id"]
            record["wikidata_description"] = node["descriptions_en"]
            record["wikipedia_url"] = node["site:enwiki"]
            record["wikidata_property"] = node["site:enwiki"]
            if not node["descriptions_en"]:
                record["wikidata_description"] = ""
            if record["wikidata_id"] and record["wikidata_id"].startswith("P"):
                record["wikidata_id"] = None

            if not record["wikipedia_url"]:
                record["wikipedia_url"] = ""
            is_valid = True
            for k, v in record.items():
                if v == None:
                    is_valid = False
                    break
            if is_valid == False:
                print("node is not valid=", node)
                print("record is not valid=", record)
                continue
            record["properties"] = self.get_main_property_of_node(node)
            record_list.append(record)
            # print("record=", node)
        self.write_json(output_file, record_list)

    def write_json(self, output_path, node_list):
        with codecs.open(output_path, 'w', 'utf-8') as f:
            json.dump(node_list, f)


if __name__ == "__main__":
    output_file = "with_properties_wikidata_software_entity.json"
    exporter = WikiDataExporter()
    exporter.start_export(output_file)
