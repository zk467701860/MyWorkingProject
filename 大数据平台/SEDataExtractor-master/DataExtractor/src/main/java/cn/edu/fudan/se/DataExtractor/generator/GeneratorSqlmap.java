package cn.edu.fudan.se.DataExtractor.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lyk on 2017/5/8.
 */
public class GeneratorSqlmap {
    public void generator(String path) throws Exception{
        List<String> warnings = new ArrayList<String>();

        File file = new File(path);
        System.out.println(file.getAbsolutePath());
        ConfigurationParser configParser = new ConfigurationParser(warnings);
        Configuration config =  configParser.parseConfiguration(file);
        DefaultShellCallback callback = new DefaultShellCallback(true);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                callback, warnings);
        myBatisGenerator.generate(null);
    }

    public static void main(String[] args) throws Exception {
        try{
            GeneratorSqlmap generatorSqlmap = new GeneratorSqlmap();
            generatorSqlmap.generator("src/main/java/resources/generatorConfig.xml");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
