package com.softdev.cms.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.softdev.cms.entity.Activity;
import com.softdev.cms.entity.ActivitySign;
import com.softdev.cms.entity.FormSubmitValue;
import com.softdev.cms.entity.dto.QueryParamDTO;
import com.softdev.cms.mapper.ActivityMapper;
import com.softdev.cms.mapper.ActivitySignMapper;
import com.softdev.cms.util.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 * @description activity_sign
 * @author zhengkai.blog.csdn.net
 * @date 2020-02-29 19:42:09
 */
@Slf4j
@RestController
@RequestMapping("/activitySign")
public class ActivitySignController {

    @Autowired
    private ActivitySignMapper activitySignMapper;
    @Autowired
    private ActivityMapper activityMapper;

    /**
     * 新增或编辑
     */
    @PostMapping("/save")
    public Object save(@RequestBody ActivitySign activitySign){

        Activity activity = activityMapper.selectById(activitySign.getActivityId());

        log.info("activitySign:"+JSON.toJSONString(activitySign));
        ActivitySign oldActivitySign = activitySignMapper.selectOne(new QueryWrapper<ActivitySign>().eq("Sign_id",activitySign.getSignId()));
        if(oldActivitySign!=null){
            activitySignMapper.updateById(activitySign);
        }else{
            //判断是否已经签到
            if(activitySignMapper.selectOne(new QueryWrapper<ActivitySign>()
                    .eq("user_name",activitySign.getUserName())
                    .eq("activity_id",activitySign.getActivityId())
            )!=null){
                return ReturnT.ERROR("已签到，请勿重复签到");
            }
            //判断是否在时间范围内
            if(!DateUtil.isIn(new Date(),activity.getStartTime(),activity.getEndTime())){
                return ReturnT.ERROR("不在活动时间范围，不能签到");
            }
            activitySign.setSignTime(new Date());
            activitySignMapper.insert(activitySign);
        }
        return ReturnT.SUCCESS("签到成功");
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public Object delete(int id){
        ActivitySign activitySign = activitySignMapper.selectOne(new QueryWrapper<ActivitySign>().eq("sign_id",id));
        if(activitySign!=null){
            activitySignMapper.deleteById(id);
            return ReturnT.SUCCESS("删除成功");
        }else{
            return ReturnT.ERROR();
        }
    }

    /**
     * 查询
     */
    @PostMapping("/find")
    public Object find(Integer id,Integer activityId,String userName){
        ActivitySign activitySign = null;
        if(id!=null) {
            activitySign = activitySignMapper.selectOne(new QueryWrapper<ActivitySign>().eq("sign_id",id));
        }
        if(activityId!=null&&userName!=null) {
            activitySign = activitySignMapper.selectOne(new QueryWrapper<ActivitySign>().eq("activity_Id",activityId).eq("user_Code",userName));
        }
        if(activitySign!=null){
            return ReturnT.SUCCESS(activitySign);
        }else{
            return ReturnT.ERROR();
        }
    }

    /**
     * 分页查询
     */
    @PostMapping("/list2")
    public Object list2(String searchParams,
                       @RequestParam(required = false, defaultValue = "0") int page,
                       @RequestParam(required = false, defaultValue = "10") int limit) {
        log.info("page:"+page+"-limit:"+limit+"-json:"+ JSON.toJSONString(searchParams));
        //分页构造器
        Page<ActivitySign> buildPage = new Page<ActivitySign>(page,limit);
        //条件构造器
        QueryWrapper<ActivitySign> queryWrapper = new QueryWrapper<ActivitySign>();
        if(StringUtils.isNotEmpty(searchParams)) {
            ActivitySign activitySign = JSON.parseObject(searchParams, ActivitySign.class);
            queryWrapper.eq(activitySign.getActivityId()!=null, "activity_id", activitySign.getActivityId());
            queryWrapper.eq(StringUtils.isNotBlank(activitySign.getUserName()), "user_code", activitySign.getUserName());
            queryWrapper.eq(StringUtils.isNotBlank(activitySign.getUserName()), "user_name", activitySign.getUserName());
        }
        //执行分页
        IPage<ActivitySign> pageList = activitySignMapper.selectPage(buildPage, queryWrapper);
        //返回结果
        return ReturnT.PAGE(pageList.getRecords(),pageList.getTotal());
    }
    /**
     * 分页查询
     */
    @PostMapping("/list")
    public Object list(String searchParams,
                       @RequestParam(required = false, defaultValue = "0") int page,
                       @RequestParam(required = false, defaultValue = "10") int limit) {
        log.info("searchParams:"+ JSON.toJSONString(searchParams));
        //条件构造器
        QueryWrapper<FormSubmitValue> queryWrapper = new QueryWrapper<FormSubmitValue>();
        QueryParamDTO queryParamDTO = JSON.parseObject(searchParams, QueryParamDTO.class);
        queryParamDTO.setPage((page - 1)* limit);
        queryParamDTO.setLimit(limit);
        //(page - 1) * limit, limit
        List<ActivitySign> itemList = activitySignMapper.pageAll(queryParamDTO);
        Integer itemCount = activitySignMapper.countAll(queryParamDTO);
        //返回结果
        return ReturnT.PAGE(itemList,itemCount);
    }
    @GetMapping("/list")
    public ModelAndView listPage(Integer activityId){
        return new ModelAndView("cms/activitySign-list","activityId",activityId);
    }
    @GetMapping("/edit")
    public ModelAndView editPage(Integer id,Integer activityId){
        ActivitySign activitySign = activitySignMapper.selectOne(new QueryWrapper<ActivitySign>().eq("activity_id",activityId).eq("sign_id",id));
        return new ModelAndView("cms/activitySign-edit","activitySign",activitySign).addObject("activityId",activityId);
    }
    @GetMapping("/display")
    public ModelAndView display(Integer activityId){
        Activity activity = activityMapper.selectOne(new QueryWrapper<Activity>().eq("activity_id",activityId));
        return new ModelAndView("cms/activitySign-display","activity",activity).addObject("activityId",activityId);
    }
    @GetMapping("/signResult")
    public ModelAndView signResult(boolean isSuccess,String msg){
        if(isSuccess){
            return new ModelAndView("cms/success","msg",(msg!=null)?msg:"签到成功!");
        }else{
            return new ModelAndView("cms/error","msg",(msg!=null)?msg:"已签到，请勿重复签到!");
        }
    }
    @GetMapping("/qrcode")
    public ModelAndView qrcode(int activityId){
        Activity activity = activityMapper.selectOne(new QueryWrapper<Activity>().eq("activity_id",activityId));
        log.info(JSON.toJSONString(activity));
        return new ModelAndView("cms/activitySign-qrcode","activity",activity);
    }
}



