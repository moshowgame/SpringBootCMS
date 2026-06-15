package com.softdev.cms.mapper;

import com.softdev.cms.entity.FormSubmit;
import com.softdev.cms.entity.dto.QueryParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FormSubmitMapper {

    FormSubmit selectById(@Param("submitId") Integer submitId);

    List<FormSubmit> pageAll(QueryParamDTO queryParamDTO);

    int countAll(QueryParamDTO queryParamDTO);

    int insert(FormSubmit formSubmit);

    int updateById(FormSubmit formSubmit);

    int deleteById(@Param("submitId") Integer submitId);
}
