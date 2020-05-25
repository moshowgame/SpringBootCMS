package com.softdev.cms.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.softdev.cms.entity.Channel;
import com.softdev.cms.entity.dto.QueryParamDTO;
import com.softdev.cms.mapper.ChannelMapper;
import com.softdev.cms.util.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @description 频道
 * @author zhengkai.blog.csdn.net
 * @date 2020-05-25
 */
@Slf4j
@RestController
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    private ChannelMapper channelMapper;

    /**
     * 新增或编辑
     */
    @PostMapping("/save")
    public Object save(@RequestBody Channel channel){
        log.info("channel:"+JSON.toJSONString(channel));
        Channel oldChannel = channelMapper.selectOne(new QueryWrapper<Channel>().eq("channel_id",channel.getChannelId()));
        if(oldChannel!=null){
            channelMapper.updateById(channel);
        }else{
            if(channelMapper.selectOne(new QueryWrapper<Channel>().eq("channel_name",channel.getChannelName()))!=null){
                return ReturnT.ERROR("保存失败，名字重复");
            }
            channelMapper.insert(channel);
        }
        return ReturnT.SUCCESS("保存成功");
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public Object delete(int id){
        Channel channel = channelMapper.selectOne(new QueryWrapper<Channel>().eq("channel_id",id));
        if(channel!=null){
            channelMapper.deleteById(id);
            return ReturnT.SUCCESS("删除成功");
        }else{
            return ReturnT.ERROR("没有找到该对象");
        }
    }

    /**
     * 查询
     */
    @PostMapping("/find")
    public Object find(int id){
        Channel channel = channelMapper.selectOne(new QueryWrapper<Channel>().eq("channel_id",id));
        if(channel!=null){
            return ReturnT.SUCCESS(channel);
        }else{
            return ReturnT.ERROR("没有找到该对象");
        }
    }

    /**
     * 分页查询
     */
    @PostMapping("/list")
    public Object list(String searchParams,
                       @RequestParam(required = false, defaultValue = "0") int page,
                       @RequestParam(required = false, defaultValue = "10") int limit) {
        log.info("page:"+page+"-limit:"+limit+"-json:"+ JSON.toJSONString(searchParams));
        //分页构造器
        Page<Channel> buildPage = new Page<Channel>(page,limit);
        //条件构造器
        QueryWrapper<Channel> queryWrapper = new QueryWrapper<Channel>();
        if(StringUtils.isNotEmpty(searchParams)&&JSON.isValid(searchParams)) {
            Channel channel = JSON.parseObject(searchParams, Channel.class);
            queryWrapper.eq(StringUtils.isNoneEmpty(channel.getChannelName()), "channel_name", channel.getChannelName());
        }
        //执行分页
        IPage<Channel> pageList = channelMapper.selectPage(buildPage, queryWrapper);
        //返回结果
        return ReturnT.PAGE(pageList.getRecords(),pageList.getTotal());
    }
    /**
     * 手工分页查询(按需使用)
     */
    @PostMapping("/list2")
    public ReturnT list2(String searchParams,
                         @RequestParam(required = false, defaultValue = "0") int page,
                         @RequestParam(required = false, defaultValue = "10") int limit) {
        log.info("searchParams:"+ JSON.toJSONString(searchParams));
        //通用模式
        Channel queryParamDTO = JSON.parseObject(searchParams, Channel.class);
        //专用DTO模式
        //QueryParamDTO queryParamDTO = JSON.parseObject(searchParams, QueryParamDTO.class);
        //queryParamDTO.setPage((page - 1)* limit);
        //queryParamDTO.setLimit(limit);
        //(page - 1) * limit, limit
        List<Channel> itemList = channelMapper.pageAll(queryParamDTO,(page - 1)* limit,limit);
        Integer itemCount = channelMapper.countAll(queryParamDTO);
        //返回结果
        return ReturnT.PAGE(itemList,itemCount);
    }
    @GetMapping("/list")
    public ModelAndView listPage(){
        return new ModelAndView("cms/channel-list");
    }

    @GetMapping("/edit")
    public ModelAndView editPage(int id){
        Channel channel = channelMapper.selectOne(new QueryWrapper<Channel>().eq("channel_id",id));
        return new ModelAndView("cms/channel-edit","channel",channel);
    }
}



