package com.softdev.cms.service;

import com.softdev.cms.entity.Article;
import com.softdev.cms.entity.Channel;
import com.softdev.cms.entity.Template;
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
        return templateMap;
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

    @Cacheable(value = "cache-channel", key = "#status")
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

    @Cacheable(value = "cache-channel", key = "#channelId")
    public Channel getChannelById(Integer channelId) {
        return channelMapper.selectById(channelId);
    }
}
