import urllib
import chardet
rawdata = urllib.urlopen('https://f-droid.org/repository/browse/?fdcategory=Internet&fdid=com.vuze.android.remote&fdpage=7').read()
enc=chardet.detect(rawdata)
print enc['encoding']
