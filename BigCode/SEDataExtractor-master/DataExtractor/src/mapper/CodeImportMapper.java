package mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import po.CodeImport;
import po.CodeImportExample;

public interface CodeImportMapper {
    long countByExample(CodeImportExample example);

    int deleteByExample(CodeImportExample example);

    int insert(CodeImport record);

    int insertSelective(CodeImport record);

    List<CodeImport> selectByExample(CodeImportExample example);

    int updateByExampleSelective(@Param("record") CodeImport record, @Param("example") CodeImportExample example);

    int updateByExample(@Param("record") CodeImport record, @Param("example") CodeImportExample example);
}