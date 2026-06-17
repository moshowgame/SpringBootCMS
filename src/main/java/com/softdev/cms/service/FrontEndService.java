package com.softdev.cms.service;

import com.softdev.cms.entity.Article;
import com.softdev.cms.entity.Channel;
import com.softdev.cms.entity.Template;
import com.softdev.cms.entity.dto.QueryParamDTO;
import com.softdev.cms.mapper.ArticleMapper;
import com.softdev.cms.mapper.ChannelMapper;
import com.softdev.cms.mapper.TemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class FrontEndService {

    @Autowired
    TemplateMapper templateMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    ChannelMapper channelMapper;

    @Cacheable(value = "cache-page", key = "#pageName")
    public Map<String, Object> getTemplateMap(String pageName) {
        List<Template> baseList = templateMapper.selectByPage("base");
        List<Template> templateList = templateMapper.selectByPage(pageName);
        Map<String, Object> templateMap = new LinkedHashMap<>(baseList.size() + templateList.size() + 1);
        baseList.forEach(m -> templateMap.put(m.getTemplateName(), m.getTemplateValue()));
        templateList.forEach(m -> templateMap.put(m.getTemplateName(), m.getTemplateValue()));
        Map<String, Object> defaults = getDefaultTemplateMap(pageName);
        for (Map.Entry<String, Object> entry : defaults.entrySet()) {
            templateMap.putIfAbsent(entry.getKey(), entry.getValue());
        }
        return templateMap;
    }

    private Map<String, Object> getDefaultTemplateMap(String pageName) {
        Map<String, Object> defaults = new LinkedHashMap<>();
        defaults.put("companyName", "SpringBootCMS 内容管理系统");
        defaults.put("location", "中国 · 上海");
        defaults.put("phone", "400-888-8888");
        defaults.put("email", "info@springbootcms.com");
        defaults.put("readMore", "了解更多");
        if ("index".equals(pageName)) {
            defaults.put("bannerTitle", "专业的内容管理解决方案");
            defaults.put("bannerDescription", "基于 Spring Boot + MyBatis 的现代化 CMS 系统，让您的网站建设更高效");
            defaults.put("bannerButton", "立即体验");
            defaults.put("bannerUrl", "#");
            defaults.put("serviceTitle", "核心功能");
            defaults.put("serviceDescription", "提供完整的文章管理、栏目管理、活动管理、表单管理等功能");
            defaults.put("service1title", "文章管理");
            defaults.put("service1description", "富文本编辑、文章分类、标签管理、SEO优化");
            defaults.put("service1url", "#");
            defaults.put("service1icon", "bi-journal-text");
            defaults.put("service2title", "栏目管理");
            defaults.put("service2description", "多级栏目、权限控制、模板配置、灵活排序");
            defaults.put("service2url", "#");
            defaults.put("service2icon", "bi-diagram-3");
            defaults.put("service3title", "活动与表单");
            defaults.put("service3description", "活动发布、在线签到、自定义表单、数据收集");
            defaults.put("service3url", "#");
            defaults.put("service3icon", "bi-calendar-check");
            defaults.put("solutionTitle", "一站式内容管理平台");
            defaults.put("solutionDescription", "从文章发布到数据收集，从模板配置到用户管理，满足您的所有需求");
            defaults.put("solutionUrl", "#");
            defaults.put("quoteTitle", "开始构建您的专属网站");
            defaults.put("quoteDescription", "专业的技术架构，灵活的扩展能力，助力您的业务发展");
            defaults.put("quoteUrl", "#");
            defaults.put("consultingTitle", "专业的技术咨询服务");
            defaults.put("consultingDescription", "我们提供完整的技术支持和定制开发服务，帮助您快速实现业务目标");
            defaults.put("consultingUrl", "#");
        } else if ("channel".equals(pageName) || "article".equals(pageName) || "search".equals(pageName)) {
            defaults.put("bannerTitle", "文章中心");
            defaults.put("bannerDescription", "浏览最新的文章和资讯");
        }
        return defaults;
    }

    @CacheEvict(value = "cache-page", key = "#pageName")
    public void refreshTemplateMap(String pageName) {
        // Cache eviction only
    }

    @Cacheable(value = "cache-article", key = "#articleId")
    public Article getArticle(String articleId) {
        try {
            Integer id = Integer.valueOf(articleId);
            return articleMapper.selectById(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 文章详情页上下文：上一页 / 下一页 / 相关文章，用于详情页企业级增强。
     * key 带上 articleId 以保证缓存正确。
     */
    @Cacheable(value = "cache-article", key = "'ctx-' + #articleId")
    public Map<String, Object> getArticleContext(String articleId) {
        Map<String, Object> ctx = new LinkedHashMap<>();
        try {
            Integer id = Integer.valueOf(articleId);
            Article current = articleMapper.selectById(id);
            ctx.put("prev", articleMapper.selectPrev(id));
            ctx.put("next", articleMapper.selectNext(id));
            Integer channelId = (current != null && current.getChannelId() != null) ? current.getChannelId() : id;
            ctx.put("related", articleMapper.selectRelated(channelId, id, 4));
        } catch (NumberFormatException e) {
            ctx.put("prev", null);
            ctx.put("next", null);
            ctx.put("related", java.util.Collections.emptyList());
        }
        return ctx;
    }

    @Cacheable(value = "cache-channel", key = "'list-' + #status")
    public List<Channel> getChannelList(Integer status) {
        List<Channel> all = channelMapper.selectAll();
        return all.stream()
                .filter(c -> status.equals(c.getStatus()))
                .sorted((a, b) -> {
                    if (a.getSeq() == null) return 1;
                    if (b.getSeq() == null) return -1;
                    return a.getSeq().compareTo(b.getSeq());
                })
                .toList();
    }

    @Cacheable(value = "cache-channel", key = "'id-' + #channelId")
    public Channel getChannelById(Integer channelId) {
        return channelMapper.selectById(channelId);
    }

    /**
     * 首页最新已发布文章（默认取 6 篇），供首页动态区块展示。
     */
    @Cacheable(value = "cache-article", key = "'latest-' + #limit")
    public List<Article> getLatestArticles(Integer limit) {
        QueryParamDTO queryParamDTO = new QueryParamDTO();
        queryParamDTO.setStatus(1);
        queryParamDTO.setPage(1);
        queryParamDTO.setLimit(limit == null ? 6 : limit);
        queryParamDTO.setPageLimit(1, queryParamDTO.getLimit());
        return articleMapper.pageAll(queryParamDTO);
    }
}
