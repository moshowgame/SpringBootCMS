package com.softdev.cms.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * @description user
 * @author zhengkai.blog.csdn.net
 * @date 2020-02-15 22:06:34
 */
@Data
public class User implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(type= IdType.AUTO)
    private Integer userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 显示名称
     */
    private String showName;

    /**
     * 密码
     */
    private String password;

    /**
     * 是否启用：1启用 0停用
     */
    private Integer status;

    /**
     * 权限id
     */
    private Integer roleId;

    private String openId;

    private String phone;

    private String email;

    private String companyName;

    private String companyAddress;

    public User() {
    }

}
