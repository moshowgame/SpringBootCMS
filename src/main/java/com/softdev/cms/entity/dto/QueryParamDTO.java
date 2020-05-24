package com.softdev.cms.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryParamDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    String userName;
    String showName;
    Integer formId;
    Integer year;
    Integer departmentId;
    Integer majorId;
    Integer classesId;
    Integer page;
    Integer limit;
    Integer userId;
    Integer status;

    public void setPageLimit(Integer page,Integer limit) {
        this.page = (page-1)*limit;
        this.limit = limit;
    }
}
