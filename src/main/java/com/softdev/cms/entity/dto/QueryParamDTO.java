package com.softdev.cms.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryParamDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    Integer userId;
    Integer createUserId;
    String userName;
    String showName;
    Integer articleId;
    Integer parentChannelId;
    String title;
    Integer formId;
    Integer submitId;
    Integer channelId;
    Integer classesId;
    Integer status;
    Integer page;
    Integer limit;
    /**
     *设置分页参数for手工分页
     */
    public void setPageLimit(Integer page,Integer limit) {
        this.page = (page-1)*limit;
        this.limit = limit;
    }
    /**
     *设置分页参数for手工分页
     */
    public void setPageLimit() {
        this.page = (this.page-1)*this.limit;
    }
}
