package com.softdev.cms.controller;

import com.softdev.cms.entity.User;
import com.softdev.cms.mapper.UserMapper;
import com.softdev.cms.util.Result;
import com.softdev.cms.service.CmsUserDetailsService;
import com.wf.captcha.SpecCaptcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;

@Controller
@RequestMapping("/admin")
public class BackEndController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CmsUserDetailsService userDetailsService;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @GetMapping("/login")
    public String loginPage() {
        return "cms/login";
    }

    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/png");
        response.setHeader("Cache-Control", "no-store");
        try {
            SpecCaptcha captcha = new SpecCaptcha(130, 48, 4);
            String text = captcha.text();
            HttpSession session = request.getSession();
            session.setAttribute("captcha", text.toLowerCase());
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                captcha.out(os);
                response.getOutputStream().write(os.toByteArray());
            }
        } catch (Exception e) {
            throw new RuntimeException("验证码生成失败", e);
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public Result<String> login(@RequestParam String userName,
                                @RequestParam String password,
                                @RequestParam String captcha,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        HttpSession session = request.getSession();
        String sessionCaptcha = (String) session.getAttribute("captcha");
        if (StringUtils.isBlank(captcha) || StringUtils.isBlank(sessionCaptcha)
                || !captcha.toLowerCase().equals(sessionCaptcha)) {
            return Result.fail("验证码错误");
        }
        session.removeAttribute("captcha");

        User user = userMapper.selectByUserName(userName);
        if (user == null) {
            return Result.fail("用户名或密码错误");
        }

        // 兼容 BCrypt 哈希和明文密码 (便于初始化数据)
        boolean passwordOk;
        String dbPassword = user.getPassword();
        if (dbPassword != null && dbPassword.startsWith("$2")) {
            passwordOk = passwordEncoder.matches(password, dbPassword);
        } else {
            passwordOk = password.equals(dbPassword);
        }
        if (!passwordOk) {
            return Result.fail("用户名或密码错误");
        }

        // 通过 Spring Security 建立认证 (解决 403 问题)
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);

        // 保存用户信息到 session (供页面使用)
        session.setAttribute("currentUser", user);
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("userName", user.getUserName());
        session.setAttribute("showName", user.getShowName());
        session.setAttribute("roleId", user.getRoleId());

        return Result.success("登录成功");
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return "redirect:/admin/login";
    }

    @GetMapping("/index")
    public String index() {
        return "cms/index";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "cms/welcome";
    }
}
