package mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import po.CodeParameter;
import po.CodeParameterExample;

public interface CodeParameterMapper {
    long countByExample(CodeParameterExample example);

    int deleteByExample(CodeParameterExample example);

    int insert(CodeParameter record);

    int insertSelective(CodeParameter record);

    List<CodeParameter> selectByExample(CodeParameterExample example);

    int updateByExampleSelective(@Param("record") CodeParameter record, @Param("example") CodeParameterExample example);

    int updateByExample(@Param("record") CodeParameter record, @Param("example") CodeParameterExample example);
}