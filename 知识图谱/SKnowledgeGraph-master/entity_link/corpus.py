import codecs
import json

import nltk

"""

it' goal is to find the count from corpus.
FEAT = LOAD '$feat' USING PigStorage('\t') AS (
    alias:chararray,
    phrase_freq:long,
    alias_freq:long,
    entity_id:chararray,
    alias_entity_freq:long
);
{
    "alias":"",
    "link":"",
    "count":"",
}

"""


class EntityCFNameToEntityIDDataGenerate:
    def __init__(self):
        pass


"""
process the alias to lower case.
"""


class ProcessorAliasCount:

    def process_alias_2_entity_map(self, old_vocabulary_path, new_vocabulary_path):
        """
        process the alias in
        :return:
        """
        with codecs.open(old_vocabulary_path, 'r', 'utf-8') as f:
            vocabulary_list = json.load(f)

        new_vocabulary_list=[]
        for item in vocabulary_list:
            alias = item["alias"]
            if not alias:
                continue
            alias=alias.strip()
            new_tokenize_alias = nltk.word_tokenize(alias)

            new_alias = " ".join(new_tokenize_alias).lower()
            print("alias=", alias, " new_alias=", new_alias)

            item["alias"] = new_alias
            item["entity"] = item["link"]
            del item["link"]
            new_vocabulary_list.append(item)
        with codecs.open(new_vocabulary_path, 'w', 'utf-8') as f:
            json.dump(new_vocabulary_list, f)


class AliasStatisticsUtil:
    def __init__(self, alias_ngram=False):
        self.label_vocabulary = set([])
        self.max_label_length = 15
        self.alias_to_phrase_freq_count = {}
        self.vocabulary_path = None
        self.alias_ngram = alias_ngram

    def init_vocabulary_from_json(self, vocabulary_path):

        vocabulary_list = []
        alias_to_phrase_freq_count = {}
        with codecs.open(vocabulary_path, 'r', 'utf-8') as f:
            self.vocabulary_path = vocabulary_path
            vocabulary_list = json.load(f)
        label_vocabulary = set([])
        for item in vocabulary_list:
            alias = item["alias"]
            label_vocabulary.add(alias)
            # phrase_freq = 0
            # entity_id = item["entity"]
            # alias_freq = item["count"]
            # alias_entity_freq = item["count"]
        self.label_vocabulary = label_vocabulary
        print("labelVocabulary size=%d", len(label_vocabulary))
        print("max_label_length=%d", self.max_label_length)
        for alias in self.label_vocabulary:
            alias_to_phrase_freq_count[alias] = 0
        self.alias_to_phrase_freq_count = alias_to_phrase_freq_count

    def count_occur_from_json(self, corpus_json_path):
        with codecs.open(corpus_json_path, 'r', 'utf-8') as f:
            corpus_list = json.load(f)
            for corpus in corpus_list:
                self.count_occur_in_content(corpus["clean_text"])

    def count_occur_in_content(self, content):
        label_vocabulary = self.label_vocabulary
        alias_to_phrase_freq_count = self.alias_to_phrase_freq_count
        max_label_length = self.max_label_length
        if content == None or content == "":
            return
        words = nltk.word_tokenize(content)
        content = " ".join(words).lower()
        match_index_list = []
        for i, char in enumerate(content):
            if self.is_white_char(content[i]):
                match_index_list.append(i)

        len_match_index_list = len(match_index_list)
        for i in range(0, len_match_index_list - 1):
            start_index = match_index_list[i] + 1
            if self.is_white_char(content[start_index]):
                continue
            max_word_index = min(i + max_label_length, len_match_index_list - 1)
            for j in range(max_word_index, i, -1):
                current_index = match_index_list[j]
                ngram = content[start_index:current_index]
                if ngram in label_vocabulary:
                    alias_to_phrase_freq_count[ngram] = alias_to_phrase_freq_count[ngram] + 1

        self.alias_to_phrase_freq_count = alias_to_phrase_freq_count

    def is_white_char(self, char):
        if char == " " or char == "\n" or char == "\t":
            return True
        return False

    def write_to_tsv(self, tsv_path):

        with codecs.open(self.vocabulary_path, 'r', 'utf-8') as f:
            vocabulary_list = json.load(f)
            print(self.alias_to_phrase_freq_count)
            alias_set = self.alias_to_phrase_freq_count.keys()

            with codecs.open(tsv_path, 'w', 'utf-8') as tsv_f:
                for item in vocabulary_list:

                    alias = item["alias"]
                    if alias in alias_set:
                        phrase_freq = self.alias_to_phrase_freq_count[alias]
                    else:
                        phrase_freq = 0

                    entity_id = item["entity"]
                    alias_freq = item["count"]
                    alias_entity_freq = item["count"]

                    left_part = "\t".join([str(phrase_freq), str(alias_freq),entity_id, str(alias_entity_freq)])
                    if self.alias_ngram == False:
                        tsv_f.write(
                            "\t".join([alias, left_part]))
                        tsv_f.write("\n")
                    else:
                        print("alias=", alias)
                        alias_words = alias.split(" ")
                        for i in range(0, len(alias_words)):
                            current_ngram_alias = ""
                            if alias_words[i] == "(":
                                break
                            for j in range(i, len(alias_words)):
                                if current_ngram_alias:
                                    current_ngram_alias = current_ngram_alias + " " + alias_words[j]
                                else:
                                    current_ngram_alias = alias_words[j]
                                line_data = "\t".join([current_ngram_alias, left_part])
                                print("line=", line_data)
                                print(line_data)
                                tsv_f.write(
                                    line_data)
                                tsv_f.write("\n")
