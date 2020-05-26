package com.softdev.cms.config;

import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
/**
*  2019-2-11 liutf WebMvcConfig 整合 cors 和 SpringMvc MessageConverter
*/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

/*    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedHeaders("x-requested-with")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS", "TRACE")
                .maxAge(3600);
    }*/

    @Autowired
    private UserLoginInterceptor userLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //添加对用户是否登录的拦截器，并添加过滤项、排除项
        registry.addInterceptor(userLoginInterceptor).addPathPatterns("/**")
                //排除静态资源文件
                .excludePathPatterns("/static/**")
                //排除小程序api
                .excludePathPatterns("/api/**")
                //排除FrontEnd页面
                .excludePathPatterns("/page/**")
                //.excludePathPatterns("/index")
                //排除登录页面和登录方法
                .excludePathPatterns("/login")
                //微信认证
                .excludePathPatterns("/MP_verify_*.txt")
                //活动签到
                .excludePathPatterns("/activitySign/display")
                .excludePathPatterns("/activitySign/submit")
                //排除验证码
                .excludePathPatterns("/captcha")
                //排除注销方法
                .excludePathPatterns("/logout");
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //FastJsonHttpMessageConverter
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setCharset(StandardCharsets.UTF_8);
        fastConverter.setFastJsonConfig(fastJsonConfig);

        //StringHttpMessageConverter
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        stringConverter.setDefaultCharset(StandardCharsets.UTF_8);
        stringConverter.setSupportedMediaTypes(fastMediaTypes);
        converters.add(stringConverter);
        converters.add(fastConverter);
    }
}