package com.softdev.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description form
 * @author zhengkai.blog.csdn.net
 * @date 2020-03-06 23:00:35
 */
@Data
public class Form implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * form_id
     */
    @TableId(type = IdType.AUTO)
    private Integer formId;

    /**
     * form_name
     */
    private String formName;

    /**
     * form_type
     */
    private Integer formType;

    /**
     * create_user_id
     */
    private Integer createUserId;

    /**
     * create_user_name
     */
    private String createUserName;

    /**
     * create_time
     */
    private Date createTime;

    /**
     * modify_time
     */
    private Date updateTime;
    /**
     * 描述
     */
    private String formDesc;

    private Integer status;
    /**
     * 附件
     */
    private String attachment;
    public Form() {
    }

}
