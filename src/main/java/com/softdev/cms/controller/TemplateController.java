package com.softdev.cms.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.softdev.cms.entity.Template;
import com.softdev.cms.mapper.TemplateMapper;
import com.softdev.cms.util.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @description template
 * @author zhengkai.blog.csdn.net
 * @date 2020-05-31
 */
@Slf4j
@RestController
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private TemplateMapper templateMapper;

    /**
     * 新增或编辑
     */
    @PostMapping("/save")
    public Object save(@RequestBody Template template){
        log.info("template:"+JSON.toJSONString(template));
        Template oldTemplate = templateMapper.selectOne(new QueryWrapper<Template>().eq("template_id",template.getTemplateId()));
        if(oldTemplate!=null){
            templateMapper.updateById(template);
        }else{
            if(templateMapper.selectOne(new QueryWrapper<Template>().eq("template_name",template.getTemplateName()))!=null){
                return ReturnT.ERROR("保存失败，名字重复");
            }
            templateMapper.insert(template);
        }
        return ReturnT.SUCCESS("保存成功");
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public Object delete(int id){
        Template template = templateMapper.selectOne(new QueryWrapper<Template>().eq("template_id",id));
        if(template!=null){
            templateMapper.deleteById(id);
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
        Template template = templateMapper.selectOne(new QueryWrapper<Template>().eq("template_id",id));
        if(template!=null){
            return ReturnT.SUCCESS(template);
        }else{
            return ReturnT.ERROR("没有找到该对象");
        }
    }

    /**
     * 自动分页查询
     */
    @PostMapping("/list")
    public Object list(String searchParams,
                       @RequestParam(required = false, defaultValue = "0") int page,
                       @RequestParam(required = false, defaultValue = "10") int limit) {
        log.info("page:"+page+"-limit:"+limit+"-json:"+ JSON.toJSONString(searchParams));
        //分页构造器
        Page<Template> buildPage = new Page<Template>(page,limit);
        //条件构造器
        QueryWrapper<Template> queryWrapper = new QueryWrapper<Template>();
        if(StringUtils.isNotEmpty(searchParams)&&JSON.isValid(searchParams)) {
            Template template = JSON.parseObject(searchParams, Template.class);
            queryWrapper.eq(StringUtils.isNotEmpty(template.getPage()),"page",template.getPage());
            queryWrapper.eq(StringUtils.isNotEmpty(template.getTemplateName()), "template_name", template.getTemplateName());
        }
        //执行分页
        IPage<Template> pageList = templateMapper.selectPage(buildPage, queryWrapper);
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
        Template queryParamDTO = JSON.parseObject(searchParams, Template.class);
        //专用DTO模式
        //QueryParamDTO queryParamDTO = JSON.parseObject(searchParams, QueryParamDTO.class);
        //queryParamDTO.setPage((page - 1)* limit);
        //queryParamDTO.setLimit(limit);
        //(page - 1) * limit, limit
        List<Template> itemList = templateMapper.pageAll(queryParamDTO,(page - 1)* limit,limit);
        Integer itemCount = templateMapper.countAll(queryParamDTO);
        //返回结果
        return ReturnT.PAGE(itemList,itemCount);
    }
    @GetMapping("/list")
    public ModelAndView listPage(){
        return new ModelAndView("cms/template-list");
    }

    @GetMapping("/edit")
    public ModelAndView editPage(int id){
        Template template = templateMapper.selectOne(new QueryWrapper<Template>().eq("template_id",id));
        return new ModelAndView("cms/template-edit","template",template);
    }

    /**
     * 发布/暂停(如不需要请屏蔽)
     */
/*    @PostMapping("/publish")
    public Object publish(int id,Integer status){
        Template template = templateMapper.selectOne(new QueryWrapper<Template>().eq("template_id",id));
        if(template!=null){
            template.setUpdateTime(new Date());
            template.setStatus(status);
            templateMapper.updateById(template);
            return ReturnT.SUCCESS((status==1)?"已发布":"已暂停");
        }else if(status.equals(template.getStatus())){
            return ReturnT.SUCCESS("状态不正确");
        }else{
            return ReturnT.ERROR();
        }
    }*/

}



