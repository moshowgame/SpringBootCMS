package com.softdev.cms.mapper;

import com.softdev.cms.entity.Template;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TemplateMapper {

    Template selectById(@Param("templateId") Integer templateId);

    List<Template> pageAll(@Param("query") Template query, @Param("page") int page, @Param("limit") int limit);

    int countAll(@Param("query") Template query);

    List<Template> selectByPage(@Param("page") String page);

    int insert(Template template);

    int updateById(Template template);

    int deleteById(@Param("templateId") Integer templateId);
}
