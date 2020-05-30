package com.softdev.cms.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.alibaba.fastjson.JSON;
import com.softdev.cms.entity.User;
import com.softdev.cms.mapper.UserMapper;
import com.softdev.cms.service.JwtUserDetailsService;
import com.softdev.cms.util.JwtTokenUtil;
import com.softdev.cms.util.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * spring boot code generator
 * @author zhengkai.blog.csdn.net
 */
@RestController
@Slf4j
public class IndexController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/")
    public ModelAndView defaultPage(){
        return new ModelAndView("cms/login");
    }

    @GetMapping("/welcome")
    public ModelAndView welcome(){
        return new ModelAndView("cms/welcome");
    }

    @GetMapping("/index")
    public ModelAndView indexPage(){
        return new ModelAndView("cms/index");
    }

    @GetMapping("/login")
    public ModelAndView loginPage(){
        return new ModelAndView("cms/login");
    }

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping("/login")
    public ReturnT login(String username, String password, String captcha, HttpServletRequest request){
        log.info("username-"+username+"-password-"+password+"-"+captcha);
        boolean loginSuccess = false;
        User user = userMapper.login(username.trim(),password.trim());
        String sessionCaptcha = request.getSession().getAttribute("captcha")+"";
        if(!sessionCaptcha.equals(captcha)){
            //return new ReturnT(ReturnT.FAIL_CODE,"登录失败，验证码不正确");
        }
        log.info("user:"+ JSON.toJSONString(user));
        if(user!=null&&user.getStatus()==1){
            loginSuccess = true;
            //放置用户信息到session中
            request.getSession().setAttribute("loginUser",user);
            request.getSession().setAttribute("loginUserId",user.getUserId());
            request.getSession().setAttribute("loginUserName",user.getUserName());
            request.getSession().setAttribute("roleId",user.getRoleId());
            //设置session存储时间，以秒为单位，3600=60*30即为30分钟
            request.getSession().setMaxInactiveInterval(1800);
        }
        if(loginSuccess){
            //final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            //String token = jwtTokenUtil.generateToken(userDetails);
            //自定义token生成规则
            String token = jwtTokenUtil.generateToken(user);
            return ReturnT.SUCCESS(token);
        }else{
            return ReturnT.ERROR("登录失败，账号密码不正确");
        }
    }
    @PostMapping("/api/login")
    public ReturnT apiLogin(@RequestBody User loginUser){
        boolean loginSuccess = false;
        User user = userMapper.login(loginUser.getUserName(),loginUser.getPassword());
        if(user!=null&&user.getStatus()==1){
            loginSuccess = true;
        }
        if(loginSuccess){
            //如果为空则更新openid
            if(StringUtils.isNotBlank(loginUser.getOpenId())&&StringUtils.isBlank(user.getOpenId())){
                user.setOpenId(loginUser.getOpenId());userMapper.updateById(user);
            }
            user.setPassword("");
            return new ReturnT(ReturnT.SUCCESS_CODE,"登录成功",user);
        }else{
            return new ReturnT(ReturnT.FAIL_CODE,"登录失败，账号密码不正确");
        }
    }
    @PostMapping("/logout")
    public ReturnT logout(HttpServletRequest request){
        //移除session信息
        request.getSession().removeAttribute("roleId");
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("loginUserName");
        request.getSession().removeAttribute("loginUserId");
        //关闭session
        request.getSession().invalidate();
        return new ReturnT(ReturnT.SUCCESS_CODE,"注销成功");
    }
    @GetMapping("/captcha")
    public void captcha(HttpServletResponse response,HttpSession session) throws IOException {
        //定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 100, 4, 4);
        session.setAttribute("captcha",captcha.getCode());
        log.info("captcha:"+captcha.getCode());
        captcha.write(response.getOutputStream());
    }

    @GetMapping("/userinfo")
    public ReturnT userInfo(HttpServletRequest request){
        return new ReturnT(request.getSession().getAttribute("user"));
    }
}
