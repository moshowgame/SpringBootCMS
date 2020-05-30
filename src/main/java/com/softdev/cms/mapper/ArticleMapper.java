package com.softdev.cms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.softdev.cms.entity.dto.QueryParamDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.softdev.cms.entity.Article;

import java.util.List;

/**
 * @description 文章
 * @author zhengkai.blog.csdn.net
 * @date 2020-05-25 20:33:02
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    @Select(
            "<script>select t0.*,t1.channel_Name from article t0 " +
                    //add here if need left join
                    "left join channel t1 on t0.channel_id=t1.channel_id "+
                    "where 1=1" +
                    "<when test='articleId!=null and articleId!=&apos;&apos; '> and t0.article_id=#{articleId}</when> " +
                    "<when test='title!=null and title!=&apos;&apos; '> and t0.title=#{title}</when> " +
                    "<when test='createUserId!=null and createUserId!=&apos;&apos; '> and t0.create_user_id=#{createUserId}</when> " +
                    //"<when test='createUserName!=null and createUserName!=&apos;&apos; '> and t0.create_user_name=#{createUserName}</when> " +
                    //"<when test='content!=null and content!=&apos;&apos; '> and t0.content=#{content}</when> " +
                    "<when test='channelId!=null and channelId!=&apos;&apos; '> and t0.channel_id=#{channelId}</when> " +
                    //"<when test='keyword!=null and keyword!=&apos;&apos; '> and t0.keyword=#{keyword}</when> " +
                    //"<when test='createTime!=null and createTime!=&apos;&apos; '> and t0.create_time=#{createTime}</when> " +
                    //"<when test='updateTime!=null and updateTime!=&apos;&apos; '> and t0.update_time=#{updateTime}</when> " +
                    //add here if need page limit
                    " limit ${page},${limit} " +
                    " </script>")
    List<Article> pageAll(QueryParamDTO queryParamDTO);

    @Select("<script>select count(1) from article t0 " +
            //add here if need left join
            "where 1=1" +
            "<when test='articleId!=null and articleId!=&apos;&apos; '> and t0.article_id=#{articleId}</when> " +
            "<when test='title!=null and title!=&apos;&apos; '> and t0.title=#{title}</when> " +
            "<when test='createUserId!=null and createUserId!=&apos;&apos; '> and t0.create_user_id=#{createUserId}</when> " +
            //"<when test='createUserName!=null and createUserName!=&apos;&apos; '> and t0.create_user_name=#{createUserName}</when> " +
            //"<when test='content!=null and content!=&apos;&apos; '> and t0.content=#{content}</when> " +
            "<when test='channelId!=null and channelId!=&apos;&apos; '> and t0.channel_id=#{channelId}</when> " +
            //"<when test='keyword!=null and keyword!=&apos;&apos; '> and t0.keyword=#{keyword}</when> " +
            //"<when test='createTime!=null and createTime!=&apos;&apos; '> and t0.create_time=#{createTime}</when> " +
            //"<when test='updateTime!=null and updateTime!=&apos;&apos; '> and t0.update_time=#{updateTime}</when> " +
            " </script>")
    int countAll(QueryParamDTO queryParamDTO);

}
