package com.softdev.cms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.softdev.cms.entity.User;
import com.softdev.cms.entity.jwt.JwtUser;
import com.softdev.cms.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
/**
 * JwtUserDetailsService
 *	 	实现UserDetailsService,重写loadUserByUsername方法
 *  	返回随机生成的user,pass是密码,这里固定生成的
 *  	如果你自己需要定制查询user的方法,请改造这里
 * @author zhengkai.blog.csdn.net
 */
@Service
public class JwtUserDetailsService implements UserDetailsService{

    @Autowired
    private UserMapper userMapper;

    /**
     * 获取username并返回给SpringSecurity(不需要处理密码)
     * */
    @Override
    public UserDetails loadUserByUsername(String username) {
        //String pass = new BCryptPasswordEncoder().encode("pass");
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("user_name",username));
        if (user!=null) {
            return new JwtUser(user.getUserId().toString(), user.getUserName(),user.getPassword(),(user.getRoleId()==9)?"ADMIN":"USER", true);
        } else {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
    }
}