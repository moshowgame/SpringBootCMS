package com.softdev.cms.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Template implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer templateId;
    private String templateName;
    private String templateValue;
    private String description;
    private String page;
    private Integer deleted;

    public Template() {
    }
}
