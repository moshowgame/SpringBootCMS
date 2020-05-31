package com.softdev.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.softdev.cms.entity.Template;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * @description template
 * @author zhengkai.blog.csdn.net
 * @date 2020-05-31
 */
@Mapper
public interface TemplateMapper extends BaseMapper<Template> {

    @Select(
            "<script>select t0.* from template t0 " +
                    //add here if need left join
                    "where 1=1" +
                    "<when test='id!=null and id!=&apos;&apos; '> and t0.id=#{id}</when> " +
                    "<when test='name!=null and name!=&apos;&apos; '> and t0.name=#{name}</when> " +
                    "<when test='value!=null and value!=&apos;&apos; '> and t0.value=#{value}</when> " +
                    "<when test='description!=null and description!=&apos;&apos; '> and t0.description=#{description}</when> " +
                    "<when test='page!=null and page!=&apos;&apos; '> and t0.page=#{page}</when> " +
                    //add here if need page limit
                    //" limit ${page},${limit} " +
                    " </script>")
    List<Template> pageAll(Template queryParamDTO,int page,int limit);

    @Select("<script>select count(1) from template t0 " +
            //add here if need left join
            "where 1=1" +
            "<when test='id!=null and id!=&apos;&apos; '> and t0.id=#{id}</when> " +
            "<when test='name!=null and name!=&apos;&apos; '> and t0.name=#{name}</when> " +
            "<when test='value!=null and value!=&apos;&apos; '> and t0.value=#{value}</when> " +
            "<when test='description!=null and description!=&apos;&apos; '> and t0.description=#{description}</when> " +
            "<when test='page!=null and page!=&apos;&apos; '> and t0.page=#{page}</when> " +
            " </script>")
    int countAll(Template queryParamDTO);

}
