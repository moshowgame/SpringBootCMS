package com.softdev.cms.mapper;

import com.softdev.cms.entity.ActivitySign;
import com.softdev.cms.entity.dto.QueryParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ActivitySignMapper {

    ActivitySign selectById(@Param("signId") Integer signId);

    List<ActivitySign> pageAll(QueryParamDTO queryParamDTO);

    int countAll(QueryParamDTO queryParamDTO);

    Integer validSignTotal(@Param("activityId") Integer activityId, @Param("signType") Integer signType);

    int insert(ActivitySign activitySign);

    int updateById(ActivitySign activitySign);

    int deleteById(@Param("signId") Integer signId);
}
