package com.softdev.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.softdev.cms.entity.Activity;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description activity
 * @author zhengkai.blog.csdn.net
 * @date 2020-02-26 23:27:13
 */
@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {


}
