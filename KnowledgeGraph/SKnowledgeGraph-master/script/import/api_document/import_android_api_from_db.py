import MySQLdb
from py2neo import Node, Relationship
from skgraph.graph_util.question_answer_system.graph.accessor.graph_accessor import DefaultGraphAccessor, GraphClient

from skgraph.graph.accessor.factory import NodeBuilder
# Mysql connect
from skgraph.util import clean_html_text

conn = MySQLdb.connect(
    host='10.141.221.75',
    port=3306,
    user='root',
    passwd='root',
    db='knowledgeGraph',
    charset="utf8"
)
cur = conn.cursor()

# neo4j connect
connect_graph = DefaultGraphAccessor(GraphClient(server_number=1)).graph


# read from mysql
def mySQLReader():
    lib_node = create_android_api_node(27)

    # get package
    cur.execute("select * from androidAPI_package")
    package_sql_data_list = cur.fetchall()

    for package_node_sql_data_index in range(0, len(package_sql_data_list)):
        api_package_id, package_node = create_package_node_from_mysql(package_node_sql_data_index, lib_node,
                                                                      package_sql_data_list)

        # get class
        cur.execute("select * from androidAPI_class where Package_id = %s", (api_package_id,))
        class_node_sql_data_list = cur.fetchall()
        for class_node_sql_data_index in range(0, len(class_node_sql_data_list)):
            api_class_id, class_node = create_class_node_from_mysql(class_node_sql_data_index, package_node,
                                                                    class_node_sql_data_list)

            # get method
            cur.execute("select * from androidAPI_method where Class_id = %s", (api_class_id,))
            method_node_sql_data_list = cur.fetchall()
            for method_node_sql_data_index in range(0, len(method_node_sql_data_list)):
                api_method_id, method_node, api_method_retType = create_method_node_from_mysql(
                    class_node,
                    method_node_sql_data_index,
                    method_node_sql_data_list)
                # # get method parameter
                #         create_return_value_node_from_mysql(api_method_retStr, api_method_retType, method_node)
                #
                #         cur.execute("select * from jdk_parameter where method_id = %s", (api_method_id,))
                #         parameter_node_sql_data_list = cur.fetchall()
                #         for parameter_node_sql_data_index in range(0, len(parameter_node_sql_data_list)):
                #             create_parameter_node_from_mysql(parameter_node_sql_data_index, method_node,
                #                                              parameter_node_sql_data_list)
                #
                #         cur.execute("select * from jdk_exception where method_id = %s", (api_method_id,))
                #         exception_node_sql_data_list = cur.fetchall()
                #         for exception_node_sql_data_index in range(0, len(exception_node_sql_data_list)):
                #             create_throw_exception_node_from_mysql(exception_node_sql_data_index, method_node,
                #                                                    exception_node_sql_data_list)

    print "reading from mysql completed!"


# def create_return_value_node_from_mysql(api_method_retStr, api_method_retType, method_node):
#     if api_method_retStr is not None and api_method_retStr != "":
#         api_return_value_node = NodeBuilder(). \
#             add_label("api"). \
#             add_label("java method return value"). \
#             add_label("entity"). \
#             add_one_property("name", api_method_retStr). \
#             add_one_property("value description", api_method_retStr). \
#             add_one_property("value type", api_method_retType). \
#             build()
#         connect_graph.merge(api_return_value_node)
#         connect_graph.merge(Relationship(method_node, 'return', api_return_value_node))


# def create_parameter_node_from_mysql(l, method_node, result4):
#     api_parameter_id = result4[l][0]
#     api_parameter_name = result4[l][1]
#     api_parameter_type_class_id = result4[l][4]
#     api_parameter_type_string = result4[l][5]
#     api_parameter_description = result4[l][6]
#     api_parameter_description = clean_html_text(api_parameter_description)
#     try:
#         sql = "select name,doc_website from jdk_class where class_id = " + str(
#             api_parameter_type_class_id)
#         cur.execute(sql)
#         class_query = cur.fetchone()
#         class_name = class_query[0]
#         class_doc_website = class_query[1]
#     except Exception:
#         traceback.print_exc()
#         class_name = None
#         class_doc_website = None
#     parameter_node = NodeBuilder(). \
#         add_label("api"). \
#         add_label("java method parameter"). \
#         add_one_property("name", api_parameter_type_string + " " + api_parameter_name). \
#         add_one_property("formal parameter name", api_parameter_name). \
#         add_one_property("formal parameter type", api_parameter_type_string). \
#         add_one_property("description", api_parameter_description). \
#         add_one_property("formal parameter type document website", class_doc_website). \
#         add_one_property("formal parameter type full name", class_name). \
#         build()
#     connect_graph.merge(parameter_node)
#     # connect_graph.merge(Relationship(parameter_node, 'instance of', node6para))
#     connect_graph.merge(Relationship(parameter_node, 'belong to', method_node))


# def create_throw_exception_node_from_mysql(l, method_node, result4):
#     # todo complete
#     exception_id = result4[l][0]
#     exception_name = result4[l][1]
#     throw_exception_description = result4[l][4]
#     throw_exception_description = clean_html_text(throw_exception_description)
#     throw_exception_node = NodeBuilder(). \
#         add_label("api"). \
#         add_label("entity"). \
#         add_label("java throw exception description"). \
#         add_one_property("name", throw_exception_description). \
#         add_one_property("exception type", exception_name). \
#         add_one_property("description", throw_exception_description). \
#         build()
#     connect_graph.merge(throw_exception_node)
#     connect_graph.merge(Relationship(method_node, 'throw exception description', throw_exception_node))
#     if method_node["throw exception"] is not None:
#         method_node["throw exception"] = method_node["throw exception"].append(exception_name)
#     else:
#         exception_list = [exception_name]
#         method_node["throw exception"] = exception_list
#     connect_graph.push(method_node)


def create_method_node_from_mysql(class_node, methode_node_mysql_data_index, result3):
    # todo complete

    api_method_id = result3[methode_node_mysql_data_index][0]
    api_method_name = result3[methode_node_mysql_data_index][1]
    api_method_short_description = result3[methode_node_mysql_data_index][2]

    api_method_ret_value_type = result3[methode_node_mysql_data_index][6]
    api_method_type = result3[methode_node_mysql_data_index][7]
    api_method_add_in_api_level = result3[methode_node_mysql_data_index][8]
    api_method_declaration = result3[methode_node_mysql_data_index][9]

    static_method = False
    abstract_method = False
    final_method = False
    if api_method_ret_value_type == "null":
        api_method_ret_value_type = None
    else:
        api_method_ret_value_type = clean_html_text(api_method_ret_value_type)
        method_return_value_type_split_word = api_method_ret_value_type.split(" ")
        if len(method_return_value_type_split_word) > 1:
            api_method_ret_value_type = method_return_value_type_split_word[-1]
            if "static" in method_return_value_type_split_word:
                static_method = True
            if "final" in method_return_value_type_split_word:
                final_method = True
            if "abstract" in method_return_value_type_split_word:
                abstract_method = True

    if api_method_short_description == "null":
        api_method_short_description = None
    else:
        api_method_short_description = clean_html_text(api_method_short_description)

    api_type = "Method"
    api_permissions_modifier = None

    if api_method_type == "null":
        api_method_type = None
    else:
        if "constructor" in api_method_type:
            api_type = "Constructor"
        if "public" in api_method_type:
            api_permissions_modifier = "public"
        if "private" in api_method_type:
            api_permissions_modifier = "private"
        if "protected" in api_method_type:
            api_permissions_modifier = "protected"

    api_method_declaration = clean_html_text(api_method_declaration)
    if api_method_declaration:
        api_method_declaration = api_method_declaration.replace(" (", "(")

    method_node = NodeBuilder(). \
        add_entity_label(). \
        add_label("api"). \
        add_label("java " + api_type.lower()). \
        add_one_property("name", api_method_name). \
        add_one_property("method name", api_method_name). \
        add_one_property("api type", api_type). \
        add_one_property("method type", api_method_type). \
        add_one_property("declaration", api_method_declaration). \
        add_one_property("return value type", api_method_ret_value_type). \
        add_one_property("description", api_method_short_description). \
        add_one_property("first version", api_method_add_in_api_level). \
        add_one_property("added in API level", api_method_add_in_api_level). \
        add_one_property("permission modifier", api_permissions_modifier). \
        add_one_property("static method", static_method). \
        add_one_property("abstract method", abstract_method). \
        add_one_property("final method", final_method). \
        build()

    connect_graph.merge(method_node)
    # connect_graph.merge(Relationship(method_node, 'instance of', node5field))
    connect_graph.merge(Relationship(method_node, 'belong to', class_node))

    return api_method_id, method_node, api_method_ret_value_type


def create_class_node_from_mysql(class_node_mysql_data_index, package_node, result2):
    api_class_id = result2[class_node_mysql_data_index][0]
    api_class_name = result2[class_node_mysql_data_index][1]
    api_class_description = result2[class_node_mysql_data_index][2]
    api_class_add_in_level = result2[class_node_mysql_data_index][6]
    api_class_full_declaration = result2[class_node_mysql_data_index][7]
    api_class_parent_class = result2[class_node_mysql_data_index][8]

    package_url = package_node["name"].replace(".", "/")

    api_class_doc_website = "https://developer.android.com/reference/{package_name}/{class_name}.html".format(
        package_name=package_url, class_name=api_class_name)

    api_class_description = clean_html_text(api_class_description)
    api_class_full_declaration = clean_html_text(api_class_full_declaration)
    if api_class_parent_class == "null":
        api_class_parent_class = None

    class_node = NodeBuilder(). \
        add_entity_label(). \
        add_label("api"). \
        add_label("java class"). \
        add_one_property("name", api_class_name). \
        add_one_property("class name", api_class_name). \
        add_one_property("first version", api_class_add_in_level). \
        add_one_property("added in API level", api_class_add_in_level). \
        add_one_property("declaration", api_class_full_declaration). \
        add_one_property("api document website", api_class_doc_website). \
        add_one_property("description", api_class_description). \
        add_one_property("parent class", api_class_parent_class). \
        build()
    connect_graph.merge(class_node)
    # connect_graph.merge(Relationship(class_node, 'instance of', node3class))
    connect_graph.merge(Relationship(class_node, 'belong to', package_node))
    return api_class_id, class_node


def create_package_node_from_mysql(package_node_mysql_data_index, lib_node, result1):
    api_package_id = result1[package_node_mysql_data_index][0]
    api_package_name = result1[package_node_mysql_data_index][1].strip()
    api_package_short_description = result1[package_node_mysql_data_index][2]
    api_add_in_level = result1[package_node_mysql_data_index][3]

    package_url = api_package_name.replace(".", "/")

    api_package_doc_website = "https://developer.android.com/reference/{package_name}/package-summary.html".format(
        package_name=package_url)

    print api_package_id, "     ", api_package_name
    api_package_description = clean_html_text(api_package_short_description)
    package_node = NodeBuilder(). \
        add_entity_label(). \
        add_api_label(). \
        add_label("java package"). \
        add_one_property("name", api_package_name). \
        add_one_property("package name", api_package_name). \
        add_one_property("added in API level", api_add_in_level). \
        add_one_property("first version", api_add_in_level). \
        add_one_property("api document website", api_package_doc_website). \
        add_one_property("description", api_package_description). \
        build()
    # insert package to neo4j
    connect_graph.merge(package_node)
    api_add_in_level = result1[package_node_mysql_data_index][4]
    connect_graph.merge(Relationship(package_node, 'belong to', lib_node))
    package_node["added in API level"] = api_add_in_level
    package_node["first version"] = api_add_in_level
    connect_graph.push(package_node)
    return api_package_id, package_node


def create_all_android_api_node(start_api_level, end_api_level):
    android_software_development_node = NodeBuilder(). \
        add_label("wikidata"). \
        add_one_property("wd_item_id", "Q4759469"). \
        build()

    android_sdk_node = NodeBuilder(). \
        add_label("api"). \
        add_entity_label(). \
        add_label("java library"). \
        add_label("Android SDK"). \
        add_label("Android library"). \
        add_one_property("name", "Android SDK"). \
        add_one_property("alias", ["Android software development kit"]). \
        add_one_property("site:enwiki", "https://en.wikipedia.org/wiki/Android_software_development#SDK"). \
        build()
    android_api_node = NodeBuilder(). \
        add_label("api"). \
        add_entity_label(). \
        add_label("java library"). \
        add_label("Android SDK"). \
        add_label("Android library"). \
        add_one_property("name", "Android API"). \
        add_one_property("alias", ["Android Application Program Interface"]). \
        build()
    sdk_node = NodeBuilder(). \
        add_label("wikidata"). \
        add_one_property("wd_item_id", "Q467707"). \
        build()
    connect_graph.merge(android_sdk_node)
    connect_graph.merge(sdk_node)

    android_node = NodeBuilder(). \
        add_label("wikidata"). \
        add_one_property("wd_item_id", "Q94"). \
        build()
    connect_graph.merge(android_sdk_node)
    connect_graph.merge(sdk_node)
    connect_graph.merge(android_node)
    connect_graph.merge(android_api_node)
    connect_graph.merge(android_software_development_node)
    connect_graph.merge(Relationship(android_sdk_node, 'instance of', sdk_node))
    connect_graph.merge(Relationship(android_sdk_node, 'used for', android_software_development_node))

    connect_graph.merge(Relationship(android_sdk_node, 'provide', android_api_node))
    connect_graph.merge(Relationship(android_node, 'provide', android_api_node))

    for api_level in range(start_api_level, end_api_level):
        lib_node = NodeBuilder(). \
            add_label("api"). \
            add_entity_label(). \
            add_label("java library"). \
            add_label("Android SDK"). \
            add_one_property("name", "Android API " + str(api_level)). \
            add_one_property("api level", api_level). \
            add_one_property("api document website", "https://developer.android.com/reference/packages.html"). \
            build()
        # insert lib to neo4j
        connect_graph.merge(lib_node)

        connect_graph.merge(Relationship(android_sdk_node, 'provide', lib_node))
        connect_graph.merge(Relationship(android_node, 'support', lib_node))
        connect_graph.merge(Relationship(android_api_node, 'version', lib_node))


def create_android_api_node(api_level):
    lib_node = NodeBuilder(). \
        add_entity_label(). \
        add_label("api"). \
        add_label("java library"). \
        add_label("Android SDK"). \
        add_one_property("name", "Android API " + str(api_level)). \
        build()
    # insert lib to neo4j
    connect_graph.merge(lib_node)
    return lib_node


# write into neo4j
def schema_builder():
    node1 = Node("schema", "entity", name="Java software library", wd_item_id='Q21127166')
    connect_graph.merge(node1)
    node2 = Node("schema", "entity", name="java package")
    connect_graph.merge(node2)
    class_node = Node("schema", "entity", name="java class")
    connect_graph.merge(class_node)
    node4 = Node("schema", "entity", name="java method")
    connect_graph.merge(node4)
    node5 = Node("schema", "entity", name="java field")
    connect_graph.merge(node5)
    method_parameter_node = Node("schema", "entity", name="java method parameter")
    connect_graph.merge(method_parameter_node)

    exception_node = Node("schema", "entity", name="java exception")
    connect_graph.merge(exception_node)
    throw_exception_description_node = Node("schema", "entity", name="java throw exception description")
    connect_graph.merge(throw_exception_description_node)

    connect_graph.merge(Relationship(node2, 'belong to', node1))
    connect_graph.merge(Relationship(class_node, 'belong to', node2))
    connect_graph.merge(Relationship(node4, 'belong to', class_node))
    connect_graph.merge(Relationship(node5, 'belong to', class_node))
    connect_graph.merge(Relationship(method_parameter_node, 'belong to', node4))

    connect_graph.merge(Relationship(method_parameter_node, 'parameter type', class_node))
    connect_graph.merge(Relationship(class_node, 'return value type', class_node))
    connect_graph.merge(Relationship(method_parameter_node, 'throw', exception_node))
    connect_graph.merge(
        Relationship(method_parameter_node, 'throw exception description', throw_exception_description_node))

    node7 = Node("schema", "entity", name="package")
    connect_graph.merge(node7)
    connect_graph.merge(Relationship(node2, 'subclass of', node7))
    node8 = Node("schema", "entity", name="class")
    connect_graph.merge(node8)
    connect_graph.merge(Relationship(class_node, 'subclass of', node8))
    node9 = Node("schema", "entity", name="method")
    connect_graph.merge(node9)
    connect_graph.merge(Relationship(node4, 'subclass of', node9))
    node10 = Node("schema", "entity", name="field")
    connect_graph.merge(node10)
    connect_graph.merge(Relationship(node5, 'subclass of', node10))
    node11 = Node("schema", "entity", name="method parameter")
    connect_graph.merge(node11)
    connect_graph.merge(Relationship(method_parameter_node, 'subclass of', node11))

    print "writing concept to neo4j completed!"


schema_builder()
create_all_android_api_node(1, 28)
mySQLReader()
cur.close()
conn.close()
