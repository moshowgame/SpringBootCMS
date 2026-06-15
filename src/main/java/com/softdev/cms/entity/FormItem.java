package com.softdev.cms.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class FormItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer itemId;
    private Integer formId;
    private String itemName;
    private String itemType;
    private String defaultValue;
    private String itemTip;
    private String placeholder;
    private Integer required;
    private Integer sort;
    private Integer deleted;

    public FormItem() {
    }

    public FormItem(Integer formId, String itemName, String itemType, String defaultValue) {
        this.formId = formId;
        this.itemName = itemName;
        this.itemType = itemType;
        this.defaultValue = defaultValue;
    }
}
