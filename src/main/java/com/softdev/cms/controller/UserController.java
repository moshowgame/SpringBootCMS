package com.softdev.cms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softdev.cms.entity.User;
import com.softdev.cms.entity.dto.QueryParamDTO;
import com.softdev.cms.mapper.UserMapper;
import com.softdev.cms.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/save")
    public Result<String> save(@RequestBody User user) {
        try {
            log.info("user:{}", objectMapper.writeValueAsString(user));
        } catch (Exception e) {
            log.warn("serialize user failed", e);
        }
        User oldUser = userMapper.selectById(user.getUserId());
        if (oldUser != null) {
            // 如果密码不为空，则加密
            if (StringUtils.isNotEmpty(user.getPassword()) && !user.getPassword().startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userMapper.updateById(user);
        } else {
            // 检查用户名重复
            User existing = userMapper.selectByUserName(user.getUserName());
            if (existing != null) {
                return Result.fail("保存失败，用户名重复");
            }
            // 新增用户加密密码
            if (StringUtils.isNotEmpty(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            userMapper.insert(user);
        }
        return Result.success("保存成功");
    }

    @PostMapping("/delete")
    public Result<String> delete(@RequestParam Integer id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            userMapper.deleteById(id);
            return Result.success("删除成功");
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/find")
    public Result<User> find(@RequestParam Integer id) {
        User user = userMapper.selectById(id);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/list")
    public Result<List<User>> list(@RequestParam(required = false) String searchParams,
                                    @RequestParam(required = false, defaultValue = "1") int page,
                                    @RequestParam(required = false, defaultValue = "10") int limit) {
        QueryParamDTO queryParamDTO = new QueryParamDTO();
        if (StringUtils.isNotEmpty(searchParams)) {
            try {
                queryParamDTO = objectMapper.readValue(searchParams, QueryParamDTO.class);
            } catch (Exception e) {
                log.warn("parse searchParams failed", e);
            }
        }
        queryParamDTO.setPageLimit(page, limit);
        List<User> itemList = userMapper.pageAll(queryParamDTO);
        int itemTotal = userMapper.countAll(queryParamDTO);
        return Result.success(itemList, itemTotal);
    }

    @GetMapping("/list")
    public ModelAndView listPage() {
        return new ModelAndView("cms/user-list");
    }

    @GetMapping("/edit")
    public ModelAndView editPage(@RequestParam Integer id) {
        User user = userMapper.selectById(id);
        return new ModelAndView("cms/user-edit", "user", user);
    }

    @GetMapping("/info")
    public ModelAndView infoPage(HttpServletRequest request) {
        Object loginUserId = request.getSession().getAttribute("userId");
        User user = null;
        if (loginUserId instanceof Integer) {
            user = userMapper.selectById((Integer) loginUserId);
        }
        return new ModelAndView("cms/user-info", "user", user);
    }
}
