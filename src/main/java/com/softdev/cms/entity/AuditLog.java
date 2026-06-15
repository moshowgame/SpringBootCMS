package com.softdev.cms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AuditLog implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long logId;
    private Integer userId;
    private String userName;
    private String action;
    private String module;
    private String targetId;
    private String targetName;
    private String detail;
    private String ip;
    private String userAgent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public AuditLog() {
    }

    public AuditLog(Integer userId, String userName, String action, String module, String targetId, String targetName, String ip) {
        this.userId = userId;
        this.userName = userName;
        this.action = action;
        this.module = module;
        this.targetId = targetId;
        this.targetName = targetName;
        this.ip = ip;
    }
}
