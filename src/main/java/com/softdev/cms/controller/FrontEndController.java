package com.softdev.cms.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.softdev.cms.entity.Article;
import com.softdev.cms.entity.Template;
import com.softdev.cms.entity.dto.QueryParamDTO;
import com.softdev.cms.mapper.ArticleMapper;
import com.softdev.cms.mapper.TemplateMapper;
import com.softdev.cms.service.FrontEndService;
import com.softdev.cms.util.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 前端页面+模板值控制
 * @author zhengkai.blog.csdn.net
 */
@RestController
@RequestMapping("/page")
@Slf4j
public class FrontEndController {

    @Autowired
    TemplateMapper templateMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    FrontEndService frontEndService;

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
        queryWrapper.select("article_id","title","description","create_user_name","create_time")
                .eq(queryParamDTO.getChannelId()!=null,"channel_id",queryParamDTO.getChannelId())
                .orderByAsc("is_top","create_time")
            ;
        //执行分页
        IPage<Article> pageList = articleMapper.selectPage(buildPage, queryWrapper);
        //返回结果
        return ReturnT.PAGE(pageList.getRecords(),pageList.getTotal());
    }
}
