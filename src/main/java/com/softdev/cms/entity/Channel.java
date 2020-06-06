package com.softdev.cms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @description 频道
 * @author zhengkai.blog.csdn.net
 * @date 2020-05-25
 */
@Data
public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 频道id
     */
    private Integer channelId;

    /**
     * 频道名称
     */
    private String channelName;

    /**
     * 父类id
     */
    private Integer parentChannelId;

    /**
     * 频道图标
     */
    private String icon;

    private Integer status;

    private Integer seq;

    public Channel() {
    }

}
