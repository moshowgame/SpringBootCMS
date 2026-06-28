package com.softdev.cms.controller;

import com.softdev.cms.entity.User;
import com.softdev.cms.entity.dto.QueryParamDTO;
import com.softdev.cms.mapper.ArticleMapper;
import com.softdev.cms.mapper.ChannelMapper;
import com.softdev.cms.mapper.FormMapper;
import com.softdev.cms.mapper.UserMapper;
import com.softdev.cms.util.Result;
import com.softdev.cms.service.CmsUserDetailsService;
import com.wf.captcha.SpecCaptcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.softdev.cms.entity.Channel;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class BackEndController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ChannelMapper channelMapper;

    @Autowired
    private FormMapper formMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CmsUserDetailsService userDetailsService;

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @GetMapping("/login")
    public String loginPage() {
        return "cms/login";
    }

    /**
     * 开发环境快速登录（仅 dev profile 生效）
     * 用于绕过验证码进行本地开发测试
     */
    @PostMapping("/devLogin")
    @ResponseBody
    public Result<String> devLogin(@RequestParam String userName,
                                   @RequestParam String password,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        if (!"dev".equals(activeProfile)) {
            return Result.fail("该接口仅在开发环境可用");
        }
        HttpSession session = request.getSession();
        User user = userMapper.selectByUserName(userName);
        if (user == null) {
            return Result.fail("用户名或密码错误");
        }
        String dbPassword = user.getPassword();
        boolean passwordOk = (dbPassword != null && dbPassword.startsWith("$2"))
            ? passwordEncoder.matches(password, dbPassword)
            : password.equals(dbPassword);
        if (!passwordOk) {
            return Result.fail("用户名或密码错误");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
        session.setAttribute("currentUser", user);
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("userName", user.getUserName());
        session.setAttribute("showName", user.getShowName());
        session.setAttribute("roleId", user.getRoleId());
        return Result.success("登录成功");
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

    /**
     * 仪表盘聚合统计（用户/文章/频道/浏览/表单）。单接口返回所有计数。
     */
    @GetMapping("/dashboard/stats")
    @ResponseBody
    public Result<Map<String, Object>> dashboardStats() {
        QueryParamDTO emptyDto = new QueryParamDTO();
        Channel emptyChannel = new Channel();
        Long viewCount = articleMapper.sumViewCount();
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("userCount", userMapper.count());
        stats.put("articleCount", articleMapper.countAll(emptyDto));
        stats.put("channelCount", channelMapper.countAll(emptyChannel));
        stats.put("formCount", formMapper.countAll(emptyDto));
        stats.put("viewCount", viewCount == null ? 0L : viewCount);
        return Result.success(stats);
    }
}
