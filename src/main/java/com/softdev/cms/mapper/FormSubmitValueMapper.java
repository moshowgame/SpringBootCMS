package com.softdev.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.softdev.cms.entity.FormSubmitValue;
import com.softdev.cms.entity.dto.QueryParamDTO;
import com.softdev.cms.entity.dto.FormSubmitValueDTO;

import com.softdev.cms.entity.dto.FormSubmitValueExcelDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @description form_submit_value
 * @author zhengkai.blog.csdn.net
 * @date 2020-03-06 23:02:07
 */
@Mapper
public interface FormSubmitValueMapper extends BaseMapper<FormSubmitValue> {

    @Select("select t1.item_id,t1.item_name,t1.item_type,t1.default_value,t2.value_text,t2.user_id from form_item t1\n" +
            "left join form_submit_value t2 on t2.item_id=t1.item_id and t2.submit_id=#{submitId}\n" +
            "where t1.form_id=#{formId} order by t1.item_id")
    List<FormSubmitValueDTO> getFormSubmitValue(Integer formId, Integer submitId);

    @Select("<script>select t.user_id,t.show_name,t.user_name,t1.submit_id,t2.item_id,t2.item_name,t2.item_type,t1.value_text from user t0\n" +
            "left join form_submit t3 on t3.user_id=t0.user_id and t3.form_id=#{formId}\n" +
            "left join form_submit_value t1 on t1.submit_id= t3.submit_id\n" +
            "left join form_item t2 on t2.item_id=t1.item_id\n" +
            "where t2.item_name is not null\n" +
            "<when test='userId!=null and userId!=&apos;&apos; '> and t0.user_Id=#{userId}</when> " +
            "<when test='userName!=null and userName!=&apos;&apos; '> and t0.user_name=#{userName} </when> " +
            "<when test='showName!=null and showName!=&apos;&apos; ' > and t0.show_Name like '%${showName}%' </when> " +
            "ORDER BY t1.user_id,t1.value_id</script>")
    List<FormSubmitValueExcelDTO> getFormSubmitValueExcel(QueryParamDTO queryParam);

    @Select("SELECT t1.item_id,t0.item_name,t1.value_text,count(*) as count FROM `form_item` t0 \n" +
            "left join form_submit_value t1 on t1.item_id=t0.item_id\n" +
            "where t0.item_type='select'\n" +
            "GROUP BY t0.item_id,t0.item_name,t1.value_text")
    List<FormSubmitValueDTO> getSelectCountList();
}
