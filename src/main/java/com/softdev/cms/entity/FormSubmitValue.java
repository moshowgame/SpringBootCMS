package com.softdev.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @description form_submit_value
 * @author zhengkai.blog.csdn.net
 * @date 2020-03-06 23:02:07
 */
@Data
public class FormSubmitValue implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * value_id
     */
    @TableId(type = IdType.AUTO)
    private Integer valueId;

    /**
     * item_id
     */
    private Integer itemId;

    /**
     * form_id
     */
    private Integer formId;

    /**
     * value_text
     */
    private String valueText;

    /**
     * user_id
     */
    private Integer userId;

    /**
     * submit_id
     */
    private Integer submitId;

    public FormSubmitValue() {
    }

    public FormSubmitValue(Integer formId, Integer submitId,Integer itemId, String valueText, Integer userId) {
        this.itemId = itemId;
        this.formId = formId;
        this.valueText = valueText;
        this.userId = userId;
        this.submitId = submitId;
    }
    public FormSubmitValue(Integer formId, Integer submitId,Integer itemId, String valueText) {
        this.itemId = itemId;
        this.formId = formId;
        this.valueText = valueText;
        this.submitId = submitId;
    }
}
