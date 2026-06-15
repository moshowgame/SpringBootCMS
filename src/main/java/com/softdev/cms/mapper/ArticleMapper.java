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

    Article selectBySlug(@Param("slug") String slug);

    int incrementViewCount(@Param("articleId") Integer articleId);

    List<Article> pageAll(QueryParamDTO queryParamDTO);

    int countAll(QueryParamDTO queryParamDTO);

    int insert(Article article);

    int updateById(Article article);

    int deleteById(@Param("articleId") Integer articleId);
}
