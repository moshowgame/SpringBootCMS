package com.softdev.cms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softdev.cms.entity.Channel;
import com.softdev.cms.mapper.ChannelMapper;
import com.softdev.cms.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    private ChannelMapper channelMapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/save")
    @CacheEvict(value = "cache-channel", allEntries = true)
    public Result<String> save(@RequestBody Channel channel) {
        try {
            log.info("channel:{}", objectMapper.writeValueAsString(channel));
        } catch (Exception e) {
            log.warn("serialize channel failed", e);
        }
        Channel oldChannel = channelMapper.selectById(channel.getChannelId());
        if (oldChannel != null) {
            channelMapper.updateById(channel);
        } else {
            List<Channel> all = channelMapper.selectAll();
            boolean nameDuplicate = all.stream()
                    .anyMatch(c -> c.getChannelName().equals(channel.getChannelName()));
            if (nameDuplicate) {
                return Result.fail("保存失败，名字重复");
            }
            channelMapper.insert(channel);
        }
        return Result.success("保存成功");
    }

    @PostMapping("/delete")
    @CacheEvict(value = "cache-channel", allEntries = true)
    public Result<String> delete(@RequestParam Integer id) {
        Channel channel = channelMapper.selectById(id);
        if (channel != null) {
            channelMapper.deleteById(id);
            return Result.success("删除成功");
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/find")
    @Cacheable(value = "cache-channel", key = "#id")
    public Result<Channel> find(@RequestParam Integer id) {
        Channel channel = channelMapper.selectById(id);
        if (channel != null) {
            return Result.success(channel);
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/list")
    public Result<List<Channel>> list(@RequestParam(required = false) String searchParams,
                                      @RequestParam(required = false, defaultValue = "1") int page,
                                      @RequestParam(required = false, defaultValue = "10") int limit) {
        Channel query = new Channel();
        if (StringUtils.isNotEmpty(searchParams)) {
            try {
                query = objectMapper.readValue(searchParams, Channel.class);
            } catch (Exception e) {
                log.warn("parse searchParams failed", e);
            }
        }
        List<Channel> itemList = channelMapper.pageAll(query, (page - 1) * limit, limit);
        int itemCount = channelMapper.countAll(query);
        return Result.success(itemList, itemCount);
    }

    @GetMapping("/list")
    public ModelAndView listPage() {
        return new ModelAndView("cms/channel-list");
    }

    @GetMapping("/edit")
    public ModelAndView editPage(@RequestParam Integer id) {
        Channel channel = channelMapper.selectById(id);
        return new ModelAndView("cms/channel-edit", "channel", channel);
    }

    @PostMapping("/publish")
    @CacheEvict(value = "cache-channel", allEntries = true)
    public Result<String> publish(@RequestParam Integer id, @RequestParam Integer status) {
        Channel channel = channelMapper.selectById(id);
        if (channel != null) {
            channel.setStatus(status);
            channelMapper.updateById(channel);
            return Result.success((status == 1) ? "已发布" : "已暂停");
        } else {
            return Result.fail("操作失败");
        }
    }
}
