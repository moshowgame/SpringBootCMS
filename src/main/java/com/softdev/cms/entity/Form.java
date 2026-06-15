package com.softdev.cms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Form implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer formId;
    private String formName;
    private Integer formType;
    private Integer createUserId;
    private String createUserName;
    private String formDesc;
    private String attachment;
    private Integer status;
    private Integer deleted;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public Form() {
    }
}
