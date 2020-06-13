package com.softdev.cms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.softdev.cms.entity.Article;
import com.softdev.cms.entity.Channel;
import com.softdev.cms.entity.Template;
import com.softdev.cms.mapper.ArticleMapper;
import com.softdev.cms.mapper.ChannelMapper;
import com.softdev.cms.mapper.TemplateMapper;
import com.softdev.cms.util.EhCacheUtil;
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

    /**
     * 获取模板map（缓存）
     * @param pageName
     * @return
     */
    @Cacheable(value = EhCacheUtil.CACHE_NAME_PAGE, key = EhCacheUtil.CACHE_KEY_PAGE)
    public Map getTemplateMap(String pageName){
        //获取基础信息
        List<Template> baseList = templateMapper.selectList(new QueryWrapper<Template>().eq("page","base"));
        //获取当前页面信息
        List<Template> templateList = templateMapper.selectList(new QueryWrapper<Template>().eq("page",pageName));
        Map<String,Object> templateMap = new LinkedHashMap<>(baseList.size()+templateList.size()+1);
        //封装成templateMap
        baseList.stream().forEach(m->{templateMap.put(m.getTemplateName(),m.getTemplateValue());});
        templateList.stream().forEach(m->{templateMap.put(m.getTemplateName(),m.getTemplateValue());});
        return templateMap;
    }
    @CacheEvict(value = EhCacheUtil.CACHE_NAME_PAGE, key = EhCacheUtil.CACHE_KEY_PAGE)
    public void refreshTemplateMap(String pageName){

    }
    /**
     * 获取文章（缓存）
     * @return
     */
    @Cacheable(value = EhCacheUtil.CACHE_NAME_ARTICLE, key = EhCacheUtil.CACHE_KEY_ARTICLE)
    public Article getArticle(String articleId){
        return articleMapper.selectById(articleId);
    }
    /**
     * 获取栏目（缓存）
     * @return
     */
    @Cacheable(value = EhCacheUtil.CACHE_NAME_CHANNEL, key = EhCacheUtil.CACHE_KEY_CHANNEL)
    public List<Channel> getChannelList(Integer status){
        return channelMapper.selectList(new QueryWrapper<Channel>().eq("status",status).orderByAsc("seq"));
    }
    /**
     * 获取栏目（缓存）
     * @return
     */
    @Cacheable(value = EhCacheUtil.CACHE_NAME_CHANNEL, key = EhCacheUtil.CACHE_KEY_CHANNEL_ID)
    public Channel getChannelById(Integer channelId){
        return channelMapper.selectById(channelId);
    }
}
