package com.softdev.cms.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer channelId;
    private String channelName;
    private String slug;
    private Integer parentChannelId;
    private String icon;
    private String coverImage;
    private String description;
    private Integer channelType;
    private Integer status;
    private Integer seq;
    private Integer deleted;

    public Channel() {
    }
}
