package com.softdev.cms.config;


import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class UserLoginInterceptor implements HandlerInterceptor {

/*    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler)throws Exception {
        HttpSession session = request.getSession(true);
        Object user=session.getAttribute("loginUser");
        Object openid=session.getAttribute("openid");
        if(session.getAttribute("systemName")==null){
            session.setAttribute("systemName","SpringBootCMS");
        }
        if(null!=user||openid!=null) {
            //已登录
            return true;
        }else {//未登录
            //直接重定向到登录页面
            response.sendRedirect(request.getContextPath()+"/login");
            return false;
        }
    }*/

}
