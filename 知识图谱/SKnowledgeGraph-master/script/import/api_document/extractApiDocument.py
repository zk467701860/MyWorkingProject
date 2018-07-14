import Queue

import MySQLdb
# Mysql connect

from py2neo import Relationship

from skgraph.graph.accessor.graph_accessor import GraphClient
from skgraph.graph.accessor.graph_client_for_wikidata import WikiDataGraphAccessor
from shared.logger_util import Logger

conn = None
cur = None
graphClient = WikiDataGraphAccessor(GraphClient(server_number=1))
# neo4j connect
connect_graph = graphClient.graph
logger = Logger("android_sdk_importer").get_log()
# buffer
q = Queue.Queue()
n = 0


def get_android_sdk_node():
    labels = ["library"]
    property_dict = {"name": "android API"}
    from skgraph.graph.accessor.factory import NodeBuilder
    nodeBuilder = NodeBuilder().add_labels(*labels).add_property(**property_dict)
    android_sdk_node = nodeBuilder.build()
    connect_graph.merge(android_sdk_node, "library", "name")
    return android_sdk_node


def get_instance_of_relation(node1, node2):
    property_dict = {"wd_item_id": "P31"}
    return Relationship(node1, 'instance of', node2, **property_dict)


# read from mysql
def mySQLReader(start, end):
    java_library_node = graphClient.find_wikidata_node(wd_item_id='Q21127166')
    if not java_library_node:
        logger.error("node: java library is not found")
    android_sdk_node = get_android_sdk_node()
    connect_graph.create(get_instance_of_relation(android_sdk_node, java_library_node))
    api_package_node = connect_graph.find_one(
        label="api concept",
        property_key="name",
        property_value="api package"
    )
    api_class_node = connect_graph.find_one(
        label="api concept",
        property_key="name",
        property_value="api class"
    )
    api_method_node = connect_graph.find_one(
        label="api concept",
        property_key="name",
        property_value="api method"
    )
    api_field_node = connect_graph.find_one(
        label="api concept",
        property_key="name",
        property_value="api field"
    )
    api_parameter_node = connect_graph.find_one(
        label="api concept",
        property_key="name",
        property_value="api method parameter"
    )

    # get all-version library
    cur.execute("select * from api_library where library_id >= %s and library_id < %s", (start, end))
    result = cur.fetchall()
    for i in range(0, len(result)):
        lib_node, library_id = import_library_node(android_sdk_node, i, result)

        # get package
        cur.execute("select * from api_package where library_id = %s", (library_id,))
        result1 = cur.fetchall()
        for j in range(0, len(result1)):
            api_package_id, package_node = import_package_node(api_package_node, j, lib_node, result1)

            # get class
            cur.execute("select * from api_class where package_id = %s", (api_package_id,))
            result2 = cur.fetchall()
            for m in range(0, len(result2)):
                api_class_id, class_node = import_class_node(api_class_node, m, package_node, result2)

                # get method
                cur.execute("select * from api_method where class_id = %s", (api_class_id,))
                result3 = cur.fetchall()
                for n in range(0, len(result3)):
                    api_method_id, method_node = import_method_node(api_method_node, class_node, n, result3)

                    # get method parameter
                    cur.execute("select * from api_parameter where method_id = %s", (api_method_id,))
                    result4 = cur.fetchall()
                    for l in range(0, len(result4)):
                        import_parameter_node(api_parameter_node, l, method_node, result4)

                # get class variable
                cur.execute("select * from api_parameter where class_id = %s", (api_class_id,))
                result3 = cur.fetchall()
                for n in range(0, len(result3)):
                    import_field_node(api_field_node, class_node, n, result3)
        logger.info("complete for android sdk api %s", str(library_id))
    print "reading from mysql completed!"
    logger.info("reading from mysql completed! %s-%s", str(start), str(end))


def import_library_node(android_sdk_node, i, result):
    library_id = result[i][0]
    library_name = result[i][1]
    library_orgnization = result[i][2]
    library_introduction = result[i][3]
    library_version = result[i][4]
    library_license = result[i][7]
    library_doc_website = result[i][8]
    labels = ["library"]
    property_dict = {"name": "android API " + str(library_id),
                     "library name": library_name,
                     "library id": str(library_id),
                     "organization": library_orgnization,
                     "introduction": library_introduction,
                     "version": library_version,
                     "license": library_license,
                     "doc website": library_doc_website}
    from skgraph.graph.accessor.factory import NodeBuilder
    nodeBuilder = NodeBuilder().add_labels(*labels).add_property(**property_dict)
    print library_id
    # insert lib to neo4j
    lib_node = nodeBuilder.build()
    connect_graph.merge(lib_node, "library", property_dict.keys())
    connect_graph.create(get_instance_of_relation(lib_node, android_sdk_node))
    connect_graph.create(Relationship(android_sdk_node, 'version', lib_node))
    return lib_node, library_id


def import_package_node(api_package_node, j, lib_node, result1):
    api_package_id = result1[j][0]
    api_package_name = result1[j][1]
    api_package_first_version = result1[j][2]
    api_package_description = result1[j][3]
    api_package_doc_website = result1[j][4]
    logger.info("complete %s : %s", api_package_id, api_package_name)
    print api_package_id, "     ", api_package_name
    labels = ["api package"]
    property_dict = {
        "package id": api_package_id,
        "name": api_package_name,
        "package name": api_package_name,
        "first version": api_package_first_version,
        "description": api_package_description,
        "doc website": api_package_doc_website
    }
    nodeBuilder = NodeBuilder().add_labels(*labels).add_property(**property_dict)
    package_node = nodeBuilder.build()
    connect_graph.merge(package_node)
    connect_graph.create(get_instance_of_relation(package_node, api_package_node))
    connect_graph.create(Relationship(package_node, 'belong to', lib_node))
    return api_package_id, package_node


def import_class_node(api_class_node, m, package_node, result2):
    api_class_id = result2[m][0]
    api_class_name = result2[m][1]
    api_class_first_version = result2[m][6]
    api_class_type = result2[m][8]
    api_class_doc_website = result2[m][10]
    api_class_description = result2[m][3]
    labels = ["api class"]
    property_dict = {
        "class id": api_class_id,
        "name": api_class_name,
        "class name": api_class_name,
        "first version": api_class_first_version,
        "class type": api_class_type,
        "doc website": api_class_doc_website,
        "description": api_class_description
    }
    nodeBuilder = NodeBuilder().add_labels(*labels).add_property(**property_dict)
    # insert package to neo4j
    class_node = nodeBuilder.build()
    connect_graph.merge(class_node)
    connect_graph.create(get_instance_of_relation(class_node, api_class_node))
    connect_graph.create(Relationship(class_node, 'belong to', package_node))
    return api_class_id, class_node


def import_method_node(api_method_node, class_node, n, result3):
    api_method_id = result3[n][0]
    api_method_name = result3[n][1]
    api_method_ret = result3[n][5]
    api_method_first_version = result3[n][6]
    api_method_is_static = result3[n][7]
    labels = ["api method"]
    property_dict = {
        "method id": api_method_id,
        "name": api_method_name,
        "method name": api_method_name,
        "first version": api_method_first_version,
        "static": api_method_is_static,
        "return": api_method_ret

    }
    nodeBuilder = NodeBuilder().add_labels(*labels).add_property(**property_dict)
    # insert method to neo4j
    method_node = nodeBuilder.build()
    connect_graph.merge(method_node)
    connect_graph.create(get_instance_of_relation(method_node, api_method_node))
    connect_graph.create(Relationship(method_node, 'belong to', class_node))
    return str(api_method_id), method_node


def import_parameter_node(api_parameter_node, l, method_node, result4):
    api_parameter_id = result4[l][0]
    api_parameter_name = result4[l][1]
    api_parameter_type = result4[l][5]
    api_parameter_first = result4[l][6]
    labels = ["api method parameter"]
    property_dict = {
        "parameter id": api_parameter_id,
        "name": api_parameter_name,
        "parameter name": api_parameter_name,
        "parameter type": api_parameter_type,
        "parameter first": api_parameter_first,
    }
    nodeBuilder = NodeBuilder().add_labels(*labels).add_property(**property_dict)
    # insert parameter to neo4j
    parameter_node = nodeBuilder.build()
    connect_graph.merge(parameter_node)
    connect_graph.create(get_instance_of_relation(parameter_node, api_parameter_node))
    connect_graph.create(Relationship(parameter_node, 'belong to', method_node))


def import_field_node(api_field_node, class_node, n, result3):
    api_parameter_id = result3[n][0]
    api_parameter_name = result3[n][1]
    api_parameter_type = result3[n][5]
    api_parameter_first = result3[n][6]
    labels = ["api field"]
    property_dict = {
        "field id": api_parameter_id,
        "name": api_parameter_name,
        "parameter name": api_parameter_name,
        "parameter type": api_parameter_type,
        "parameter first": api_parameter_first,
    }
    nodeBuilder = NodeBuilder().add_labels(*labels).add_property(**property_dict)
    # insert field to neo4j
    field_node = nodeBuilder.build()
    connect_graph.merge(field_node)
    connect_graph.create(get_instance_of_relation(field_node, api_field_node))
    connect_graph.create(Relationship(field_node, 'belong to', class_node))


# write into neo4j
def init_concept_node():
    api_package_node = NodeBuilder().add_labels("api concept").add_property(name="api package").build()
    api_class_node = NodeBuilder().add_labels("api concept").add_property(name="api class").build()
    api_method_node = NodeBuilder().add_labels("api concept").add_property(name="api method").build()
    api_field_node = NodeBuilder().add_labels("api concept").add_property(name="api field").build()
    api_parameter_node = NodeBuilder().add_labels("api concept").add_property(name="api method parameter").build()
    subgraph = api_package_node | api_class_node | api_method_node | api_field_node | api_parameter_node
    connect_graph.merge(subgraph)
    print "init concept node to neo4j completed!"


# complete relation in neo4j
def complete_relation():
    cur.execute("select api_class_id,extend_class from api_class")
    result = cur.fetchall()
    for r in result:
        api_class_id = r[0]
        extend_class = r[1]
        if extend_class is not None:
            node1 = connect_graph.find_one(
                label="api_class",
                property_key="api_class_id",
                property_value=api_class_id
            )
            node2 = connect_graph.find_one(
                label="api_class",
                property_key="api_class_id",
                property_value=extend_class
            )
            if node1 is not None and node2 is not None:
                connect_graph.create(Relationship(node1, 'extends', node2))

    print "extends complete!"
    logger.info("extends complete!")


# using multiple products and multiple consumer model

# for i in range(3):
#     t=threading.Thread(target=mySQLReader,args=(i*500,(i+1)*500,))
#     t.start()
#
# for j in range(20):
#     t=threading.Thread(target=neo4jWriter,args=())
#     t.start()
# neo4jWriter()
# mySQLReader(25,28)
# completeR()

if __name__ == "__main__":
    conn = MySQLdb.connect(
        host='10.141.221.73',
        port=3306,
        user='root',
        passwd='root',
        db='fdroid',
        charset="utf8"
    )
    cur = conn.cursor()

    init_concept_node()
    mySQLReader(25, 27)
    complete_relation()
    cur.close()
    conn.close()
