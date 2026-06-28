package com.softdev.cms.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softdev.cms.service.CmsUserDetailsService;
import com.softdev.cms.util.Result;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, CmsUserDetailsService userDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder)
            .and()
            .build();
    }

    /**
     * 未认证处理：AJAX 请求返回 JSON 401，普通页面请求重定向到登录页
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            // AJAX/JSON 请求：返回 401 JSON
            String requestedWith = request.getHeader("X-Requested-With");
            String accept = request.getHeader("Accept");
            boolean isAjax = "XMLHttpRequest".equals(requestedWith)
                || (accept != null && accept.contains("application/json"))
                || request.getRequestURI().startsWith(request.getContextPath() + "/api")
                || request.getRequestURI().contains("/list")
                || request.getRequestURI().contains("/find")
                || request.getRequestURI().contains("/save")
                || request.getRequestURI().contains("/delete")
                || request.getRequestURI().contains("/publish")
                || request.getRequestURI().contains("/audit");
            if (isAjax) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                Result<String> result = Result.fail("未登录或登录已过期，请重新登录");
                response.getWriter().write(new ObjectMapper().writeValueAsString(result));
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/login");
            }
        };
    }

    /**
     * 权限不足处理：返回 JSON 403 提示
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            log.warn("AccessDenied: {} {} - {} (user: {}, authorities: {})",
                    request.getMethod(), request.getRequestURI(), accessDeniedException.getMessage(),
                    request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous",
                    request.getUserPrincipal() != null ? request.getUserPrincipal() : "n/a");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            Result<String> result = Result.fail("权限不足，无法访问该资源");
            response.getWriter().write(new ObjectMapper().writeValueAsString(result));
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                    AuthenticationEntryPoint authEntryPoint,
                                                    AccessDeniedHandler deniedHandler) throws Exception {
        http
            // CSRF 保护：针对管理后台的 state-changing 操作（POST/PUT/DELETE）
            // 公开接口（前台展示、表单提交、验证码、登录）已豁免
            // 关键：使用 CsrfTokenRequestAttributeHandler（非 XOR）确保 token 立即被解析
            // 否则 XSRF-TOKEN Cookie 不会被设置，前端 JS 无法读取
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(
                    "/", "/page/**",                  // 前台页面和接口
                    "/admin/login", "/admin/captcha", "/admin/devLogin",  // 登录相关
                    "/api/public/**",                  // 公开 API
                    "/file/files/*",                    // 公开文件访问
                    "/file/editorUpload",               // TinyMCE 上传
                    "/file/layuiUpload",                // LayUI 上传
                    "/formSubmitValue/submit",          // 表单提交
                    "/activitySign/save"                // 活动签到
                )
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
                .contentTypeOptions(c -> {})  // 启用 X-Content-Type-Options: nosniff
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000)
                )
                .contentSecurityPolicy(csp -> csp.policyDirectives(
                    "default-src 'self'; " +
                    "script-src 'self' 'unsafe-inline'; " +
                    "style-src 'self' 'unsafe-inline'; " +
                    "img-src 'self' data: https: http:; " +
                    "font-src 'self' data:; " +
                    "connect-src 'self'; " +
                    "frame-ancestors 'self'; " +
                    "form-action 'self';"
                ))
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation().migrateSession()  // Session 固定防护：登录后迁移Session
                .maximumSessions(1)
            )
            .authorizeHttpRequests(auth -> auth
                // 公开访问
                .requestMatchers("/", "/page/**", "/static/**").permitAll()
                .requestMatchers("/admin/login", "/admin/captcha", "/admin/devLogin").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/file/files/*").permitAll()
                // 写操作（保存/删除/审核/发布）— 仅管理员（防越权）
                .requestMatchers(HttpMethod.POST, "/user/save", "/user/delete").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/menu/save", "/menu/delete").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/siteConfig/save", "/siteConfig/delete").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/template/save", "/template/delete").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/auditLog/**").hasRole("ADMIN")
                // 其他后台接口需要认证（任何已登录用户可访问列表/查看）
                .requestMatchers("/admin/**").authenticated()
                .requestMatchers("/article/**", "/channel/**").authenticated()
                .requestMatchers("/activity/**", "/activitySign/**").authenticated()
                .requestMatchers("/form/**", "/formItem/**", "/formSubmit/**", "/formSubmitValue/**").authenticated()
                .requestMatchers("/media/**", "/tag/**").authenticated()
                .requestMatchers("/user/**", "/menu/**", "/siteConfig/**", "/template/**", "/auditLog/**").authenticated()
                .requestMatchers("/file/**").authenticated()
                // 其余请求
                .anyRequest().denyAll()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authEntryPoint)
                .accessDeniedHandler(deniedHandler)
            )
            .formLogin(form -> form.disable())
            .logout(logout -> logout
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/admin/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "XSRF-TOKEN")
            );

        return http.build();
    }
}
