package com.softdev.cms.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Media implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer mediaId;
    private String fileName;
    private String filePath;
    private Long fileSize;
    private String fileType;
    private String fileExt;
    private String mediaType;
    private Integer width;
    private Integer height;
    private String title;
    private String altText;
    private Integer uploadUserId;
    private String uploadUserName;
    private Integer deleted;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public Media() {
    }
}
