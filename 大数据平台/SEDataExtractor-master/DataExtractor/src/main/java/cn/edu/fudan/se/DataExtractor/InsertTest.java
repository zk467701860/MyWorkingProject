package cn.edu.fudan.se.DataExtractor;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeClass;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.mapper.CodeClassMapper;
import cn.edu.fudan.se.DataExtractor.mybatisDao.MybatisFactory;
import cn.edu.fudan.se.DataExtractor.repository.CodeFile;

public class InsertTest {
	public static void main(String[] args){
		SqlSessionFactory factory = MybatisFactory.getSqlSessionFactory();
		SqlSession sqlSession = factory.openSession();
		CodeClass code = new CodeClass();
		code.setClassName("class name");
		CodeFile f = new CodeFile();
		f.setFileId(15);
		code.setPackageName("package name");
		try{
			List<CodeClass> codeClass = sqlSession.getMapper(CodeClassMapper.class).getAllOldCodeClass();
			for(CodeClass c : codeClass){
				System.out.println(c.getCodeClassId()+"\t"+c.getClassName());
			}
			sqlSession.commit();
		}finally {
		    sqlSession.close();
		}	
	}
}
