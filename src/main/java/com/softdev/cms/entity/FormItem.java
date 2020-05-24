package com.softdev.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @description form_item
 * @author zhengkai.blog.csdn.net
 * @date 2020-03-06 23:01:45
 */
@Data
public class FormItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * item_id
     */
    @TableId(type = IdType.AUTO)
    private Integer itemId;

    /**
     * form_id
     */
    private Integer formId;

    /**
     * item_name
     */
    private String itemName;

    /**
     * input，radio，checkbox，textarea
     */
    private String itemType;

    /**
     * default_value
     */
    private String defaultValue;

    /**
     * item_tip
     */
    private String itemTip;

    public FormItem(){

    }
    public FormItem(Integer formId, String itemName, String itemType, String defaultValue) {
        this.formId = formId;
        this.itemName = itemName;
        this.itemType = itemType;
        this.defaultValue = defaultValue;
    }
}
