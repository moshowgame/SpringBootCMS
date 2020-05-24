package com.softdev.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.softdev.cms.entity.ActivitySign;
import com.softdev.cms.entity.User;
import com.softdev.cms.entity.dto.QueryParamDTO;
import org.apache.ibatis.annotations.Mapper;
import com.softdev.cms.entity.FormSubmit;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description form_submit
 * @author zhengkai.blog.csdn.net
 * @date 2020-03-06 23:01:14
 */
@Mapper
public interface FormSubmitMapper extends BaseMapper<FormSubmit> {

    @Select("<script>select t0.*,t2.form_name from form_submit t0 " +
            "left join user t1 on t1.user_id=t0.user_id " +
            "left join form t2 on t2.form_id=t0.form_id " +
            "where 1=1" +
            "<when test='formId!=null and formId!=&apos;&apos; '> and t0.form_Id=#{formId}</when> " +
            "<when test='userId!=null and userId!=&apos;&apos; '> and t0.user_Id=#{userId}</when> " +
            "<when test='userName!=null and userName!=&apos;&apos; '> and t1.user_name=#{userName} </when> " +
            "<when test='showName!=null and showName!=&apos;&apos; ' > and t1.show_Name like '%${showName}%' </when> " +
            " limit ${page},${limit} </script>")
    List<FormSubmit> pageAll(QueryParamDTO queryParamDTO);

    @Select("<script>select count(1) from form_submit t0 " +
            "left join user t1 on t1.user_id=t0.user_id " +
            "left join form t2 on t2.form_id=t0.form_id " +
            "where 1=1" +
            "<when test='formId!=null and formId!=&apos;&apos; '> and t0.form_Id=#{formId}</when> " +
            "<when test='userId!=null and userId!=&apos;&apos; '> and t0.user_Id=#{userId}</when> " +
            "<when test='userName!=null and userName!=&apos;&apos; '> and t1.user_name=#{userName} </when> " +
            "<when test='showName!=null and showName!=&apos;&apos; ' > and t1.show_Name like '%${showName}%' </when> " +
            " </script>")
    int countAll(QueryParamDTO queryParamDTO);

}
