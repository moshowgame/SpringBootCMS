package com.softdev.cms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class FormSubmit implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer submitId;
    private Integer formId;
    private Integer userId;
    private String userName;
    private String showName;
    private String phone;
    private String company;
    private Integer status;
    private Integer auditUserId;
    private String auditUserName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date auditTime;

    private Integer deleted;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    // 关联字段（非表字段）
    private String formName;

    public FormSubmit() {
    }

    public FormSubmit(Integer formId, Integer userId, String userName) {
        this.formId = formId;
        this.userId = userId;
        this.userName = userName;
    }

    public FormSubmit(Integer formId, String showName, String phone, String company) {
        this.formId = formId;
        this.phone = phone;
        this.company = company;
        this.showName = showName;
    }
}
