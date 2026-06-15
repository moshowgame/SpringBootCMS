package com.softdev.cms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softdev.cms.entity.Article;
import com.softdev.cms.entity.dto.QueryParamDTO;
import com.softdev.cms.mapper.ArticleMapper;
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
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleMapper articleMapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/save")
    @CacheEvict(value = "cache-article", allEntries = true)
    public Result<String> save(@RequestBody Article article) {
        try {
            log.info("article:{}", objectMapper.writeValueAsString(article));
        } catch (Exception e) {
            log.warn("serialize article failed", e);
        }
        Article oldArticle = articleMapper.selectById(article.getArticleId());
        article.setUpdateTime(new Date());
        if (oldArticle != null) {
            articleMapper.updateById(article);
        } else {
            if (articleMapper.selectById(article.getArticleId()) != null && article.getArticleId() != null) {
                return Result.fail("保存失败，标题重复");
            }
            article.setCreateTime(new Date());
            articleMapper.insert(article);
        }
        return Result.success("保存成功");
    }

    @PostMapping("/delete")
    @CacheEvict(value = "cache-article", allEntries = true)
    public Result<String> delete(@RequestParam Integer id) {
        Article article = articleMapper.selectById(id);
        if (article != null) {
            articleMapper.deleteById(id);
            return Result.success("删除成功");
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/find")
    @Cacheable(value = "cache-article", key = "#id")
    public Result<Article> find(@RequestParam Integer id) {
        Article article = articleMapper.selectById(id);
        if (article != null) {
            // 增加浏览量
            articleMapper.incrementViewCount(id);
            return Result.success(article);
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/list")
    public Result<List<Article>> list(@RequestParam(required = false) String searchParams,
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
        List<Article> itemList = articleMapper.pageAll(queryParamDTO);
        int itemTotal = articleMapper.countAll(queryParamDTO);
        return Result.success(itemList, itemTotal);
    }

    @GetMapping("/list")
    public ModelAndView listPage() {
        return new ModelAndView("cms/article-list");
    }

    @GetMapping("/edit")
    public ModelAndView editPage(@RequestParam Integer id) {
        Article article = articleMapper.selectById(id);
        return new ModelAndView("cms/article-edit", "article", article);
    }

    @PostMapping("/publish")
    @CacheEvict(value = "cache-article", allEntries = true)
    public Result<String> publish(@RequestParam Integer id, @RequestParam Integer status) {
        Article article = articleMapper.selectById(id);
        if (article != null) {
            article.setUpdateTime(new Date());
            article.setStatus(status);
            articleMapper.updateById(article);
            return Result.success((status == 1) ? "已发布" : "已暂停");
        } else {
            return Result.fail("操作失败");
        }
    }
}
