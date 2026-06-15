package com.softdev.cms.mapper;

import com.softdev.cms.entity.User;
import com.softdev.cms.entity.dto.QueryParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {

    User selectByUserName(@Param("userName") String userName);

    User selectById(@Param("userId") Integer userId);

    List<User> pageAll(QueryParamDTO queryParamDTO);

    int countAll(QueryParamDTO queryParamDTO);

    int insert(User user);

    int updateById(User user);

    int deleteById(@Param("userId") Integer userId);

    int count();
}
