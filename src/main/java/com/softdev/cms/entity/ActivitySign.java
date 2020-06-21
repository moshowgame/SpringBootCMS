package com.softdev.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description activity_sign
 * @author zhengkai.blog.csdn.net
 * @date 2020-02-29 19:42:09
 */
@Data
public class ActivitySign implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 签到id
     */
    @TableId(type = IdType.AUTO)
    private Integer signId;

    /**
     * 活动id
     */
    private Integer activityId;

    /**
     * 用户id
     */
    //private Integer userId;

    /**
     * 签到时间
     */
    private Date signTime;

    /**
     * 姓名
     */
    private String userName;
    /**
     * 姓名
     */
    private String showName;
    /**
     * 签到类型 1签到 2请假
     */
    private Integer signType;
    /**
     * 请假理由
     */
    private String leaveReason;
    private String phone;
    private String company;

    private Integer userId;

    public ActivitySign() {
    }

}
