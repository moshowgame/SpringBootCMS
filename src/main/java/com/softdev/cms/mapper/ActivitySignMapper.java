package com.softdev.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.softdev.cms.entity.ActivitySign;
import com.softdev.cms.entity.dto.QueryParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description activity_sign
 * @author zhengkai.blog.csdn.net
 * @date 2020-02-29 19:42:09
 */
@Mapper
public interface ActivitySignMapper extends BaseMapper<ActivitySign> {


    @Select("select count(1) as 'value' from activity_sign t " +
            "inner join user t2 on t2.user_name = t.user_name " +
            "where t.activity_Id=#{activityId} and t.sign_type=#{signType}")
    Integer validSignTotal(Integer activityId, Integer signType);

    @Select("<script>select t0.* from activity_sign t0 " +
            "left join user t1 on t1.user_id=t0.user_id " +
            "where 1=1" +
            "<when test='showName!=null and showName!=&apos;&apos; '> and t0.show_name like '%${showName}%' </when> " +
            "<when test='userName!=null and userName!=&apos;&apos; ' > and t0.user_name=#{userName} </when> " +
            " limit ${page},${limit} </script>")
    List<ActivitySign> pageAll(QueryParamDTO queryParamDTO);

    @Select("<script>select count(1) from activity_sign t0 " +
            "left join user t1 on t1.user_id=t0.user_id " +
            "where 1=1" +
            "<when test='showName!=null and showName!=&apos;&apos; '> and t0.show_name like '%${showName}%' </when> " +
            "<when test='userName!=null and userName!=&apos;&apos; ' > and t0.user_name=#{userName} </when> " +
            " </script>")
    int countAll(QueryParamDTO queryParamDTO);

}
