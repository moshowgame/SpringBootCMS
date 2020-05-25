package com.softdev.cms.controller;

import com.alibaba.fastjson.JSON;
import com.softdev.cms.entity.Article;
import com.softdev.cms.mapper.ArticleMapper;
import com.softdev.cms.util.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description 文章
 * @author zhengkai.blog.csdn.net
 * @date 2020-05-25 20:33:02
 */
@Slf4j
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 新增或编辑
     */
    @PostMapping("/save")
    public Object save(@RequestBody Article article){
        log.info("article:"+JSON.toJSONString(article));
        Article oldArticle = articleMapper.selectOne(new QueryWrapper<Article>().eq("article_id",article.getArticleId()));
        article.setUpdateTime(new Date());
        if(oldArticle!=null){
            articleMapper.updateById(article);
        }else{
            if(articleMapper.selectOne(new QueryWrapper<Article>().eq("title",article.getTitle()))!=null){
                return ReturnT.ERROR("保存失败，标题重复");
            }
            article.setCreateTime(new Date());
            articleMapper.insert(article);
        }
        return ReturnT.SUCCESS("保存成功");
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public Object delete(int id){
        Article article = articleMapper.selectOne(new QueryWrapper<Article>().eq("article_id",id));
        if(article!=null){
            articleMapper.deleteById(id);
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
        Article article = articleMapper.selectOne(new QueryWrapper<Article>().eq("article_id",id));
        if(article!=null){
            return ReturnT.SUCCESS(article);
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
        Page<Article> buildPage = new Page<Article>(page,limit);
        //条件构造器
        QueryWrapper<Article> queryWrapper = new QueryWrapper<Article>();
        if(StringUtils.isNotEmpty(searchParams)&&JSON.isValid(searchParams)) {
            Article article = JSON.parseObject(searchParams, Article.class);
            queryWrapper.eq(StringUtils.isNoneEmpty(article.getTitle()), "article_name", article.getTitle());
        }
        //执行分页
        IPage<Article> pageList = articleMapper.selectPage(buildPage, queryWrapper);
        //返回结果
        return ReturnT.PAGE(pageList.getRecords(),pageList.getTotal());
    }

    @GetMapping("/list")
    public ModelAndView listPage(){
        return new ModelAndView("cms/article-list");
    }

    @GetMapping("/edit")
    public ModelAndView editPage(int id){
        Article article = articleMapper.selectOne(new QueryWrapper<Article>().eq("article_id",id));
        return new ModelAndView("cms/article-edit","article",article);
    }
}



