package com.softdev.cms.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * @description menu
 * @author zhengkai.blog.csdn.net
 * @date 2020-02-19 20:38:47
 */
@Data
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单id
     */
    @TableId(type = IdType.AUTO)
    private Integer menuId;

    /**
     * 地址
     */
    private String href;

    /**
     * 图标
     */
    private String icon;

    /**
     * 目标
     */
    private String target;

    /**
     * 名字
     */
    private String title;

    private String roleId;
    /**
     * 父菜单id
     */
    private Integer parentMenuId;

    @TableField(exist = false)
    private List<Menu> child;

    public Menu() {
    }

}
