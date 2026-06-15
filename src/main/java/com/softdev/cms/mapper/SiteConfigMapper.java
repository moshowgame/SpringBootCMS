package com.softdev.cms.mapper;

import com.softdev.cms.entity.SiteConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SiteConfigMapper {

    SiteConfig selectById(@Param("configId") Integer configId);

    SiteConfig selectByKey(@Param("configKey") String configKey);

    List<SiteConfig> selectByGroup(@Param("configGroup") String configGroup);

    List<SiteConfig> selectAll();

    int insert(SiteConfig siteConfig);

    int updateById(SiteConfig siteConfig);

    int deleteById(@Param("configId") Integer configId);
}
