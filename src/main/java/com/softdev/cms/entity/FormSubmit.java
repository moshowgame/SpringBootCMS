package com.softdev.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description form_submit
 * @author zhengkai.blog.csdn.net
 * @date 2020-03-06 23:01:14
 */
@Data
public class FormSubmit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * submit_id
     */
    @TableId(type = IdType.AUTO)
    private Integer submitId;

    /**
     * form_id
     */
    private Integer formId;

    /**
     * user_id
     */
    private Integer userId;

    private String userName;

    /**
     * create_time
     */
    private Date createTime;

    /**
     * modify_time
     */
    private Date updateTime;
    /**
     * 状态与审核相关
     */
    private Date auditTime;
    private Integer status;
    private String auditUserName;
    private Integer auditUserId;

    @TableField(exist = false)
    private String formName;
    public FormSubmit() {
    }

    public FormSubmit(Integer formId, Integer userId, String userName) {
        this.formId = formId;
        this.userId = userId;
        this.userName = userName;
        this.createTime = new Date();
        this.updateTime = new Date();
    }
}
