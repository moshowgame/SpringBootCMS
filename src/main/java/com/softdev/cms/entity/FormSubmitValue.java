package com.softdev.cms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FormSubmitValue implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer valueId;
    private Integer itemId;
    private Integer formId;
    private String valueText;
    private Integer userId;
    private Integer submitId;
    private Integer deleted;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public FormSubmitValue() {
    }

    public FormSubmitValue(Integer formId, Integer submitId, Integer itemId, String valueText, Integer userId) {
        this.itemId = itemId;
        this.formId = formId;
        this.valueText = valueText;
        this.userId = userId;
        this.submitId = submitId;
    }

    public FormSubmitValue(Integer formId, Integer submitId, Integer itemId, String valueText) {
        this.itemId = itemId;
        this.formId = formId;
        this.valueText = valueText;
        this.submitId = submitId;
    }
}
