package com.softdev.cms.mapper;

import com.softdev.cms.entity.Channel;
import com.softdev.cms.entity.dto.QueryParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChannelMapper {

    Channel selectById(@Param("channelId") Integer channelId);

    Channel selectBySlug(@Param("slug") String slug);

    List<Channel> selectByParentChannelId(@Param("parentChannelId") Integer parentChannelId);

    List<Channel> selectAll();

    List<Channel> pageAll(@Param("query") Channel query, @Param("page") int page, @Param("limit") int limit);

    int countAll(@Param("query") Channel query);

    int insert(Channel channel);

    int updateById(Channel channel);

    int deleteById(@Param("channelId") Integer channelId);
}
