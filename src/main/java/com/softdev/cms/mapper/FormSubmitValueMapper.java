package com.softdev.cms.mapper;

import com.softdev.cms.entity.FormSubmitValue;
import com.softdev.cms.entity.dto.FormSubmitValueDTO;
import com.softdev.cms.entity.dto.FormSubmitValueExcelDTO;
import com.softdev.cms.entity.dto.QueryParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FormSubmitValueMapper {

    FormSubmitValue selectById(@Param("valueId") Integer valueId);

    List<FormSubmitValueDTO> getFormSubmitValue(@Param("formId") Integer formId, @Param("submitId") Integer submitId);

    List<FormSubmitValueExcelDTO> getFormSubmitValueExcel(QueryParamDTO queryParam);

    List<FormSubmitValueDTO> getSelectCountList();

    int insert(FormSubmitValue formSubmitValue);

    int updateById(FormSubmitValue formSubmitValue);

    int deleteById(@Param("valueId") Integer valueId);
}
