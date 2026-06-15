package com.softdev.cms.mapper;

import com.softdev.cms.entity.Form;
import com.softdev.cms.entity.dto.QueryParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FormMapper {

    Form selectById(@Param("formId") Integer formId);

    List<Form> pageAll(QueryParamDTO queryParamDTO);

    int countAll(QueryParamDTO queryParamDTO);

    int insert(Form form);

    int updateById(Form form);

    int deleteById(@Param("formId") Integer formId);
}
