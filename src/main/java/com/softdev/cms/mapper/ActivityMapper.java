package com.softdev.cms.mapper;

import com.softdev.cms.entity.Activity;
import com.softdev.cms.entity.dto.QueryParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ActivityMapper {

    Activity selectById(@Param("activityId") Integer activityId);

    List<Activity> pageAll(QueryParamDTO queryParamDTO);

    int countAll(QueryParamDTO queryParamDTO);

    int insert(Activity activity);

    int updateById(Activity activity);

    int deleteById(@Param("activityId") Integer activityId);
}
