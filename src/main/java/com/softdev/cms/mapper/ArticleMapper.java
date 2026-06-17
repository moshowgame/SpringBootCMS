package com.softdev.cms.mapper;

import com.softdev.cms.entity.Article;
import com.softdev.cms.entity.dto.QueryParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper {

    Article selectById(@Param("articleId") Integer articleId);

    List<Article> selectByChannelId(@Param("channelId") Integer channelId);

    /**
     * 同频道已发布的相关文章（排除当前文章），用于详情页"相关推荐"。
     */
    List<Article> selectRelated(@Param("channelId") Integer channelId,
                                @Param("articleId") Integer articleId,
                                @Param("limit") Integer limit);

    /**
     * 上一篇（article_id 更小的相邻已发布文章）
     */
    Article selectPrev(@Param("articleId") Integer articleId);

    /**
     * 下一篇（article_id 更大的相邻已发布文章）
     */
    Article selectNext(@Param("articleId") Integer articleId);

    Article selectBySlug(@Param("slug") String slug);

    int incrementViewCount(@Param("articleId") Integer articleId);

    /**
     * 已发布文章的总浏览量（仪表盘统计）
     */
    Long sumViewCount();

    List<Article> pageAll(QueryParamDTO queryParamDTO);

    int countAll(QueryParamDTO queryParamDTO);

    int insert(Article article);

    int updateById(Article article);

    int deleteById(@Param("articleId") Integer articleId);
}
