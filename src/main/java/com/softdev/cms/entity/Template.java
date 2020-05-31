package com.softdev.cms.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * @description template
 * @author zhengkai.blog.csdn.net
 * @date 2020-05-31
 */
@Data
public class Template implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 模板id
     */
    @TableId(type = IdType.AUTO)
    private Integer templateId;

    /**
     * div id
     */
    private String templateName;

    /**
     * 值
     */
    private String templateValue;

    /**
     * 描述
     */
    private String description;

    /**
     * 页面
     */
    private String page;

    public Template() {
    }

}
