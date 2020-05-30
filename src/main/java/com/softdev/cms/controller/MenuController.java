package com.softdev.cms.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.softdev.cms.entity.Menu;
import com.softdev.cms.mapper.MenuMapper;
import com.softdev.cms.util.JwtTokenUtil;
import com.softdev.cms.util.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description menu
 * @author zhengkai.blog.csdn.net
 * @date 2020-02-19 20:42:16
 */
@Slf4j
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    /**
     * 新增或编辑
     */
    @PostMapping("/save")
    public Object save(@RequestBody Menu menu){
        log.info("menu:"+JSON.toJSONString(menu));
        Menu oldMenu = menuMapper.selectOne(new QueryWrapper<Menu>().eq("menu_id",menu.getMenuId()));
        if(oldMenu!=null){
            menuMapper.updateById(menu);
        }else{
            if(menuMapper.selectOne(new QueryWrapper<Menu>().eq("title",menu.getTitle()))!=null){
                return ReturnT.ERROR("保存失败，名字重复");
            }
            menuMapper.insert(menu);
        }
        return ReturnT.SUCCESS();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public Object delete(int id){
        Menu menu = menuMapper.selectOne(new QueryWrapper<Menu>().eq("menu_id",id));
        if(menu!=null){
            menuMapper.deleteById(id);
            return ReturnT.SUCCESS("删除成功");
        }else{
            return ReturnT.ERROR();
        }
    }

    /**
     * 查询
     */
    @PostMapping("/find")
    public Object find(int id){
        Menu menu = menuMapper.selectOne(new QueryWrapper<Menu>().eq("menu_id",id));
        if(menu!=null){
            return ReturnT.SUCCESS(menu);
        }else{
            return ReturnT.ERROR();
        }
    }

    /**
     * 分页查询
     */
    @PostMapping("/list")
    public Object list(String searchParams,
                       @RequestParam(required = false, defaultValue = "0") int page,
                       @RequestParam(required = false, defaultValue = "10") int limit) {
        log.info("page:"+page+"-limit:"+limit+"-json:"+ JSON.toJSONString(searchParams));
        //分页构造器
        Page<Menu> buildPage = new Page<Menu>(page,limit);
        //条件构造器
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<Menu>();
        if(StringUtils.isNotEmpty(searchParams)) {
            Menu menu = JSON.parseObject(searchParams, Menu.class);
            queryWrapper.eq(menu.getMenuId()!=null, "menu_id", menu.getMenuId());
            queryWrapper.eq(menu.getParentMenuId()!=null, "parent_menu_id", menu.getParentMenuId());
        }
        //执行分页
        IPage<Menu> pageList = menuMapper.selectPage(buildPage, queryWrapper);
        //返回结果
        return ReturnT.PAGE(pageList.getRecords(),pageList.getTotal());
    }
    @GetMapping("/list")
    public ModelAndView listPage(){
        return new ModelAndView("cms/menu-list");
    }
    @GetMapping("/edit")
    public ModelAndView editPage(int id){
        Menu menu = menuMapper.selectOne(new QueryWrapper<Menu>().eq("menu_id",id));
        return new ModelAndView("cms/menu-edit","menu",menu);
    }

    @GetMapping("/init")
    public Object initMenu(String token){
        //获取当前用户权限
        //Integer roleId = Integer.parseInt(session.getAttribute("roleId")+"");
        //jwt
        Integer roleId = jwtTokenUtil.getRoleIdFromToken(token);
        //根据角色查询可用菜单
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<Menu>().like("role_id",roleId);
        List<Menu> pageList = menuMapper.selectList(queryWrapper);
        //jwt
        pageList.forEach(m->m.setHref(m.getHref()+"?token="+token));
        //最终返回的jsonMap
        Map<String,Object> parentMap = new HashMap<>();
        //clear信息
        Map<String,String> clearInfo = new HashMap<>();
        clearInfo.put("clearUrl","/static/api/clear.json");
        parentMap.put("clearInfo",clearInfo);
        //home首页信息
        Map<String,String> homeInfo = new HashMap<>();
        homeInfo.put("title","首页");
        homeInfo.put("icon","fa fa-home");
        homeInfo.put("href","/cms/welcome?token="+token);
        parentMap.put("homeInfo",homeInfo);
        //info系统左边logo文字信息
        Map<String,String> logoInfo = new HashMap<>();
        logoInfo.put("title","CMS");
        logoInfo.put("image","/cms/static/images/logo.png");
        logoInfo.put("href","#");
        parentMap.put("logoInfo",logoInfo);
        //通过jdk8的lambda过滤父选项list
        List<Menu> parentMenuList = pageList.stream().filter(m -> m.getParentMenuId()==0).collect(Collectors.toList());
        //遍历父选项，再从所有菜单中找到自己的子选项（仅支持二级菜单）
        for(Menu menu:parentMenuList){
            menu.setHref("");
            menu.setChild(pageList.stream().filter(m -> menu.getMenuId().equals(m.getParentMenuId())).collect(Collectors.toList()));
        }
        //currency系统上方信息
        Map<String,Object> menuInfo = new HashMap<>();
        //Map<String,Object> currency = new HashMap<>();
        menuInfo.put("title","SpringBootCMS");
        menuInfo.put("href","");
        menuInfo.put("icon","fa fa-home");
        menuInfo.put("target","_self");
        menuInfo.put("child",parentMenuList);
        //加载封装的菜单信息
        parentMap.put("menuInfo", Arrays.asList(menuInfo));
        return parentMap;
    }
}



