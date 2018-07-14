#encoding:utf-8

from flask import Flask, abort, request, jsonify
from gevent import monkey
from gevent.pywsgi import WSGIServer
monkey.patch_all()

import requests
import pymysql
from threading import Thread
import os
import re
import configparser
import uuid
from flask_cors import *

app = Flask(__name__)
app.config.update(DEBUG=True)
CORS(app, supports_credentials = True)
config = configparser.ConfigParser()
config.read('config.conf')
path = config.get('Path', 'path')

class MyThread(object):
    def __init__(self, func_list=None):
        self.return_value = None
        self.func_list = func_list
        self.threads = []

    def set_thread_func_list(self, func_list):
        self.func_list = func_list

    def trace_func(self, func, *args, **kwargs):
        ret = func(*args, **kwargs)
        self.return_value = ret

    def start(self):
        """
        @note: 启动多线程执行，并阻塞到结束
        """
        self.threads = []
        self.return_value = None
        for func_dict in self.func_list:
            if func_dict["args"]:
                new_arg_list = []
                new_arg_list.append(func_dict["func"])
                for arg in func_dict["args"]:
                    new_arg_list.append(arg)
                new_arg_tuple = tuple(new_arg_list)
                t = Thread(target=self.trace_func, args=new_arg_tuple)
            else:
                t = Thread(target=self.trace_func, args=(func_dict["func"],))
            self.threads.append(t)

        for thread_obj in self.threads:
            thread_obj.start()

        for thread_obj in self.threads:
            thread_obj.join()

    def ret_value(self):
        """
        @note: 返回子线程函数的返回值
        """
        return self.return_value

class UrlError(Exception):
    def __init__(self, ErrorInfo):
        super().__init__(self)
        self.error_info = ErrorInfo

    def __str__(self):
        return self.error_info

class DownloadError(Exception):
    def __init__(self, ErrorInfo):
        super().__init__(self)
        self.error_info = ErrorInfo

    def __str__(self):
        return self.error_info

def is_java(project_url, vcs_type):
    if vcs_type == 'git':
        flag = 0
        while True:
            try:
                header = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:53.0) Gecko/20100101 Firefox/53.0',
                         'Authorization': 'token ' + config.get('GithubToken', 'token')}
                response = requests.get('https://api.github.com/repos/' + project_url[19:], timeout=15, headers=header).json()
            except Exception:
                flag += 1
                if flag > 3:
                    return False
            else:
                if response['language'] == 'Java':
                    return True
                else:
                    return False
    else:
        return False

def is_maven(url):
    flag = 0
    while True:
        try:
            header = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:53.0) Gecko/20100101 Firefox/53.0',
                     'Authorization': 'token ' + config.get('GithubToken', 'token')}
            response = requests.get('https://api.github.com/repos/' + url[19:] + '/contents', timeout=15, headers=header).json()
        except Exception:
            flag += 1
            if flag > 3:
                return False
        else:
            for item in response:
                if item['name'] == 'pom.xml':
                    return True
            else:
                return False

def get_project_description(url):
    flag = 0
    while True:
        try:
            header = {'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:53.0) Gecko/20100101 Firefox/53.0',
                     'Authorization': 'token ' + config.get('GithubToken', 'token')}
            response = requests.get(url, timeout=15, headers=header).json()
        except Exception:
            flag += 1
            if flag > 3:
                return ''
        else:
            result = '' if response['description'] is None else response['description']
            return result

def is_url_valid(project_url):
    patterns = ('https://github.com/([\w-]{1,}/[\w-]{1,})')
    flag = 0
    for pattern in patterns:
        ret = re.search(pattern, project_url)
        if ret is not None:
            flag = 1
            break

    return flag == 1

def is_project_valid(project_url):
    flag = 0
    while True:
        try:
            response = requests.get(project_url, timeout=15).headers['Status']
        except Exception as e:
            flag += 1
            if flag > 3:
                return False
        else:
            if '200' in response:
                return True
            else:
                return False

def get_vcs_type(project_url):
    dic = {'github.com': 'git'}
    patterns = ('github.com',)
    for pattern in patterns:
        if pattern in project_url:
            return dic[pattern]

def insert_into_mysql(tablename, params={}):
    conn = pymysql.connect(
        host=config.get('IssueTrackerMysqlDB', 'host'),
        db=config.get('IssueTrackerMysqlDB', 'db'),
        user=config.get('IssueTrackerMysqlDB', 'user'),
        passwd=config.get('IssueTrackerMysqlDB', 'passwd'),
        charset=config.get('IssueTrackerMysqlDB', 'charset')
    )
    sql = "insert into %s " % tablename
    keys = params.keys()
    sql += "(`" + "`,`".join(keys) + "`)"               #字段组合
    values = list(params.values())                        #值组合，由元组转换为数组
    sql += " values (%s)" % ','.join(['%s']*len(values))  #配置相应的占位符
    cur = conn.cursor()
    cur.execute(sql, values)
    conn.commit()
    cur.close()
    conn.close()

# insert_into_mysql('project', {'uuid':4, 'name':'rooo', 'url':'test', 'vcs_type':'test', 'account_id':'eeeee'})

def delete_from_mysql(tablename, params={}):
    conn = pymysql.connect(
        host=config.get('IssueTrackerMysqlDB', 'host'),
        db=config.get('IssueTrackerMysqlDB', 'db'),
        user=config.get('IssueTrackerMysqlDB', 'user'),
        passwd=config.get('IssueTrackerMysqlDB', 'passwd'),
        charset=config.get('IssueTrackerMysqlDB', 'charset')
    )
    sql = "delete from %s " % tablename
    sql += " where uuid = %(uuid)s "
    cur = conn.cursor()
    cur.execute(sql, params)
    conn.commit()
    cur.close()
    conn.close()

def get_data_from_mysql(tablename, params={}, fields=[]):
    conn = pymysql.connect(
        host=config.get('IssueTrackerMysqlDB', 'host'),
        db=config.get('IssueTrackerMysqlDB', 'db'),
        user=config.get('IssueTrackerMysqlDB', 'user'),
        passwd=config.get('IssueTrackerMysqlDB', 'passwd'),
        charset=config.get('IssueTrackerMysqlDB', 'charset')
    )
    sql = "select %s from %s " % ('*' if len(fields) == 0 else ','.join(fields), tablename)
    keys = params.keys()
    where = ""
    ps = []
    values = []
    if len(keys) > 0:                    #存在查询条件时，以与方式组合
        for key in keys:
            ps.append(key + " =%s ")
            values.append(params[key])
        where += ' where ' + ' and '.join(ps)
    cur = conn.cursor()
    cur.execute(sql + where, values)
    ret = cur.fetchall()
    cur.close()
    conn.close()

    return ret

def get_project_name(url):
    pattern = 'github.com/([\w-]{1,}/[\w-]{1,})'
    ret = re.findall(pattern, url)
    if ret is None or len(ret) == 0:
        raise UrlError('The format of url is incorrect!')
    else:
        return ret[0]


def download(url, account_name, project_name):
    if is_project_valid(url):
        if not os.path.exists(path + '/' + account_name + '/' + project_name):

            if not os.path.exists(path + '/' + account_name):
                os.makedirs(path + '/' + account_name)

            os.chdir(path + '/' + account_name)
            ret = os.system('git clone https://jixixuanf5905:xinylu6261@' + url.replace('https://', ''))
            # ret = subprocess.call(['git', 'clone', 'https://jixixuanf5905:xinylu6261@' + url.replace('https://', '')])

            if ret != 0:
                raise DownloadError('Opps, download failed! Please ensure the validation of the project.')

    else:
        raise UrlError('The format of url is incorrect!')

@app.errorhandler(404)
def not_found(error=None):
    message = {
        'status': 404,
        'message': 'Not Found: ' + request.url
    }
    resp = jsonify(message)
    resp.status_code = 404

    return resp

def insert_async_project(app, project_url, account_name):
    with app.app_context():
        if is_project_valid(project_url):

            if is_url_valid(project_url):
                vcs_type = get_vcs_type(project_url)

                if is_java(project_url, vcs_type) and is_maven(project_url):

                    try:
                        ret = get_data_from_mysql(
                            tablename='account',
                            params={'accountName': account_name},
                            fields=['uuid'],
                        )
                        project_name = project_url.split('/')[4]
                        account_id = ret[0]
                        uu_id = uuid.uuid1().__str__()
                        insert_into_mysql(
                            tablename='project',
                            params={
                                'uuid': uu_id,
                                'name': project_name,
                                'url': project_url,
                                'vcs_type': vcs_type,
                                'account_id': account_id
                            }
                        )

                    except Exception as e:
                        message = {
                            'status': 'Failed',
                            'message': 'error, ' + e.__str__()
                        }
                        return jsonify(data=message)

                    else:
                        try:
                            download(project_url, account_name, project_name)
                        except Exception as e:
                            message = {
                                'status': 'Failed',
                                'message': 'error, ' + e.__str__()
                            }
                            delete_from_mysql(
                                tablename='project',
                                params={'uuid': uu_id}
                            )
                            return jsonify(data=message)

                        else:
                            message = {
                                'status': 'Successful',
                            }

                            return jsonify(data=message)

                else:
                    message = {
                        'status': 'Failed',
                        'message': 'Not a java maven project.'
                    }
                    return jsonify(data=message)

            else:
                message = {
                    'status': 'Failed',
                    'message': 'The url of project is invalid.'
                }
                return jsonify(data=message)

        else:
            message = {
                'status': 'Failed',
                'message': 'The project is invalid.'
            }
            return jsonify(data=message)

@app.route('/insert_project/', methods=['GET'])
def insert_project():
    project_url = request.args.get('pj_url')
    account_name = request.args.get('account_name')
    #
    # thr = MyThread()
    # func_list = []
    # func_list.append({"func":insert_async_project,"args":(app, project_url, account_name)})
    # thr.set_thread_func_list(func_list)
    # thr.start()
    # return thr.ret_value()
    if is_project_valid(project_url):

        if is_url_valid(project_url):
            vcs_type = get_vcs_type(project_url)

            if is_java(project_url, vcs_type) and is_maven(project_url):

                try:
                    ret = get_data_from_mysql(
                        tablename='account',
                        params={'accountName': account_name},
                        fields=['uuid'],
                    )
                    description = get_project_description(project_url)
                    project_name = project_url.split('/')[4]
                    account_id = ret[0]
                    uu_id = uuid.uuid1().__str__()
                    insert_into_mysql(
                        tablename='project',
                        params={
                            'uuid': uu_id,
                            'name': project_name,
                            'url': project_url,
                            'vcs_type': vcs_type,
                            'account_id': account_id,
                            'description': description
                        }
                    )

                except Exception as e:
                    message = {
                        'status': 'Failed',
                        'message': 'error, ' + e.__str__()
                    }
                    return jsonify(data=message)

                else:
                    try:
                        download(project_url, account_name, project_name)
                    except Exception as e:
                        message = {
                            'status': 'Failed',
                            'message': 'error, ' + e.__str__()
                        }
                        delete_from_mysql(
                            tablename='project',
                            params={'uuid': uu_id}
                        )
                        return jsonify(data=message)

                    else:
                        message = {
                            'status': 'Successful',
                        }

                        return jsonify(data=message)

            else:
                message = {
                    'status': 'Failed',
                    'message': 'Not a java maven project.'
                }
                return jsonify(data=message)

        else:
            message = {
                'status': 'Failed',
                'message': 'The url of project is invalid.'
            }
            return jsonify(data=message)

    else:
        message = {
            'status': 'Failed',
            'message': 'The project is invalid.'
        }
        return jsonify(data=message)

def get_async_project(app, account_name):
    with app.app_context():
        try:
            account_id = get_data_from_mysql('account', {'accountName': account_name}, ['uuid'])[0]
            ret = get_data_from_mysql('project', {'account_id':account_id}, ['*'])
            data = []
            for item in ret:
                dic = dict()
                dic['uuid'] = item[0]
                dic['name'] = item[1]
                dic['url'] = item[2]
                dic['language'] = item[3]
                dic['vcs_type'] = item[4]
                dic['account_id'] = item[5]
                data.append(dic)
                del dic
        except:
            return not_found()
        else:
            return jsonify(data=data)

@app.route('/get_project/<account_name>', methods=['GET'])
def get_project(account_name):
    # thr = MyThread()
    # func_list = []
    # func_list.append({"func":get_async_project,"args":(app, account_name)})
    # thr.set_thread_func_list(func_list)
    # thr.start()
    # return thr.ret_value()
    try:
        account_id = get_data_from_mysql('account', {'accountName': account_name}, ['uuid'])[0]
        ret = get_data_from_mysql('project', {'account_id': account_id}, ['*'])
        data = []
        for item in ret:
            dic = dict()
            dic['uuid'] = item[0]
            dic['name'] = item[1]
            dic['url'] = item[2]
            dic['language'] = item[3]
            dic['vcs_type'] = item[4]
            dic['account_id'] = item[5]
            dic['prev_scan_commit'] = item[6]
            dic['download_status'] = item[7]
            dic['scan_status'] = item[8]
            data.append(dic)
            del dic
    except:
        return not_found()
    else:
        return jsonify(data=data)

@app.route('/get_commit/<account_name>/<project_id>', methods=['GET'])
# 改成接收account_id 返回其所有pj的commit_list
def get_commit(account_name, project_id):
    if project_id == 'all':
        try:
            account_id = get_data_from_mysql('account', {'accountName': account_name}, ['uuid'])[0]
            project_url_list = get_data_from_mysql('project',  {'account_id': account_id}, ['url'])
        except:
            return not_found()
        else:
            return_data = []

            for url in project_url_list:
                new_path = path + '/' + account_name + '/' + url[0].split('/')[4]
                os.chdir(new_path)
                os.system('git log > commit_log.log')
                commit_pattern = 'commit ([\w\d]{40,40})'
                date_pattern = 'Date:   (.*)'
                with open(new_path + '/commit_log.log', 'r', encoding='UTF-8') as f:
                    temp = f.readlines()
                    data = ''

                    for item in temp:
                        if item[0] != ' ':
                            data += item

                    commit_ret = re.findall(commit_pattern, data)
                    date_ret = re.findall(date_pattern, data)
                    data = []

                for sha, date in zip(commit_ret, date_ret):
                    dic = dict()
                    dic['commit_sha'] = sha
                    dic['commit_date'] = date
                    data.append(dic)

                return_data.append(data)

            return jsonify(data=return_data)

    else:
        try:
            project_url = get_data_from_mysql('project', {'uuid': project_id}, ['url'])
        except:
            not_found()
        else:
            new_path = path + '/' + account_name + '/' + project_url[0][0].split('/')[4]
            os.chdir(new_path)
            os.system('git log > commit_log.log')
            commit_pattern = 'commit ([\w\d]{40,40})'
            date_pattern = 'Date:   (.*)'
            description_pattern = 'x,\.,\.extxax\?D\n([\s\S]{1,}?) ,,\|\.\.timmoc'
            data_del_description = ''

            with open(new_path + '/commit_log.log', 'r', encoding='UTF-8') as f:

                temp = f.readlines()
                description_data = ''
                for index in range(len(temp)):
                    if temp[index][0] != ' ':
                        data_del_description += temp[index]

                    if index == 0:
                        description_data += ' ,,|..timmoc'
                    elif temp[index][0:7] == 'commit ':
                        description_data += ' ,,|..timmoc'
                    elif index == len(temp) - 1:
                        description_data += temp[index]
                        description_data += '\n ,,|..timmoc'
                    elif temp[index][0:8] == 'Date:   ':
                        description_data += 'x,.,.extxax?D\n'
                    else:
                        description_data += temp[index]

                description_ret = re.findall(description_pattern, description_data)
                commit_ret = re.findall(commit_pattern, data_del_description)
                date_ret = re.findall(date_pattern, data_del_description)
                data = []

            for sha, date, description in zip(commit_ret, date_ret, description_ret):
                dic = dict()
                dic['commit_sha'] = sha
                dic['commit_date'] = date
                dic['description'] = description
                data.append(dic)

            return jsonify(data=data)

@app.route('/checkout/<account_name>/<project_id>/<commit_id>', methods=['GET'])
def checkout(account_name, project_id, commit_id):
    try:
        ret = get_data_from_mysql(
            tablename = 'project',
            params = {'uuid':project_id},
            fields = ['name']
        )
        project_name = ret[0][0]
        project_path = path + '/' +  account_name + '/' + str(project_name)
        os.chdir(project_path)
        os.system('git checkout ' + commit_id)
    except Exception as e:
        message = {
            'status': 'Failed',
            'message': 'error, ' + e.__str__()
        }
        return jsonify(data=message)
    else:
        message = {
            'status': 'Successful'
        }
        return jsonify(data=message)


if __name__ == "__main__":
    # app.run('127.0.0.1', debug=True, use_reloader=False, threaded=True)
    http_server = WSGIServer(('127.0.0.1', 8000), app)
    http_server.serve_forever()