import MySQLdb
from py2neo import Node, Relationship

# Mysql connect
from skgraph.graph.accessor.graph_accessor import DefaultGraphAccessor, GraphClient

conn = MySQLdb.connect(
    host='10.141.221.73',
    port=3306,
    user='root',
    passwd='root',
    db='fdroid',
    charset="utf8"
)
cur = conn.cursor()

# neo4j connect
connect_graph = DefaultGraphAccessor(GraphClient(server_number=1)).graph


# read from mysql
def mySQLReader(start, end):
    node1lib = connect_graph.find_one(
        label="library",
        property_key="wd_item_id",
        property_value="Q21127166"
    )
    node2package = connect_graph.find_one(
        label="api concept",
        property_key="name",
        property_value="api package"
    )
    node3class = connect_graph.find_one(
        label="api concept",
        property_key="name",
        property_value="api class"
    )
    node4method = connect_graph.find_one(
        label="api concept",
        property_key="name",
        property_value="api method"
    )
    node5field = connect_graph.find_one(
        label="api concept",
        property_key="name",
        property_value="api field"
    )
    node6para = connect_graph.find_one(
        label="api concept",
        property_key="name",
        property_value="api method parameter"
    )

    # get all-version library
    cur.execute("select * from jdk_library where library_id >= %s and library_id < %s", (start, end))
    result = cur.fetchall()
    for i in range(0, len(result)):
        library_id = result[i][0]
        library_name = result[i][1]
        library_orgnization = result[i][2]
        library_version = result[i][3]
        library_doc_website = result[i][4]
        print library_id
        # insert lib to neo4j
        lib_node = Node("api library", library_id=library_id, library_name=library_name,
                        library_orgnization=library_orgnization, library_version=library_version,
                        library_doc_website=library_doc_website)
        connect_graph.create(lib_node)
        connect_graph.create(Relationship(lib_node, 'instance of', node1lib))
        connect_graph.create(Relationship(lib_node, 'version', node1lib))

        # get package
        cur.execute("select * from jdk_package where library_id = %s", library_id)
        result1 = cur.fetchall()
        for j in range(0, len(result1)):
            api_package_id = result1[j][0]
            api_package_name = result1[j][1]
            api_package_first_version = result1[j][2]
            api_package_description = result1[j][3]
            api_package_doc_website = result1[j][4]
            print api_package_id, "     ", api_package_name

            # insert package to neo4j
            package_node = Node("api package", api_package_id=api_package_id, api_package_name=api_package_name,
                                api_package_first_version=api_package_first_version,
                                api_package_description=api_package_description,
                                api_package_doc_website=api_package_doc_website)
            connect_graph.create(package_node)
            connect_graph.create(Relationship(package_node, 'instance of', node2package))
            connect_graph.create(Relationship(package_node, 'belong to', lib_node))

            # get class
            cur.execute("select * from jdk_class where package_id = %s", api_package_id)
            result2 = cur.fetchall()
            for m in range(0, len(result2)):
                api_class_id = result2[m][0]
                api_class_name = result2[m][1]
                api_class_first_version = result2[m][6]
                api_class_type = result2[m][8]
                api_class_doc_website = result2[m][9]
                api_class_description = result2[m][3]

                # insert package to neo4j
                class_node = Node("api class", api_class_id=api_class_id,
                                  api_class_name=api_class_name,
                                  api_class_first_version=api_class_first_version,
                                  api_class_type=api_class_type,
                                  api_class_doc_website=api_class_doc_website,
                                  api_class_description=api_class_description)
                connect_graph.create(class_node)
                connect_graph.create(Relationship(class_node, 'instance of', node3class))
                connect_graph.create(Relationship(class_node, 'belong to', package_node))

                # get method
                cur.execute("select * from jdk_method where class_id = %s", api_class_id)
                result3 = cur.fetchall()
                for n in range(0, len(result3)):
                    api_method_id = result3[n][0]
                    api_method_type = result3[n][1]
                    api_method_name = result3[n][2]
                    api_method_declaration = result3[n][3]
                    api_method_retType = result3[n][4]
                    api_method_retStr = result3[n][5]
                    api_method_des = result3[n][6]
                    api_method_first_version = result3[n][7]
                    api_method_is_static = result3[n][8]
                    api_method_override = result3[n][9]
                    api_method_specify = result3[n][10]

                    if "Field" == api_method_type:
                        # insert field to neo4j
                        method_node = Node("api field", api_field_id=api_method_id, api_field_type=api_method_type,
                                           api_field_name=api_method_name,
                                           api_field_declaration=api_method_declaration,
                                           api_field_retType=api_method_retType,
                                           api_field_retString=api_method_retStr,
                                           api_field_description=api_method_des,
                                           api_field_first_version=api_method_first_version,
                                           api_field_is_static=api_method_is_static,
                                           api_field_override=api_method_override,
                                           api_field_specifyBy=api_method_specify)
                        connect_graph.create(method_node)
                        connect_graph.create(Relationship(method_node, 'instance of', node5field))
                        connect_graph.create(Relationship(method_node, 'belong to', class_node))
                    else:
                        # insert method to neo4j
                        method_node = Node("api method", api_method_id=api_method_id, api_method_type=api_method_type,
                                           api_method_name=api_method_name,
                                           api_method_declaration=api_method_declaration,
                                           api_method_retType=api_method_retType,
                                           api_method_retString=api_method_retStr,
                                           api_method_description=api_method_des,
                                           api_method_first_version=api_method_first_version,
                                           api_method_is_static=api_method_is_static,
                                           api_method_override=api_method_override,
                                           api_method_specifyBy=api_method_specify)
                        connect_graph.create(method_node)
                        connect_graph.create(Relationship(method_node, 'instance of', node4method))
                        connect_graph.create(Relationship(method_node, 'belong to', class_node))

                    # get method parameter
                    cur.execute("select * from jdk_parameter where method_id = %s", api_method_id)
                    result4 = cur.fetchall()
                    for l in range(0, len(result4)):
                        api_parameter_id = result4[l][0]
                        api_parameter_name = result4[l][1]
                        api_parameter_type_class = result4[l][4]
                        api_parameter_type_string = result4[l][5]
                        api_parameter_description = result4[l][6]

                        # insert parameter to neo4j
                        parameter_node = Node("api method parameter", api_parameter_id=api_parameter_id,
                                              api_parameter_name=api_parameter_name,
                                              api_parameter_type_class=api_parameter_type_class,
                                              api_parameter_type_string=api_parameter_type_string,
                                              api_parameter_description=api_parameter_description)
                        connect_graph.create(parameter_node)
                        connect_graph.create(Relationship(parameter_node, 'instance of', node6para))
                        connect_graph.create(Relationship(parameter_node, 'belong to', method_node))

    print "reading from mysql completed!"


# write into neo4j
def neo4jWriter():
    node1 = Node("library", name="api library", wd_item_id='Q21127166')
    connect_graph.create(node1)
    node2 = Node("api concept", name="api package")
    connect_graph.create(node2)
    node3 = Node("api concept", name="api class")
    connect_graph.create(node3)
    node4 = Node("api concept", name="api method")
    connect_graph.create(node4)
    node5 = Node("api concept", name="api field")
    connect_graph.create(node5)
    node6 = Node("api concept", name="api method parameter")
    connect_graph.create(node6)

    print "writing to neo4j completed!"


# complete relation in neo4j
def completeR():
    cur.execute("select api_class_id,extend_class from api_class")
    result = cur.fetchall()
    for r in result:
        api_class_id = r[0]
        extend_class = r[1]
        if extend_class != None:
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
            if node1 != None and node2 != None:
                connect_graph.create(Relationship(node1, 'extends', node2))

    print "extends complete!"
    node3 = connect_graph.find_one(
        label="api_concept",
        property_key="name",
        property_value="api_library")
    node4 = Node("api_concept", name="programming_language")
    connect_graph.create(node4)
    node5 = Node("programming_language", name="Android")
    connect_graph.create(node5)
    connect_graph.create(Relationship(node5, 'instanceOf', node4))
    connect_graph.create(Relationship(node3, 'use', node4))

    nodes = connect_graph.find(label="api_library")
    for node in nodes:
        connect_graph.create(Relationship(node, 'use', node5))

    print "relation complete!"


mySQLReader(4, 5)
cur.close()
conn.close()
