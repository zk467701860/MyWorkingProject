package cn.edu.fudan.se.DataExtractor.codeAnalyze;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

import cn.edu.fudan.se.DataExtractor.mybatisDao.CodeAnalyzeDao;
import cn.edu.fudan.se.DataExtractor.mybatisDao.DeleteDao;
import cn.edu.fudan.se.DataExtractor.repository.Gitrelease;

public class SelectTest {
	@Test
	public void testOldInnerClass(){
		CodeClass c = CodeAnalyzeDao.getInstance().getOldInnerClassByQualifiedName(2, 
				"io.github.lonamiwebs.klooni", "AndroidLauncher");
		
		assertEquals(c.getCodeClassId(), 1);
	}
	
	@Test
	public void testOldInnerClassPackage(){
		CodeClass c = CodeAnalyzeDao.getInstance().
				determinOldInnerClassByPackage(2, "io.github.lonamiwebs.klooni%");
		System.out.println(c.getCodeClassId());
	}
	
	@Test
	public void testGitRelease(){
		DeleteDao dao = new DeleteDao();
		List<Gitrelease> list = dao.getAll();
		System.out.println(list.size());
		System.out.println(list.get(0).getSrcAddress());
	}
}
