package com.softdev.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.softdev.cms.entity.Channel;
import java.util.List;

/**
 * @description 频道
 * @author zhengkai.blog.csdn.net
 * @date 2020-05-25
 */
@Mapper
public interface ChannelMapper extends BaseMapper<Channel> {

    @Select(
            "<script>select t0.* from channel t0 " +
                    //add here if need left join
                    "where 1=1" +
                    "<when test='channelId!=null and channelId!=&apos;&apos; '> and t0.channel_id=#{channelId}</when> " +
                    "<when test='channelName!=null and channelName!=&apos;&apos; '> and t0.channel_name=#{channelName}</when> " +
                    "<when test='parentChannelId!=null and parentChannelId!=&apos;&apos; '> and t0.parent_channel_id=#{parentChannelId}</when> " +
                    "<when test='icon!=null and icon!=&apos;&apos; '> and t0.icon=#{icon}</when> " +
                    //add here if need page limit
                    //" limit ${page},${limit} " +
                    " </script>")
    List<Channel> pageAll(Channel queryParamDTO,int page,int limit);

    @Select("<script>select count(1) from channel t0 " +
            //add here if need left join
            "where 1=1" +
            "<when test='channelId!=null and channelId!=&apos;&apos; '> and t0.channel_id=#{channelId}</when> " +
            "<when test='channelName!=null and channelName!=&apos;&apos; '> and t0.channel_name=#{channelName}</when> " +
            "<when test='parentChannelId!=null and parentChannelId!=&apos;&apos; '> and t0.parent_channel_id=#{parentChannelId}</when> " +
            "<when test='icon!=null and icon!=&apos;&apos; '> and t0.icon=#{icon}</when> " +
            " </script>")
    int countAll(Channel queryParamDTO);

}
