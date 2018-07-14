package cn.edu.fudan.se.DataExtractor.repository;

import java.util.List;

import org.junit.Test;

import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeField;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeImport;
import cn.edu.fudan.se.DataExtractor.codeAnalyze.CodeMethod;
import cn.edu.fudan.se.DataExtractor.mybatisDao.CodeAnalyzeDao;
import cn.edu.fudan.se.DataExtractor.mybatisDao.CodeInReleaseDao;

public class CodeTest {
	private CodeInReleaseDao dao = new CodeInReleaseDao();
	private CodeAnalyzeDao countDao = new CodeAnalyzeDao();
	@Test
	public void testClassInPackage() {
		List<SimpleClass> list = dao.selectClassInPackage(6068, "im.r_c.android.clearweather.receiver");
		System.out.println("================class in package==================");
		if(list != null){
			for(SimpleClass l : list){
				System.out.println(l.getClassName());
			}
		}
	}
	
	@Test
	public void testClassPartInPackage() {
		ClassInPackage list = dao.selectClassPartInPackage(607799);
		System.out.println("================class in package==================");
		if(list != null){
			System.out.println(list.getClassName());
		}
	}

	@Test
	public void testMethodInClass() {
		List<CodeMethod> list = dao.selectMethodInClass(607799);
		System.out.println("================method in class==================");
		if(list != null){
			for(CodeMethod l : list){
				System.out.println(l.getMethodName()+"\t"+l.getParameterList().size());
			}
		}
	}

	@Test
	public void testImportInClass() {
		List<CodeImport> list = dao.selectImportInClass(607799);
		System.out.println("================import in class==================");
		if(list != null){
			for(CodeImport l : list){
				System.out.println(l.getPackageName()+l.getClassName());
			}
		}
	}
	

	@Test
	public void testFieldInClass() {
		List<CodeField> list = dao.selectFieldInClass(607799);
		System.out.println("================field in class==================");
		if(list != null){
			for(CodeField l : list){
				System.out.println(l.getFieldName()+"\t"+l.getFieldType());
			}
		}
	}
	
	@Test
	public void testClass() {
		FullClass oneClass = dao.selectOneClass(607799);
		System.out.println("================field in class==================");
		if(oneClass != null){
			System.out.println(oneClass.getClassPath());
		}
	}
	
	@Test
	public void testCount() {
		System.out.println("================count in class==================");
		System.out.println(countDao.countMethod());
		System.out.println(countDao.countVariable());
		System.out.println(countDao.countParameter());
		System.out.println(countDao.countClass());
	}
}
