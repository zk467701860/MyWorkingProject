package com.methodPrint.testquick_mvn;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import com.mysql.jdbc.PreparedStatement;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MethodPrinter {

    public static void main(String[] args) throws Exception {
    	MethodPrinter methodPrinter = new MethodPrinter();
    	methodPrinter.init();
    }
    Connection conn = null;
    Statement statement = null;
    Statement statement1 = null;
    public void init() throws ParseException, IOException{
    	ArrayList<Integer> errorS = new ArrayList<>();
    	try{
            //调用Class.forName()方法加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("成功加载MySQL驱动！");
        }catch(ClassNotFoundException e1){
            System.out.println("找不到MySQL驱动!");
            e1.printStackTrace();
        }
        
        String url="jdbc:mysql://10.131.252.156:3306/fdroid";    //JDBC的URL    
        //调用DriverManager对象的getConnection()方法，获得一个Connection对象
        
        try {
            conn = DriverManager.getConnection(url,    "root","root");
            statement = conn.createStatement();
            statement1 = conn.createStatement();
            //创建一个Statement对象
            System.out.print("成功连接到数据库！");
            
        } catch (SQLException e){
            e.printStackTrace();
        }
        
        
        ResultSet resultSet;
        int class_id = -1;
        String class_path = "";
		try {
			resultSet = statement1.executeQuery("select class_id,class_path from r_class");
			while (resultSet.next()) {
				class_id = resultSet.getInt("class_id");
				class_path = resultSet.getString("class_path");
				
				if(class_id<609517 ){
					continue;
				}
				
				
				
				
				try {
					getInfo(class_id, class_path);
				} catch (Exception e) {
					e.printStackTrace();
					errorS.add(class_id);
					continue;
				}
	        	
			}
	        resultSet.close();
		} catch (SQLException e1) {
			
			e1.printStackTrace();
		}
        
        
        
        
        
       
        try {
        	statement.close();
        	statement1.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("complete!");
        
        
        
    }
    
    
    public void getInfo(int class_id,String class_path) throws  IOException{
    	// creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream(class_path);
        System.out.println(class_id+"           "+class_path);
        CompilationUnit cu;
        try {
            // parse the file
            cu = JavaParser.parse(in);
            //System.out.println(cu.getImports());
            
        } catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			in.close();
			return;
		}
        finally {
            in.close();
        }

        
        
        // visit and print the methods names
        //new MethodVisitor().visit(cu, null);
        if(cu.getTypes()!=null){
        	for(TypeDeclaration type: cu.getTypes()){
            	/*if(type instanceof ClassOrInterfaceDeclaration){
            		ClassOrInterfaceDeclaration variableName = (ClassOrInterfaceDeclaration) type;
                    System.out.println(variableName.getName());
            	}*/
            	if(type instanceof BodyDeclaration){
            		if(type.getMembers()==null){
            			continue;
            		}
            		for(BodyDeclaration body: type.getMembers()){
                		/*if(body instanceof ClassOrInterfaceDeclaration){
                            ClassOrInterfaceDeclaration variableName = (ClassOrInterfaceDeclaration) body;
                            System.out.println(variableName.toString());
                        }
                        else if(body instanceof FieldDeclaration){
                            FieldDeclaration variableName = (FieldDeclaration) body;
                            //System.out.println(variableName.toString());
                        }else*/ 
                        if(body instanceof MethodDeclaration){
                            MethodDeclaration variableName = (MethodDeclaration) body;
                            String funName = variableName.getName();
                            //System.out.println(funName);
                            String retType = variableName.getType().toString();
                           
                            String content = null;
                            if(variableName.getBody() != null){
                            	content = variableName.getBody().toString();
                            }
                            String enclosure = Modifier.toString(variableName.getModifiers());
                            
                           
                            if(content == null){
                            	content = "";
                            }
                            if(retType == null){
                            	retType = "";
                            }
                            if(enclosure == null){
                            	enclosure = "";
                            }
                            try {
								statement.executeUpdate("insert into r_function (class_id,fun_name,return_type,content,enclosure) values (\""+class_id+"\",\""+funName+"\",\""+retType+"\",\""+content.replace("\\", "\\\\").replace("\"", "\\\"")+"\",\""+enclosure+"\")",Statement.RETURN_GENERATED_KEYS);
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
                            
                            
                            
                            
                            
                             
                            ResultSet resultSet1;
                            int fun_id = -1;
                            try {
                            	resultSet1 = statement.getGeneratedKeys();
                            	if(resultSet1.next()) {
        							fun_id = resultSet1.getInt(1);
        						}
                                resultSet1.close();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
                            
                            
                            
                        
                            if(variableName.getParameters() != null&&fun_id!=-1){
                            	for(Parameter parameter:variableName.getParameters()){
                            		
                                    
                                    try {
        								statement.executeUpdate("insert into r_parameter (fun_id,type,var_name) values (\""+fun_id+"\",\""+parameter.getType().toString()+"\",\""+parameter.getId().toString()+"\")");
        							} catch (SQLException e1) {
        								// TODO Auto-generated catch block
        								e1.printStackTrace();
        							}
                                }
                            }
                            
                            
                        }
                	}
            	}
            	
            }
        }
        
    }

    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes. 
     
    private static class MethodVisitor extends VoidVisitorAdapter {

        @Override
        public void visit(MethodDeclaration n, Object arg) {
            // here you can access the attributes of the method.
            // this method will be called for all methods in this 
            // CompilationUnit, including inner class methods
            System.out.print(n.getName() + " ");
            List<Parameter> par = n.getParameters();
            for (Parameter parameter : par) {
            	System.out.print("Type:  " + parameter.getType() + "  ");
            	System.out.print(parameter.getId() + "  ");
			}
            System.out.println();
        }
    }*/
}
