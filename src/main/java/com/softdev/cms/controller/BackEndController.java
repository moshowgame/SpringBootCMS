package com.softdev.cms.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.softdev.cms.entity.User;
import com.softdev.cms.mapper.UserMapper;
import com.softdev.cms.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Controller
@RequestMapping("/admin")
public class BackEndController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @GetMapping("/login")
    public String loginPage() {
        return "cms/login";
    }

    @GetMapping("/captcha")
    @ResponseBody
    public Result<String> captcha(HttpServletRequest request) {
        String captchaText = defaultKaptcha.createText();
        HttpSession session = request.getSession();
        session.setAttribute("captcha", captchaText);
        BufferedImage image = defaultKaptcha.createImage(captchaText);
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", os);
            String base64 = Base64.getEncoder().encodeToString(os.toByteArray());
            return Result.success("data:image/jpg;base64," + base64);
        } catch (Exception e) {
            return Result.fail("验证码生成失败");
        }
    }

    @PostMapping("/login")
    @ResponseBody
    public Result<String> login(@RequestParam String userName,
                                @RequestParam String password,
                                @RequestParam String captcha,
                                HttpServletRequest request) {
        HttpSession session = request.getSession();
        String sessionCaptcha = (String) session.getAttribute("captcha");
        if (StringUtils.isBlank(captcha) || !captcha.equalsIgnoreCase(sessionCaptcha)) {
            return Result.fail("验证码错误");
        }
        session.removeAttribute("captcha");

        User user = userMapper.selectByUserName(userName);
        if (user == null) {
            return Result.fail("用户名或密码错误");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return Result.fail("用户名或密码错误");
        }

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
