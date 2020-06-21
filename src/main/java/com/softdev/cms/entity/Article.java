package com.softdev.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 文章
 * @author zhengkai.blog.csdn.net
 * @date 2020-05-25 20:30:32
 */
@Data
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 文章id
     */
    private Integer articleId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 创建者id
     */
    private Integer createUserId;

    /**
     * 创建者名称
     */
    private String createUserName;

    /**
     * 内容
     */
    private String content;
    /**
     * 描述
     */
    private String description;

    /**
     * 频道id
     */
    private Integer channelId;

    /**
     * 关键字，逗号分隔
     */
    private String keyword;

    /**
     * 创建时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 是否置顶
     */
    private Integer isTop;
    /**
     * 更新时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Integer status;

    @TableField(exist = false)
    private String channelName;

    public Article() {
    }

}
