package com.softdev.cms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softdev.cms.entity.Activity;
import com.softdev.cms.entity.dto.QueryParamDTO;
import com.softdev.cms.mapper.ActivityMapper;
import com.softdev.cms.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityMapper activityMapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/save")
    @CacheEvict(value = "cache-activity", allEntries = true)
    public Result<String> save(@RequestBody Activity activity) {
        try {
            log.info("activity:{}", objectMapper.writeValueAsString(activity));
        } catch (Exception e) {
            log.warn("serialize activity failed", e);
        }
        Activity oldActivity = activityMapper.selectById(activity.getActivityId());
        activity.setUpdateTime(new Date());
        if (oldActivity != null) {
            activityMapper.updateById(activity);
        } else {
            // 检查活动名重复 - 通过分页查询验证
            QueryParamDTO checkDto = new QueryParamDTO();
            checkDto.setPageLimit(1, 1);
            List<Activity> existing = activityMapper.pageAll(checkDto);
            boolean nameDuplicate = existing.stream()
                    .anyMatch(a -> activity.getActivityName().equals(a.getActivityName()));
            if (nameDuplicate) {
                return Result.fail("保存失败，活动名不能重复");
            }
            activity.setCreateTime(new Date());
            activityMapper.insert(activity);
        }
        return Result.success("保存成功");
    }

    @PostMapping("/delete")
    @CacheEvict(value = "cache-activity", allEntries = true)
    public Result<String> delete(@RequestParam Integer id) {
        Activity activity = activityMapper.selectById(id);
        if (activity != null) {
            activityMapper.deleteById(id);
            return Result.success("删除成功");
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/find")
    @Cacheable(value = "cache-activity", key = "#id")
    public Result<Activity> find(@RequestParam Integer id) {
        Activity activity = activityMapper.selectById(id);
        if (activity != null) {
            return Result.success(activity);
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/list")
    public Result<List<Activity>> list(@RequestParam(required = false) String searchParams,
                                        @RequestParam(required = false, defaultValue = "1") int page,
                                        @RequestParam(required = false, defaultValue = "10") int limit) {
        QueryParamDTO queryParamDTO = new QueryParamDTO();
        if (StringUtils.isNotEmpty(searchParams)) {
            try {
                queryParamDTO = objectMapper.readValue(searchParams, QueryParamDTO.class);
            } catch (Exception e) {
                log.warn("parse searchParams failed", e);
            }
        }
        queryParamDTO.setPageLimit(page, limit);
        List<Activity> itemList = activityMapper.pageAll(queryParamDTO);
        int itemTotal = activityMapper.countAll(queryParamDTO);
        return Result.success(itemList, itemTotal);
    }

    @GetMapping("/list")
    public ModelAndView listPage() {
        return new ModelAndView("cms/activity-list");
    }

    @GetMapping("/edit")
    public ModelAndView editPage(@RequestParam Integer id) {
        Activity activity = activityMapper.selectById(id);
        return new ModelAndView("cms/activity-edit", "activity", activity);
    }

    @PostMapping("/publish")
    @CacheEvict(value = "cache-activity", allEntries = true)
    public Result<String> publish(@RequestParam Integer id, @RequestParam Integer status) {
        Activity activity = activityMapper.selectById(id);
        if (activity != null) {
            activity.setUpdateTime(new Date());
            activity.setStatus(status);
            activityMapper.updateById(activity);
            return Result.success((status == 1) ? "活动已发布" : "活动已暂停");
        } else {
            return Result.fail("操作失败");
        }
    }
}
