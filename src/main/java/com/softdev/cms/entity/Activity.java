package com.softdev.cms.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * @description activity
 * @author zhengkai.blog.csdn.net
 * @date 2020-02-26 23:27:13
 */
@Data
public class Activity implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 活动id
     */
    @TableId(type = IdType.AUTO)
    private Integer activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动简介
     */
    private String activityDesc;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 活动开始时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 活动结束时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 创建用户id
     */
    private Integer createUserId;

    /**
     * 创建用户姓名
     */
    private String createUserName;

    private Integer status;

    public Activity() {
    }

}
