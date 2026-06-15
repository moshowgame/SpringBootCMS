package com.softdev.cms.mapper;

import com.softdev.cms.entity.FormItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FormItemMapper {

    FormItem selectById(@Param("itemId") Integer itemId);

    List<FormItem> selectByFormId(@Param("formId") Integer formId);

    int insert(FormItem formItem);

    int updateById(FormItem formItem);

    int deleteById(@Param("itemId") Integer itemId);

    int deleteByFormId(@Param("formId") Integer formId);
}
