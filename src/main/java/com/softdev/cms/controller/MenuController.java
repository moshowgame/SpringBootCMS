package com.softdev.cms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softdev.cms.entity.Menu;
import com.softdev.cms.mapper.MenuMapper;
import com.softdev.cms.util.Result;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuMapper menuMapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/save")
    public Result<String> save(@RequestBody Menu menu) {
        try {
            log.info("menu:{}", objectMapper.writeValueAsString(menu));
        } catch (Exception e) {
            log.warn("serialize menu failed", e);
        }
        Menu oldMenu = menuMapper.selectById(menu.getMenuId());
        if (oldMenu != null) {
            menuMapper.updateById(menu);
        } else {
            List<Menu> all = menuMapper.selectAll();
            boolean titleDuplicate = all.stream()
                    .anyMatch(m -> m.getTitle().equals(menu.getTitle()));
            if (titleDuplicate) {
                return Result.fail("保存失败，名字重复");
            }
            menuMapper.insert(menu);
        }
        return Result.success("保存成功");
    }

    @PostMapping("/delete")
    public Result<String> delete(@RequestParam Integer id) {
        Menu menu = menuMapper.selectById(id);
        if (menu != null) {
            menuMapper.deleteById(id);
            return Result.success("删除成功");
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/find")
    public Result<Menu> find(@RequestParam Integer id) {
        Menu menu = menuMapper.selectById(id);
        if (menu != null) {
            return Result.success(menu);
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/list")
    public Result<List<Menu>> list(@RequestParam(required = false) String searchParams,
                                    @RequestParam(required = false, defaultValue = "1") int page,
                                    @RequestParam(required = false, defaultValue = "10") int limit) {
        // Menu使用selectAll + 内存过滤（菜单数量通常不多）
        List<Menu> allMenus = menuMapper.selectAll();
        if (StringUtils.isNotEmpty(searchParams)) {
            Menu query;
            try {
                query = objectMapper.readValue(searchParams, Menu.class);
            } catch (Exception e) {
                log.warn("parse searchParams failed", e);
                return Result.success(allMenus, allMenus.size());
            }
            if (query.getMenuId() != null) {
                allMenus = allMenus.stream()
                        .filter(m -> query.getMenuId().equals(m.getMenuId()))
                        .collect(Collectors.toList());
            }
            if (query.getParentMenuId() != null) {
                allMenus = allMenus.stream()
                        .filter(m -> query.getParentMenuId().equals(m.getParentMenuId()))
                        .collect(Collectors.toList());
            }
        }
        int total = allMenus.size();
        int fromIndex = Math.min((page - 1) * limit, total);
        int toIndex = Math.min(fromIndex + limit, total);
        return Result.success(allMenus.subList(fromIndex, toIndex), total);
    }

    @GetMapping("/list")
    public ModelAndView listPage() {
        return new ModelAndView("cms/menu-list");
    }

    @GetMapping("/edit")
    public ModelAndView editPage(@RequestParam Integer id) {
        Menu menu = menuMapper.selectById(id);
        return new ModelAndView("cms/menu-edit", "menu", menu);
    }

    @GetMapping("/init")
    public Map<String, Object> initMenu(HttpSession session) {
        Integer roleId = (Integer) session.getAttribute("roleId");
        if (roleId == null) {
            return Collections.emptyMap();
        }
        List<Menu> pageList = menuMapper.selectByRoleId(String.valueOf(roleId));

        Map<String, Object> parentMap = new HashMap<>();

        Map<String, String> clearInfo = new HashMap<>();
        clearInfo.put("clearUrl", "/static/api/clear.json");
        parentMap.put("clearInfo", clearInfo);

        Map<String, String> homeInfo = new HashMap<>();
        homeInfo.put("title", "首页");
        homeInfo.put("icon", "fa fa-home");
        homeInfo.put("href", "/cms/admin/welcome");
        parentMap.put("homeInfo", homeInfo);

        Map<String, String> logoInfo = new HashMap<>();
        logoInfo.put("title", "CMS");
        logoInfo.put("image", "/cms/static/images/logo.png");
        logoInfo.put("href", "#");
        parentMap.put("logoInfo", logoInfo);

        List<Menu> parentMenuList = pageList.stream()
                .filter(m -> m.getParentMenuId() == 0)
                .collect(Collectors.toList());
        for (Menu menu : parentMenuList) {
            menu.setHref("");
            menu.setChild(pageList.stream()
                    .filter(m -> menu.getMenuId().equals(m.getParentMenuId()))
                    .collect(Collectors.toList()));
        }

        Map<String, Object> menuInfo = new HashMap<>();
        menuInfo.put("title", "SpringBootCMS");
        menuInfo.put("href", "");
        menuInfo.put("icon", "fa fa-home");
        menuInfo.put("target", "_self");
        menuInfo.put("child", parentMenuList);
        parentMap.put("menuInfo", Arrays.asList(menuInfo));

        return parentMap;
    }
}
