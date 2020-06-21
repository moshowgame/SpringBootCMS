package com.softdev.cms.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.softdev.cms.entity.*;
import com.softdev.cms.entity.dto.FormSubmitValueDTO;
import com.softdev.cms.entity.dto.QueryParamDTO;
import com.softdev.cms.mapper.*;
import com.softdev.cms.service.FrontEndService;
import com.softdev.cms.util.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 前端页面+模板值控制
 * @author zhengkai.blog.csdn.net
 */
@RestController
@RequestMapping("/page")
@Slf4j
public class FrontEndController {

    @Autowired
    FrontEndService frontEndService;

    @Autowired
    TemplateMapper templateMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    ActivitySignMapper activitySignMapper;

    @Autowired
    FormMapper formMapper;

    @Autowired
    FormSubmitMapper formSubmitMapper;

    @Autowired
    FormSubmitValueMapper formSubmitValueMapper;

    /**
     * 首页
     */
    @GetMapping("index")
    public ModelAndView indexPage(){

        return new ModelAndView("frontend/index")
                //频道列表
                .addObject("channelList",frontEndService.getChannelList(1))
                //获取模板值
                .addObject("templateMap",frontEndService.getTemplateMap("index"));
    }
    /**
     * 频道页面
     */
    @GetMapping("/channel/{channelId}")
    public ModelAndView channelPage(@PathVariable Integer channelId){
        return new ModelAndView("/frontend/channel")
                .addObject("channelId",channelId)
                .addObject("channel",frontEndService.getChannelById(channelId))
                //频道列表
                .addObject("channelList",frontEndService.getChannelList(1))
                //获取模板值
                .addObject("templateMap",frontEndService.getTemplateMap("channel"));
    }
    /**
     * 文章页面
     */
    @GetMapping("article/{articleId}")
    public ModelAndView articlePage(@PathVariable String articleId){
        //判断文章是否启用

        return new ModelAndView("frontend/article")
                //频道列表
                .addObject("channelList",frontEndService.getChannelList(1))
                //获取模板值
                .addObject("templateMap",frontEndService.getTemplateMap("article"))
                //文章内容
                .addObject("article",frontEndService.getArticle(articleId));
    }
    /**
     * 文章列表接口
     */
    @PostMapping("article/list")
    public ReturnT getArticleList(@RequestBody QueryParamDTO queryParamDTO) {
        log.info(JSON.toJSONString(queryParamDTO));
        //queryParamDTO.setPageLimit();
        //分页构造器
        Page<Article> buildPage = new Page<Article>(queryParamDTO.getPage(),queryParamDTO.getLimit());
        //条件构造器
        QueryWrapper<Article> queryWrapper = new QueryWrapper<Article>();
        //支持channelId和标题/关键字/内容多重维度搜索
        queryWrapper.select("article_id","title","description","create_user_name","create_time")
                .eq(queryParamDTO.getChannelId()!=null,"channel_id",queryParamDTO.getChannelId())
                .like(StringUtils.isNotEmpty(queryParamDTO.getSearchParam()),"title",queryParamDTO.getSearchParam())
                .or()
                .like(StringUtils.isNotEmpty(queryParamDTO.getSearchParam()),"keyword",queryParamDTO.getSearchParam())
                .or()
                .like(StringUtils.isNotEmpty(queryParamDTO.getSearchParam()),"content",queryParamDTO.getSearchParam())
                .orderByDesc("is_top","create_time")
            ;
        //执行分页
        IPage<Article> pageList = articleMapper.selectPage(buildPage, queryWrapper);
        //返回结果
        return ReturnT.PAGE(pageList.getRecords(),pageList.getTotal());
    }
    /**
     * 搜索页面
     */
    @GetMapping("/search")
    public ModelAndView getArticleSearchPage(){
        return new ModelAndView("/frontend/search")
                .addObject("channelList",frontEndService.getChannelList(1))
                //获取模板值
                .addObject("templateMap",frontEndService.getTemplateMap("search"));
    }
    /**
     * 活动搜索页面
     */
    @GetMapping("/activity/search")
    public ModelAndView getActivitySearchPage(){
        return new ModelAndView("/frontend/activity-search")
                .addObject("channelList",frontEndService.getChannelList(1))
                //获取模板值
                .addObject("templateMap",frontEndService.getTemplateMap("search"));
    }
    /**
     * 活动签到页面
     */
    @GetMapping("/activitySign/{activityId}")
    public ModelAndView getActivitySignPage(@PathVariable Integer activityId){
        return new ModelAndView("/frontend/activity-sign")
                .addObject("channelList",frontEndService.getChannelList(1))
                .addObject("activity",activityMapper.selectById(activityId))
                //获取模板值
                .addObject("templateMap",frontEndService.getTemplateMap("search"));
    }
    /**
     * 表单搜索页面
     */
    @GetMapping("/form/search")
    public ModelAndView getFormSearchPage(){
        return new ModelAndView("/frontend/form-search")
                .addObject("channelList",frontEndService.getChannelList(1))
                //获取模板值
                .addObject("templateMap",frontEndService.getTemplateMap("search"));
    }
    /**
     * 表单提交页面
     */
    @GetMapping("/formSubmit/{formId}")
    public ModelAndView getFormSubmitPage(@PathVariable Integer formId){
        List<FormSubmitValueDTO> itemValueList = formSubmitValueMapper.getFormSubmitValue(formId,0);
        return new ModelAndView("/frontend/form-sign")
                .addObject("channelList",frontEndService.getChannelList(1))
                .addObject("itemValueList",JSON.toJSONString(itemValueList))
                .addObject("form",formMapper.selectById(formId))
                //获取模板值
                .addObject("templateMap",frontEndService.getTemplateMap("search"));
    }
    /**
     * 活动提交接口
     */
    @PostMapping("/activitySign/save")
    public ReturnT activitySignSave(@RequestBody ActivitySign activitySign){
        log.info("activitySign:"+JSON.toJSONString(activitySign));
        Activity activity = activityMapper.selectById(activitySign.getActivityId());
        if(activity==null||activity.getStatus()<1){
            return ReturnT.ERROR("活动无效");
        }else{
            //判断是否已经签到
            if(activitySignMapper.selectOne(new QueryWrapper<ActivitySign>()
                    .eq("phone",activitySign.getPhone())
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
     * 活动列表接口
     */
    @PostMapping("activity/list")
    public ReturnT getActivityList(@RequestBody QueryParamDTO queryParamDTO) {
        log.info(JSON.toJSONString(queryParamDTO));
        //queryParamDTO.setPageLimit();
        //分页构造器
        Page<Activity> buildPage = new Page<Activity>(queryParamDTO.getPage(),queryParamDTO.getLimit());
        //条件构造器
        QueryWrapper<Activity> queryWrapper = new QueryWrapper<Activity>();
        //支持channelId和标题/关键字/内容多重维度搜索
        queryWrapper
                .like(StringUtils.isNotEmpty(queryParamDTO.getSearchParam()),"activity_name",queryParamDTO.getSearchParam())
                .eq("status",1)
                .orderByDesc("start_time","create_time")
        ;
        //执行分页
        IPage<Activity> pageList = activityMapper.selectPage(buildPage, queryWrapper);
        //返回结果
        return ReturnT.PAGE(pageList.getRecords(),pageList.getTotal());
    }
    /**
     * 活动列表接口
     */
    @PostMapping("form/list")
    public ReturnT getFormList(@RequestBody QueryParamDTO queryParamDTO) {
        log.info(JSON.toJSONString(queryParamDTO));
        //queryParamDTO.setPageLimit();
        //分页构造器
        Page<Form> buildPage = new Page<Form>(queryParamDTO.getPage(),queryParamDTO.getLimit());
        //条件构造器
        QueryWrapper<Form> queryWrapper = new QueryWrapper<Form>();
        //支持channelId和标题/关键字/内容多重维度搜索
        queryWrapper
                .like(StringUtils.isNotEmpty(queryParamDTO.getSearchParam()),"form_name",queryParamDTO.getSearchParam())
                .eq("status",1)
                .orderByDesc("create_time")
        ;
        //执行分页
        IPage<Form> pageList = formMapper.selectPage(buildPage, queryWrapper);
        //返回结果
        return ReturnT.PAGE(pageList.getRecords(),pageList.getTotal());
    }
    /**
     * 新增或编辑
     */
    @PostMapping("/formSubmit/save")
    @Transactional(rollbackFor = Exception.class)
    public Object formSubmitSave(@RequestBody String submitData){
        log.info("formSubmitValue:"+submitData);
        JSONArray jsonArray = JSONArray.parseArray(submitData);
        Map<String,String> baseInfoMap = new HashMap<>();
        for(int i=0;i<jsonArray.size();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            baseInfoMap.put(jsonObject.getString("name"),jsonObject.getString("value"));
        }
        Integer formId = Integer.valueOf(baseInfoMap.get("formId"));
        String phone = baseInfoMap.get("phone");
        String company = baseInfoMap.get("company");
        String showName = baseInfoMap.get("showName");
        Form form = formMapper.selectById(formId);
        //根据手机号和表单来判断重复
        FormSubmit formSubmit = formSubmitMapper.selectOne(
                new QueryWrapper<FormSubmit>().eq("form_Id",formId).eq("phone",phone)
        );
        String msg="";
        //有submitId，直接更新，否则新增
        if(formSubmit!=null){
            formSubmit.setUpdateTime(new Date());
            //如果是被拒绝的重新提交，则重置状态
            if(formSubmit.getStatus()<1){
                formSubmit.setStatus(1);
            }
            formSubmit.setCompany(company);
            formSubmit.setShowName(showName);
            formSubmitMapper.updateById(formSubmit);
            msg="已更新该手机号绑定的提交信息，编号:"+formSubmit.getSubmitId();
        }else{
            formSubmit = new FormSubmit(formId,showName,phone,company);
            formSubmitMapper.insert(formSubmit);
            msg="提交成功，编号:"+formSubmit.getSubmitId();
        }
        //处理子项
        for(String key:baseInfoMap.keySet()){
            if("formId".equals(key)|| "userId".equals(key)|| "userName".equals(key)|| "showName".equals(key)|| "file".equals(key)|| "submitId".equals(key)|| "phone".equals(key)|| "company".equals(key)){

            }else{
                Integer itemId = Integer.valueOf(key);
                String itemValue = baseInfoMap.get(key);
                FormSubmitValue formSubmitValue = formSubmitValueMapper.selectOne(new QueryWrapper<FormSubmitValue>().eq("form_id",formId).eq("submit_id",formSubmit.getSubmitId()).eq("item_id",itemId));
                if(formSubmitValue!=null){
                    formSubmitValue.setValueText(itemValue);
                    formSubmitValueMapper.updateById(formSubmitValue);
                } else{
                    formSubmitValue = new FormSubmitValue(formId,formSubmit.getSubmitId(),itemId,itemValue);
                    formSubmitValueMapper.insert(formSubmitValue);
                }
            }
        }
        return ReturnT.SUCCESS(msg);
    }
}
