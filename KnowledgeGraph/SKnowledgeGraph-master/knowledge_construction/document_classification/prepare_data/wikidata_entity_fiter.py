import codecs
import json


class WikiDataEntityFilter:
    UNKNOWN = 0
    VALID = 1
    INVALID = -1
    illegal_keywords = [
        "YouTube",
        "metro station",
        "station",
        "metro",
        "subway",
        "street in",
        "Wikimedia",
        "day in",
        "city in",
        "city of",
        "city"
        "bridge",
        "town",
        "politician",
        "actor",
        "Czech",
        "actress",
        "capital",
        "road in",
        "footballer",
        "river",
        "historical",
        "history",
        "politics",
        "government",
        "politician",
        "politicians",
        "transit",
        "country",
        "counties",
        "transport",
        "road",
        "railway",
        "urban",
        "gods",
        "disambiguation page",
        "mythical",
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
        "Monday",

        "Tuesday",
        "Wednesday", "Thursday", "Friday", "Saturday", "Sunday",
        "fiction",
        "infrastructure",
        "game",
        "film",
        "rail",
        "bank",
        "financial",
        "trade",
        "biomedical",
        "physiological",
        "electoral",
        "disease",
        "pictogram",
        "Spanish",
        "book",
        "art",
        "novel",
        "country",
        "star",
        "channels",
        "channel",
        "novels",
        "books",
        "company"
        "Awards",
        "award",
        "ceremony"
    ]
    illegal_property = [
        "point in time",
        "material used",
        "sex or gender",
        "given name",
        "day of week",
        "coordinate location",
        "country",
        "country",
        "chemical formula",
        "locator map image",
        "mother",
        "father",
        "novel",
        "book",
        "art",
        "terminus",
        "located in the administrative territorial entity",

    ]
    illegal_property_value = [
        "Q4504495",#award ceremony (Q4504495)
        "Q866",#YouTube (Q866)
        "Q17558136",#YouTube channel (Q17558136)
        "Q7189713",  # physiological condition (Q7189713)
        "Q2057971",  # health problem (Q2057971)
        "Q16745563",  # biomedical sciences (Q16745563)
        "Q19793459",  # "Q19793459" Greek letter (Q19793459)
        "Q571",  # book(Q571)
        "Q940709",  # Chinese Chan
        "Q7163",  # politics(Q7163)
        "Q413",  # physics
        "Q338",  #
        "Q5503",  # ""subterranean rapid transit (Q5503)"
        "Q371580",  # List of hangul jamo (Q371580)
        "Q2312410",  # sport discipline
        "Q35127",  # website
        "Q7889",  # video game
        "Q2249149",  # electronic game
        "Q18199879",  # trolleybus system
        "Q924286",  # transport network
        "Q7574076",  # spatial network
        "Q847906",  # online magazine
        "Q18325841",  # public transport network
        "Q1454986"  # physical system
        "Q2678338",  # railway network
        "Q928830",  # metro station
        "Q12617225",  # religious literature
        "Q49008",  # prime number
        "Q223393",  # literary genre
        "Q8242",  # literature
        "Q861716",  # visual culture
        "natural",  # number(Q21199)
        "Q483394",  # genre
        "Q4167410",  # Wikimedia disambiguation page (Q4167410)
        "Q3024240",  # historical country (Q3024240)
        "Q6256",  # country (Q6256)
        "Q188473",  # action film (Q188473)
        "Q201658",  # film genre (Q201658)
        "Q484830",  # barter (Q484830)
        "Q1322005",  # natural phenomenon (Q1322005)
        "Q483247",  # phenomenon (Q483247)
        "Q11042",  # culture(Q11042)
        "Q735",  # art(Q735)
        # art genre(Q1792379)
        #           S - Bahn(Q95723)
        # commuter rail(Q1412403)
        # rail guided transport(Q350783)
        # religious text(Q179461)
        # rail transport(Q3565868)
        # timeline(Q186117)
        # novel(Q8261)
        # magazine genre(Q21114848)
        # literary genre(Q223393)
        # music genre(Q188451)
        # audio drama genre(Q2933978)
        # Hamburg U - Bahn(Q6445)
        # metro system(Q15099348)
        # urban rail transit(Q3491904)
        # railway network(Q2678338)
        # traffic system(Q2516436)
        # railroad(Q22667)
        # public transport network(Q18325841)
        # public transport(Q178512)
        # public service(Q161837)
        # transport network(Q924286)
        "Q5",  # human (Q5)
        "Q1808963",
        "Q192388",  # "position (Q192388)"
        "Q50716",
        "Q8205328",  # artificial physical object (Q8205328)
        "Q987767",  # container (Q987767)
        "Q22687",  # bank (Q22687)
        # periodic table(Q10693)
        "Q358078",  # road network(Q358078)
        "Q376799",  # transport infrastructure(Q376799)
        # infrastructure(Q121359)
        # physical system(Q1454986)
        # geographical object(Q618123)
        # Seville Metro(Q309023)
        # Plan(Q24013340)
        # rapid transit(Q15665807)
        # Philadelphia Metro(Q11158637)
        # national timeline(Q18043430)
        # Wikimedia timeline article(Q18340550)
        # chronicle(Q185363)
        # national timeline(Q18043430)
        # timeline(Q186117)
        # tram system(Q15640053)
        # train category(Q16858238)
        # regional rail(Q14626453)
        # physical quantity(Q107715)
        #
        # road(Q34442)
        # physical phenomenon(Q1293220)
        # thoroughfare(Q83620)
        # fundamental interaction(Q104934)
        # vector physical quantity(Q2672914)
        # public space(Q294440)
        # thoroughfare(Q83620)
        # Provincial route(Q2316398)
        # Seoul Metropolitan Subway(Q16950)
        # German autobahn(Q313301)
        # physical system(Q1454986)
        # astronomical object type(Q17444909)
        # planetary system(Q206717)
        #           controlled - access highway(Q46622)
        # highway(Q269949)
        # highway system(Q25631158)
        # inner Solar System(Q7879772)
        # location(Q17334923)
        # coordinate system(Q11210)
        #
        # nucleus(Q40260)
        # cellular component(Q5058355)
        # biological component(Q28845870)
        # Lenta.ru(Q658909)
        # nuclear part(Q22325677)
        # clade(Q713623)
        # DNA(Q7430)
        "Q526877",  # web series(Q526877)
        #           F - type main - sequence star(Q1353952)
        # main sequence(Q3450)
        "Q595871",  # star system(Q595871)

        "Q39911",  # decade (Q39911)
        "Q523",  # star
        "Q1007122",  # G - type star(
        "Q179600",  # stellar classification(
        "Q25696292",  # astronomy classification
        "Q244393",  # dwarf star(
        "Q995268",  # F - type star(
        "Q21104773",  # nuclear transcription factor complex(
        "Q16686022",  # natural physical object(
        "Q1385",  # cerium(
        "Q15079663",  # metro line (Q15079663)
        "Q11344",  # chemical element
        "Q2239243",  # creature
        "Q47150325",  # calendar day of a given year (
        "Q47018478",  # calendar month of a given year (
        "Q8142",  # money
        "Q5503",  # subterranean rapid transit (Q5503)
        'Q15640053', 'Q18199879', 'Q924286', 'Q7574076', 'Q847906', 'Q18325841',
        'Q1454986', 'Q2678338',
        'Q928830', 'Q735', 'Q12617225', 'Q49008', 'Q223393', 'Q8242', 'Q861716',

        "Q18199879", "Q924286", "Q7574076", "Q847906", "Q18325841", "Q1454986", "Q2678338", "Q928830", "Q735",
        "Q12617225", "Q49008", "Q223393", "Q8242", "Q861716", "Q21199", "Q483394", "Q11042", "Q1792379", "Q95723",
        "Q1412403", "Q350783", "Q179461", "Q3565868", "Q186117", "Q8261", "Q21114848", "Q223393", "Q188451",
        "Q2933978", "Q6445", "Q15099348", "Q3491904", "Q2678338", "Q2516436", "Q22667", "Q18325841", "Q178512",
        "Q161837", "Q924286", "Q1808963", "Q7722511", "Q5499", "Q10693", "Q358078", "Q376799", "Q121359", "Q1454986",
        "Q618123", "Q309023", "Q24013340", "Q15665807", "Q18340550", "Q185363", "Q18043430", "Q186117", "Q15640053",
        "Q16858238", "Q14626453", "Q107715", "Q34442", "Q1293220", "Q83620", "Q104934", "Q2672914", "Q294440",
        "Q83620", "Q2316398", "Q16950", "Q313301", "Q1454986", "Q17444909", "Q206717", "Q46622", "Q269949",
        "Q25631158", "Q7879772", "Q17334923", "Q11210", "Q40260", "Q5058355", "Q28845870", "Q658909", "Q22325677",
        "Q713623", "Q7430", "Q526877", "Q3450", "Q595871", "Q523", "Q1007122", "Q179600", "Q25696292", "Q244393",
        "Q995268", "Q21104773", "Q16686022", "Q1385", "Q11344"
    ]

    legal_keywords = [
        "processor",
        "CPU",
        "software",
        "website",
        "programming language",
        "library",
        "library",
        "computer",
        "operating system",
        "developer",
        "platform",
        "Stack Exchange tag",
        "algorithm",
        "Internet",
        "mathematics",
        "network",
        "networks",
        "programming",
        "deep learning",
        "machine learning"
        "algorithmics",
        "algorithms",
        "data structures",
        "Web ",
        "protocol",
        "data structure",
        "Database",
        "problem",
        "file",
        "Windows",
        "Mac OS",
        "Linux",
        "Java",
        "C++",
        "Python",
        "c#",
        "development",
        "computational",
        "variable",
        "programmer",
        "programmers",
        "coding",
        "code",
        "integers",
        "integer",
        "mathematical",
        "data",
        "statistical",
    ]
    legal_value = [
        "Q173212",#"computer architecture (Q173212)"
        "Q11348",  # ""function (Q11348)"
        "Q9135",  # operation system
        'Q28530532',  # type of software
        'Q7397',  # software
        'Q21198',  # computer science
        'Q1301371',  # computer network
        "Q9143",  # programming language
        "Q8366",  # algorithm
        "Q175263",  # data structure
        "Q15836568",  # computer network protocol (
        "Q192776",  # artificial neural network
        "Q13636890",  # algorithmics (
        "Q2115",  # Extensible Markup Language
        "Q235557",  # file format
        "Q629206",  # computer language
        "Q3435924",  # computational problem (
        "Q1166625",  # mathematical problem (Q1166625)

    ]
    legal_property=["defining formula"
                    ]
    def __init__(self):
        pass

    def get_node_state(self, node):
        if self.is_valid(node):
            return self.VALID
        if self.is_invalid(node):
            return self.INVALID
        return self.UNKNOWN

    def start_filter(self, input_file, output_file):

        wikidata_ndoe_list = self.read_json(input_file)

        valid_nodes = []
        invalid_nodes = []
        unknown_nodes = []
        for record in wikidata_ndoe_list:
            state = self.get_node_state(record)
            if state == self.VALID:
                valid_nodes.append(record)

            if state == self.INVALID:
                invalid_nodes.append(record)
            if state == self.UNKNOWN:
                unknown_nodes.append(record)
                print("id=%s name=%s wd_id=%s url=%s description=%s property=%s" % (
                    record["graph_id"], record["wikidata_name"],
                    record["wikidata_id"], record["wikipedia_url"],
                    record["wikidata_description"], record["properties"]))

        self.write_json(output_file + ".invalid", invalid_nodes)
        self.write_json(output_file + ".unknown", unknown_nodes)
        self.write_json(output_file + ".valid", valid_nodes)
        print("valid=%s" % len(valid_nodes))
        print("invalid=%s" % len(invalid_nodes))
        print("unknown=%s" % len(unknown_nodes))

    def write_json(self, output_path, node_list):
        with codecs.open(output_path, 'w', 'utf-8') as f:
            json.dump(node_list, f)

    def read_json(self, input_file):
        with codecs.open(input_file, 'r', 'utf-8') as f:
            node_list = json.load(f)
            return node_list

    def is_valid(self, record):
        for word in self.legal_keywords:
            if word.lower() in record["wikidata_description"].lower() or word.lower() in record["wikidata_name"]:
                return True
        for word in self.legal_keywords:
            if word.lower() in record["wikipedia_url"].lower():
                return True
        for property, value in record["properties"]:
            if property in self.legal_keywords:
                return True
            if property in self.legal_property:
                return True

            for word in property.split():
                if word in self.legal_keywords:
                    return True
            team = []
            if type(value) == str:
                team = [value]
            if type(value) == list:
                team = value
            for value in team:
                if value in self.legal_value:
                    return True
        return False

    def is_invalid(self, record):
        if record["wikidata_name"].isdigit():
            return True
        for word in self.illegal_keywords:
            if word.lower() in record["wikidata_description"].lower().split(" ") or word.lower() in record[
                "wikidata_name"].split(" "):
                return True
        for word in self.illegal_keywords:
            if word.lower() in record["wikipedia_url"].lower():
                return True
        for property, value in record["properties"]:
            if property in self.illegal_property:
                return True
            team = []
            if type(value) == str:
                team = [value]
            if type(value) == list:
                team = value

            for value in team:
                if value in self.illegal_property_value:
                    return True
        return False


if __name__ == "__main__":
    input_file = "with_properties_wikidata_software_entity.json"
    # output_file = "filter_wikidata_software_entity.json"
    output_file = "filter_with_properties_wikidata_software_entity.json"
    filter = WikiDataEntityFilter()
    filter.start_filter(input_file=input_file, output_file=output_file)
