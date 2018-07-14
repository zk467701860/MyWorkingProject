from pymysql import OperationalError

from db.engine_factory import EngineFactory
from model import PostsRecord, APIAlias, APIEntity, has_alias_table
from shared.logger_util import Logger
from sqlalchemy_fulltext import FullTextSearch
from sqlalchemy_fulltext import modes as FullTextMode


class DBSearcher:
    RESULT_FORMAT_ONLY_ID = 2
    RESULT_FORMAT_ALL_INFO = 1
    ALL_RESULT = 0
    MAX_RESULT_NUM = 500

    def __init__(self, session=None, logger=None):
        self.__session = session
        if logger is None:
            self.logger = Logger("DBSearcher").get_log()
        else:
            self.logger = logger

    def get_session(self):
        if not self.__session:
            ## todo, init the session from a factory instance,this factory is init in the construction
            self.__session = EngineFactory.create_session(autocommit=True)

        return self.__session

    def clean_session(self):
        self.__session = None

    def full_text_search_in_nature_language(self, query, model_class, limit=ALL_RESULT,
                                            result_format=RESULT_FORMAT_ALL_INFO):
        if result_format == DBSearcher.RESULT_FORMAT_ALL_INFO:
            session_query = self.get_session().query(model_class)
        else:
            session_query = self.get_session().query(model_class.id)

        session_query = session_query.filter(FullTextSearch(query, model_class, FullTextMode.NATURAL))
        if limit == DBSearcher.ALL_RESULT:
            limit = DBSearcher.MAX_RESULT_NUM
        return session_query.limit(limit).all()


class SOPostSearcher(DBSearcher):

    def search_post(self, query, result_limit=10):
        try:
            max_limit = result_limit + 50
            statement = self.get_session().query(PostsRecord).filter(
                FullTextSearch(query, PostsRecord, FullTextMode.NATURAL))

            if result_limit == 0:
                post_list = statement.all()
            else:
                post_list = statement.limit(max_limit).all()

            post_list.sort(key=lambda x: x.score, reverse=True)

            return post_list[:result_limit]
        except Exception:
            self.clean_session()
            if self.logger:
                self.logger.exception("exception occur in query=%s", query)
            return []

    def search_post_in_simple_format(self, query, result_limit=10):
        try:
            max_limit = result_limit + 50
            statement = self.get_session().query(PostsRecord.id, PostsRecord.title, PostsRecord.score).filter(
                FullTextSearch(query, PostsRecord, FullTextMode.NATURAL))

            if result_limit == 0:
                post_list = statement.all()
            else:
                post_list = statement.limit(max_limit).all()

            post_list.sort(key=lambda x: x.score, reverse=True)

            result = post_list[:result_limit]
            return self.__parse_to_post_to_json_list(result)
        except Exception:
            self.clean_session()
            if self.logger:
                self.logger.exception("exception occur in query=%s", query)
            return []

    @staticmethod
    def __parse_to_post_to_json_list(posts):
        result = []
        for post in posts:
            team = {"id": post.id, "title": post.title, "score": post.score}
            result.append(team)
        return result


class APISearcher(DBSearcher):

    def search_api_aliases(self, query, result_limit=DBSearcher.ALL_RESULT,
                           result_format=DBSearcher.RESULT_FORMAT_ALL_INFO):
        try:
            aliases = []
            temp_result = self.get_session().query(APIAlias.id).filter(APIAlias.alias == query).all()
            aliases.extend(temp_result)
            if result_limit != DBSearcher.ALL_RESULT and len(aliases) >= result_limit:
                return aliases

            temp_result = self.full_text_search_in_nature_language(query, APIAlias, result_limit, result_format)
            aliases.extend(temp_result)

            return aliases
        except OperationalError:
            self.clean_session()
            if self.logger:
                self.logger.exception("exception occur in query=%s", query)
        except Exception:
            self.clean_session()
            if self.logger:
                self.logger.exception("exception occur in query=%s", query)
            return []

    def search_api_entity(self, query, result_limit=DBSearcher.ALL_RESULT, api_type=APIEntity.API_TYPE_ALL_API_ENTITY,
                          result_format=DBSearcher.RESULT_FORMAT_ALL_INFO):
        try:
            alias_id_list = self.search_api_aliases(query, result_format=APISearcher.RESULT_FORMAT_ONLY_ID)
            temp_alias_id_list = []

            for alias in alias_id_list:
                if alias.id not in temp_alias_id_list:
                    temp_alias_id_list.append(alias.id)

            alias_id_list = temp_alias_id_list
            if len(alias_id_list) == 0:
                return []

            return self.query_api_entity_by_alias_id_list(alias_id_list=alias_id_list, api_type=api_type,
                                                          result_limit=result_limit,
                                                          result_format=result_format)
        except OperationalError:
            self.clean_session()
            if self.logger:
                self.logger.exception("exception occur in query=%s", query)
        except Exception:
            self.clean_session()
            if self.logger:
                self.logger.exception("exception occur in query=%s", query)

            self.logger.exception("query=%s", query)
            return []

    def query_api_entity(self, id_list, api_type=APIEntity.API_TYPE_ALL_API_ENTITY):
        if api_type == APIEntity.API_TYPE_ALL_API_ENTITY:
            query = self.get_session().query(APIEntity) \
                .filter(APIEntity.id == has_alias_table.c.api_id) \
                .filter(has_alias_table.c.api_id.in_(id_list))
        else:
            query = self.get_session().query(APIEntity) \
                .filter(APIEntity.api_type == api_type) \
                .filter(APIEntity.id == has_alias_table.c.api_id) \
                .filter(has_alias_table.c.api_id.in_(id_list))
        return query.all()

    def query_api_entity_by_alias_id_list(self, alias_id_list, api_type=APIEntity.API_TYPE_ALL_API_ENTITY,
                                          result_limit=DBSearcher.ALL_RESULT,
                                          result_format=DBSearcher.RESULT_FORMAT_ALL_INFO):
        if result_format == APISearcher.RESULT_FORMAT_ALL_INFO:
            session_query = self.get_session().query(APIEntity)
        else:
            session_query = self.get_session().query(APIEntity.id)

        if api_type == APIEntity.API_TYPE_ALL_API_ENTITY:
            query = session_query \
                .filter(APIEntity.id == has_alias_table.c.api_id) \
                .filter(has_alias_table.c.alias_id.in_(alias_id_list))
        else:
            query = session_query \
                .filter(APIEntity.api_type == api_type) \
                .filter(APIEntity.id == has_alias_table.c.api_id) \
                .filter(has_alias_table.c.alias_id.in_(alias_id_list))

        if result_limit != APISearcher.ALL_RESULT:
            query = query.limit(result_limit)
        else:
            query = query.limit(APISearcher.MAX_RESULT_NUM)

        return query.all()

    def query_api_entity_by_alias_id_list_with_order(self, alias_id_list, api_type=APIEntity.API_TYPE_ALL_API_ENTITY,
                                                     result_limit=DBSearcher.ALL_RESULT,
                                                     result_format=DBSearcher.RESULT_FORMAT_ALL_INFO):
        if result_format == APISearcher.RESULT_FORMAT_ALL_INFO:
            session_query = self.get_session().query(APIEntity)
        else:
            session_query = self.get_session().query(APIEntity.id)

        filter_alias_id_list = []

        if result_limit != APISearcher.ALL_RESULT:
            filter_alias_id_list = alias_id_list[:result_limit * 2]

        if api_type == APIEntity.API_TYPE_ALL_API_ENTITY:
            query = session_query \
                .filter(APIEntity.id == has_alias_table.c.api_id) \
                .filter(has_alias_table.c.alias_id.in_(filter_alias_id_list))
        else:
            query = session_query \
                .filter(APIEntity.api_type == api_type) \
                .filter(APIEntity.id == has_alias_table.c.api_id) \
                .filter(has_alias_table.c.alias_id.in_(filter_alias_id_list))

        sorted_result = []
        all_api_entity = query.all()

        all_api_to_all_aliases_map_list = []

        for api_entity in all_api_entity:
            all_alias_id_list = []
            for alias in api_entity.all_aliases:
                all_alias_id_list.append(alias.id)
            all_api_to_all_aliases_map_list.append((api_entity, all_alias_id_list))

        for alias_id in alias_id_list:
            for (api_entity, all_alias_id_list) in all_api_to_all_aliases_map_list:
                if alias_id in all_alias_id_list and api_entity not in sorted_result:
                    sorted_result.append(api_entity)
        if result_limit != DBSearcher.ALL_RESULT and len(sorted_result) > result_limit:
            return sorted_result[:result_limit]
        return sorted_result

    def search_api_entity_with_order(self, query, result_limit=DBSearcher.ALL_RESULT,
                                     api_type=APIEntity.API_TYPE_ALL_API_ENTITY,
                                     result_format=DBSearcher.RESULT_FORMAT_ALL_INFO):
        try:
            alias_id_list = self.search_api_aliases(query, result_format=APISearcher.RESULT_FORMAT_ONLY_ID)
            temp_alias_id_list = []

            for alias in alias_id_list:
                if alias.id not in temp_alias_id_list:
                    temp_alias_id_list.append(alias.id)

            alias_id_list = temp_alias_id_list
            if len(alias_id_list) == 0:
                return []

            return self.query_api_entity_by_alias_id_list_with_order(alias_id_list=alias_id_list, api_type=api_type,
                                                                     result_limit=result_limit,
                                                                     result_format=result_format)
        except OperationalError:
            self.clean_session()
            if self.logger:
                self.logger.exception("exception occur in query=%s", query)
        except Exception:
            self.clean_session()
            if self.logger:
                self.logger.exception("exception occur in query=%s", query)

            self.logger.exception("query=%s", query)
            return []
