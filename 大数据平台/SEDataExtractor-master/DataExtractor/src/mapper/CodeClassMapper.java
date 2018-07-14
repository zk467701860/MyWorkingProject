package mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import po.CodeClass;
import po.CodeClassExample;

public interface CodeClassMapper {
    long countByExample(CodeClassExample example);

    int deleteByExample(CodeClassExample example);

    int insert(CodeClass record);

    int insertSelective(CodeClass record);

    List<CodeClass> selectByExampleWithBLOBs(CodeClassExample example);

    List<CodeClass> selectByExample(CodeClassExample example);

    int updateByExampleSelective(@Param("record") CodeClass record, @Param("example") CodeClassExample example);

    int updateByExampleWithBLOBs(@Param("record") CodeClass record, @Param("example") CodeClassExample example);

    int updateByExample(@Param("record") CodeClass record, @Param("example") CodeClassExample example);
}