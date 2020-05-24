package com.softdev.cms.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.softdev.cms.entity.Activity;
import com.softdev.cms.mapper.ActivityMapper;
import com.softdev.cms.util.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

/**
 * @description activity
 * @author zhengkai.blog.csdn.net
 * @date 2020-02-26 23:27:13
 */
@Slf4j
@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityMapper activityMapper;

    /**
     * 新增或编辑
     */
    @PostMapping("/save")
    public Object save(@RequestBody Activity activity){
        log.info("activity:"+JSON.toJSONString(activity));
        Activity oldActivity = activityMapper.selectOne(new QueryWrapper<Activity>().eq("activity_id",activity.getActivityId()));
        activity.setUpdateTime(new Date());
        if(oldActivity!=null){
            activityMapper.updateById(activity);
        }else{
            if(activityMapper.selectOne(new QueryWrapper<Activity>().eq("activity_name",activity.getActivityName()))!=null){
                return ReturnT.ERROR("保存失败，活动名不能重复");
            }
            activity.setCreateTime(new Date());
            activityMapper.insert(activity);
        }
        return ReturnT.SUCCESS("保存成功");
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public Object delete(int id){
        Activity activity = activityMapper.selectOne(new QueryWrapper<Activity>().eq("activity_id",id));
        if(activity!=null){
            activityMapper.deleteById(id);
            return ReturnT.SUCCESS("删除成功");
        }else{
            return ReturnT.ERROR();
        }
    }

    /**
     * 查询
     */
    @PostMapping("/find")
    public Object find(int id){
        Activity activity = activityMapper.selectOne(new QueryWrapper<Activity>().eq("activity_id",id));
        if(activity!=null){
            return ReturnT.SUCCESS(activity);
        }else{
            return ReturnT.ERROR();
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
        Page<Activity> buildPage = new Page<Activity>(page,limit);
        //条件构造器
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<Activity>();
        if(StringUtils.isNotEmpty(searchParams)) {
            Activity activity = JSON.parseObject(searchParams, Activity.class);
            queryWrapper.eq(StringUtils.isNoneEmpty(activity.getActivityName()), "activity_name", activity.getActivityName());
            queryWrapper.eq(activity.getStatus()!=null,"status",activity.getStatus());
        }
        queryWrapper.orderByDesc("start_time");
        //执行分页
        IPage<Activity> pageList = activityMapper.selectPage(buildPage, queryWrapper);
        //返回结果
        return ReturnT.PAGE(pageList.getRecords(),pageList.getTotal());
    }
    @GetMapping("/list")
    public ModelAndView listPage(){
        return new ModelAndView("cms/activity-list");
    }
    @GetMapping("/edit")
    public ModelAndView editPage(int id){
        Activity activity = activityMapper.selectOne(new QueryWrapper<Activity>().eq("activity_id",id));
        log.info(JSON.toJSONString(activity));
        return new ModelAndView("cms/activity-edit","activity",activity);
    }
    /**
     * 发布/暂停
     */
    @PostMapping("/publish")
    public Object publish(int id,Integer status){
        Activity activity = activityMapper.selectOne(new QueryWrapper<Activity>().eq("activity_id",id));
        if(activity!=null){
            activity.setUpdateTime(new Date());
            activity.setStatus(status);
            activityMapper.updateById(activity);
            return ReturnT.SUCCESS((status==1)?"活动已发布":"活动已暂停");
        }else if(status.equals(activity.getStatus())){
            return ReturnT.SUCCESS("活动状态不正确");
        }else{
            return ReturnT.ERROR();
        }
    }
}



