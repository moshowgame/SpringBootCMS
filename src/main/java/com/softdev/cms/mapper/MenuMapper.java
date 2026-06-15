package com.softdev.cms.mapper;

import com.softdev.cms.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper {

    Menu selectById(@Param("menuId") Integer menuId);

    List<Menu> selectByParentMenuId(@Param("parentMenuId") Integer parentMenuId);

    List<Menu> selectByRoleId(@Param("roleId") String roleId);

    List<Menu> selectAll();

    int insert(Menu menu);

    int updateById(Menu menu);

    int deleteById(@Param("menuId") Integer menuId);
}
