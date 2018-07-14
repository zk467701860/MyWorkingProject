package cn.edu.fudan.se.DataExtractor;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cn.edu.fudan.se.DataExtractor.api.APIClass;
import cn.edu.fudan.se.DataExtractor.api.APIMethod;
import cn.edu.fudan.se.DataExtractor.api.APIParameter;
import cn.edu.fudan.se.DataExtractor.api.mapper.APIMapper;
import cn.edu.fudan.se.DataExtractor.mybatisDao.APIDao;
import cn.edu.fudan.se.DataExtractor.mybatisDao.MybatisFactory;

public class SelectAPIClassTest {
	public static void main(String[] args){
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		try{
			//APIClass apiclass = sqlSession.getMapper(APIMapper.class).getClassByName_Packagename_Version("Driver", "org.xmlpull.v1.sax2", 1);
			//String[] parameterType = new String[1];
			//parameterType[0] = "";
			//APIMethod apiMethod = sqlSession.getMapper(APIMapper.class).getMethodByName_Parameter_Classid("", parameterType, 1);
//			String statement = "cn.edu.fudan.se.DataExtractor.api.mapper.APIClassMapper.getAPIClass";//映射sql的标识字符串
//	        //执行查询返回一个唯一user对象的sql
//	        APIClass api = sqlSession.selectOne(statement, 1);
	        //System.out.println(apiclass.getId());
	        //System.out.println(apiclass.getName());
	        //System.out.println(apiclass.getDocWebsite());
	        //System.out.println(apiclass.getPackageId());
//			List<APIMethod> methods = sqlSession.getMapper(APIMapper.class).getMethodsByName_Parameter_Classid("attribute", 1);
//			System.out.println(methods.size());
//			for(APIMethod method : methods){
//				System.out.println(method.getId());
//				System.out.println(method.getReturnString());
//				 List<APIParameter> parameters = sqlSession.getMapper(APIMapper.class).getParametersByMethodid(method.getId());
			//}
			APIDao dao = new APIDao();
			String[] str = new String[3];
			str[0] = "String";
			str[1] = "Object";
			str[2] = "String";
			APIMethod method = dao.getMethodsByName_Parameter_Classid("attribute", str, 53);
			System.out.println(method == null);
			System.out.println(method.getReturnString());
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
}
