package com.softdev.cms.mapper;

import com.softdev.cms.entity.FormSubmit;
import com.softdev.cms.entity.dto.QueryParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FormSubmitMapper {

    FormSubmit selectById(@Param("submitId") Integer submitId);

    /**
     * 按表单ID和用户ID查询提交记录（用于专项表单去重检查）
     */
    FormSubmit selectByFormIdAndUserId(@Param("formId") Integer formId,
                                        @Param("userId") Integer userId);

    /**
     * 按表单ID和手机号查询提交记录（用于前台匿名提交去重检查）
     */
    FormSubmit selectByFormIdAndPhone(@Param("formId") Integer formId,
                                       @Param("phone") String phone);

    List<FormSubmit> pageAll(QueryParamDTO queryParamDTO);

    int countAll(QueryParamDTO queryParamDTO);

    int insert(FormSubmit formSubmit);

    int updateById(FormSubmit formSubmit);

    int deleteById(@Param("submitId") Integer submitId);
}
