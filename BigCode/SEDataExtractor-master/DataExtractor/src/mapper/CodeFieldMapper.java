package mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import po.CodeField;
import po.CodeFieldExample;

public interface CodeFieldMapper {
    long countByExample(CodeFieldExample example);

    int deleteByExample(CodeFieldExample example);

    int insert(CodeField record);

    int insertSelective(CodeField record);

    List<CodeField> selectByExample(CodeFieldExample example);

    int updateByExampleSelective(@Param("record") CodeField record, @Param("example") CodeFieldExample example);

    int updateByExample(@Param("record") CodeField record, @Param("example") CodeFieldExample example);
}