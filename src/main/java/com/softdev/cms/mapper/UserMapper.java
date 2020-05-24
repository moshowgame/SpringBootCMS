package com.softdev.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.softdev.cms.entity.User;
import com.softdev.cms.entity.dto.QueryParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description user
 * @author zhengkai.blog.csdn.net
 * @date 2020-02-15 22:06:34
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user t where t.user_name=#{username} and t.password=#{password} ")
    public User login(String username,String password);

    @Select("select count(1) from user t ")
    public int count();

}