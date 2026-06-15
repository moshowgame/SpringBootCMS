package com.softdev.cms.config;

import com.softdev.cms.service.CmsUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
            )
            .authorizeHttpRequests(auth -> auth
                // 公开访问
                .requestMatchers("/", "/page/**", "/static/**").permitAll()
                .requestMatchers("/admin/login", "/admin/captcha").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/file/files/*").permitAll()
                // 管理员接口
                .requestMatchers("/admin/api/user/**").hasRole("ADMIN")
                // 其他后台接口需要认证
                .requestMatchers("/admin/**").authenticated()
                .requestMatchers("/article/**", "/channel/**", "/menu/**", "/user/**").authenticated()
                .requestMatchers("/activity/**", "/activitySign/**").authenticated()
                .requestMatchers("/form/**", "/formItem/**", "/formSubmit/**", "/formSubmitValue/**").authenticated()
                .requestMatchers("/template/**").authenticated()
                .requestMatchers("/media/**", "/auditLog/**", "/siteConfig/**", "/tag/**").authenticated()
                .requestMatchers("/file/**").authenticated()
                // 其余请求
                .anyRequest().denyAll()
            )
            .formLogin(form -> form.disable())
            .logout(logout -> logout
                .logoutUrl("/admin/logout")
                .logoutSuccessUrl("/admin/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }
}
