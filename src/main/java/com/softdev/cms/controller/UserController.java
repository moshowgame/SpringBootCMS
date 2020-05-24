package com.softdev.cms.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.softdev.cms.entity.User;
import com.softdev.cms.mapper.UserMapper;
import com.softdev.cms.util.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @description user
 * @author zhengkai.blog.csdn.net
 * @date 2020-02-15 22:06:34
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    /**
     * 新增或编辑
     */
    @PostMapping("/save")
    public Object save(@RequestBody User user){
        log.info("user:"+JSON.toJSONString(user));
        User oldUser = userMapper.selectOne(new QueryWrapper<User>().eq("user_id",user.getUserId()));
        user.setUpdateTime(new Date());
        if(oldUser!=null){
            userMapper.updateById(user);
        }else{
            if(userMapper.selectOne(new QueryWrapper<User>().eq("user_Name",user.getUserName()))!=null){
                return ReturnT.ERROR("保存失败，用户名重复");
            }
            user.setCreateTime(new Date());
            userMapper.insert(user);
        }
        return ReturnT.SUCCESS();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public Object delete(int id){
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("user_id",id));
        if(user!=null){
            userMapper.deleteById(id);
            return ReturnT.SUCCESS();
        }else{
            return ReturnT.ERROR();
        }
    }

    /**
     * 查询
     */
    @PostMapping("/find")
    public Object find(int id){
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("user_id",id));
        if(user!=null){
            return ReturnT.SUCCESS(user);
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
        Page<User> buildPage = new Page<User>(page,limit);
        //条件构造器
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        if(StringUtils.isNotEmpty(searchParams)) {
            User user = JSON.parseObject(searchParams, User.class);
            queryWrapper.eq(StringUtils.isNotEmpty(user.getUserName()), "user_name", user.getUserName());
            queryWrapper.like(StringUtils.isNotEmpty(user.getShowName()), "show_name", user.getShowName());
            queryWrapper.like(StringUtils.isNotEmpty(user.getCompanyName()), "company_name", user.getCompanyName());
            queryWrapper.eq(user.getRoleId()!=null,"role_Id",user.getRoleId());
        }
        //执行分页
        IPage<User> pageList = userMapper.selectPage(buildPage, queryWrapper);
        List<User> itemList = pageList.getRecords();
        //返回结果
        return ReturnT.PAGE(itemList,pageList.getTotal());
    }
    /*@PostMapping("/list2")
    public Object list2(String searchParams,
                       @RequestParam(required = false, defaultValue = "0") int page,
                       @RequestParam(required = false, defaultValue = "10") int limit) {
        log.info("page:"+page+"-limit:"+limit+"-json:"+ JSON.toJSONString(searchParams));
        QueryParamDTO queryParamDTO = JSON.parseObject(searchParams, QueryParamDTO.class);
        queryParamDTO.setPageLimit(page,limit);
        //分页构造器
        List<User> itemList=userMapper.pageAll(queryParamDTO);
        int itemTotal=userMapper.countAll(queryParamDTO);
        //返回结果
        return ReturnT.PAGE(itemList,itemTotal);
    }*/
    @GetMapping("/list")
    public ModelAndView listPage(){
        return new ModelAndView("cms/user-list");
    }
    @GetMapping("/edit")
    public ModelAndView editPage(int id){
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("user_id",id));
        return new ModelAndView("cms/user-edit","user",user);
    }
    @GetMapping("/info")
    public ModelAndView infoPage(HttpServletRequest request){
        Object loginUserId = request.getSession().getAttribute("loginUserId");
        User user = null;
        if(loginUserId!=null && loginUserId instanceof Integer){
            user = userMapper.selectOne(new QueryWrapper<User>().eq("user_id",(Integer)loginUserId));
        }
        return new ModelAndView("cms/user-info","user",user);
    }
}



