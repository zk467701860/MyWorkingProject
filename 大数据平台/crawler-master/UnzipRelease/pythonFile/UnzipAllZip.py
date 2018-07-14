#coding:utf-8
import zipfile
import os

file_list = os.listdir(os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir)))
print len(file_list)
i = 1
for file_name in file_list:
    parzip = os.path.abspath(os.path.join(os.path.dirname(__file__), os.pardir, file_name))
    print str(i) + '    ' + parzip
    subfile_list = os.listdir(parzip)
    for subfile_name in subfile_list:
        if os.path.splitext(subfile_name)[1] == '.zip':
            fullname = os.path.join(parzip, subfile_name)
            print fullname
            try:
                file_zip = zipfile.ZipFile(fullname, 'r')
                for file in file_zip.namelist():
                    #print file
                    file_zip.extract(file, parzip)
                file_zip.close()
            except Exception,e:
                print Exception,":",e
    i += 1
