package com.softdev.cms.mapper;

import com.softdev.cms.entity.ActivitySign;
import com.softdev.cms.entity.dto.QueryParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ActivitySignMapper {

    ActivitySign selectById(@Param("signId") Integer signId);

    /**
     * 按活动ID和用户名查询签到记录（用于去重检查，admin 端）
     */
    ActivitySign selectByActivityAndUserName(@Param("activityId") Integer activityId,
                                              @Param("userName") String userName);

    /**
     * 按活动ID和手机号查询签到记录（用于去重检查，前台匿名签到）
     */
    ActivitySign selectByActivityAndPhone(@Param("activityId") Integer activityId,
                                           @Param("phone") String phone);

    List<ActivitySign> pageAll(QueryParamDTO queryParamDTO);

    int countAll(QueryParamDTO queryParamDTO);

    Integer validSignTotal(@Param("activityId") Integer activityId, @Param("signType") Integer signType);

    int insert(ActivitySign activitySign);

    int updateById(ActivitySign activitySign);

    int deleteById(@Param("signId") Integer signId);
}
