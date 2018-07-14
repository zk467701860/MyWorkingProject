package mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import po.CodeMethod;
import po.CodeMethodExample;

public interface CodeMethodMapper {
    long countByExample(CodeMethodExample example);

    int deleteByExample(CodeMethodExample example);

    int insert(CodeMethod record);

    int insertSelective(CodeMethod record);

    List<CodeMethod> selectByExampleWithBLOBs(CodeMethodExample example);

    List<CodeMethod> selectByExample(CodeMethodExample example);

    int updateByExampleSelective(@Param("record") CodeMethod record, @Param("example") CodeMethodExample example);

    int updateByExampleWithBLOBs(@Param("record") CodeMethod record, @Param("example") CodeMethodExample example);

    int updateByExample(@Param("record") CodeMethod record, @Param("example") CodeMethodExample example);
}