package com.softdev.cms.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer menuId;
    private String href;
    private String icon;
    private String target;
    private String title;
    private String roleId;
    private Integer parentMenuId;
    private Integer sort;
    private Integer deleted;

    private List<Menu> child;

    public Menu() {
    }
}
