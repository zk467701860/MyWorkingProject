# -*- coding: utf-8 -*-
import fasttext
print  "000000000000000000000000000000000000000"
classifier = fasttext.supervised("../data/news_fasttext_train.txt","news_fasttext.model",label_prefix = "__label__")
print  "111111111111111111111111111111"
result = classifier.test("../data/news_fasttext_test.txt")
print result.precision
print result.recall