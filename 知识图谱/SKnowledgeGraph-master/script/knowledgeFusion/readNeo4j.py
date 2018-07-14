import sys

from skgraph.graph.accessor.graph_accessor import DefaultGraphAccessor, GraphClient

## todo: do the real knowledge fusion here
reload(sys)
sys.setdefaultencoding('utf-8')

graphClient = DefaultGraphAccessor(GraphClient(server_number=1))

relations = None
ret = []

# f = open("PropertyAndRelation","r")
# lines = f.readlines()
# for line in lines:
#     line = line.replace("\n","")
#     searchObject = re.search(r'site:\w+ \((.+)\)',line)
#     if searchObject:
#         line = searchObject.group(1)
#     ret.append(unicode("<http://zhishi.me/zhwiki/resource/fdse_relation/"+line.replace(" ","_")+"> <http://www.w3.org/2000/01/rdf-schema#name> \""+line+"\" .\n"))
#
# file_object = open('thefile.nt', 'w')
# file_object.writelines(ret)
# file_object.close( )


result = graphClient.graph.run("MATCH (n:relation) RETURN n");
for r in result:
    ali = r["n"]["name"]
    if r["n"]["aliases_en"]:
        for alias in r["n"]["aliases_en"]:
            ali = ali + " " + alias
        ret.append("<http://zhishi.me/zhwiki/resource/fdse_relation/" + r["n"]["name"].replace(" ",
                                                                                               "_") + "> <http://www.w3.org/2000/01/rdf-schema#name> \"" + ali + "\" .\n")

file_object = open('test2.nt', 'w')
file_object.writelines(ret)
file_object.close()
