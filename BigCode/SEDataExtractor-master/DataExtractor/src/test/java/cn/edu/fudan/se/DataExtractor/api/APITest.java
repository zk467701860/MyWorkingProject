package cn.edu.fudan.se.DataExtractor.api;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import cn.edu.fudan.se.DataExtractor.mybatisDao.APIDao;



public class APITest {
	@Test
	public void testgetAPIByName(){
		List<APIClass> ret = APIDao.getInstance().getApiByName("Driver", 26);
		assertEquals(ret.get(0).getId(), 26);
	}
	
	@Test	
	public void testGetApiByQualifiedName(){
		List<APIClass> ret = APIDao.getInstance().getApiByQualifiedName("android.os.Bundle", 26);
		assertEquals(ret.get(0).getId(), 59817);
	}
	
	@Test
	public void testDeterminPackage(){
		APIPackage p = APIDao.getInstance().getAPIPackage("org.xmlpull.v1.sax2", 2);
		System.out.println(p.getId()+"\t"+p.getName()+p.getDocWebsite()+"\n"+p.getLibraryId());
	}
}
