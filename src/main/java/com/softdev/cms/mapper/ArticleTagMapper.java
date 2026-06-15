package com.softdev.cms.mapper;

import com.softdev.cms.entity.ArticleTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleTagMapper {

    List<ArticleTag> selectByArticleId(@Param("articleId") Integer articleId);

    List<ArticleTag> selectByTagId(@Param("tagId") Integer tagId);

    int insert(ArticleTag articleTag);

    int deleteByArticleId(@Param("articleId") Integer articleId);

    int deleteByTagId(@Param("tagId") Integer tagId);
}
