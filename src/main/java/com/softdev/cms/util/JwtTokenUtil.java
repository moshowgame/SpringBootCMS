package com.softdev.cms.util;

import com.softdev.cms.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JwtTokenUtil JWT工具类
 * @author zhengkai.blog.csdn.net
 */
@Component
public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * 从JWT中解析Subject，一般是Username或者UserId
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    /**
     * 从JWT中解析RoleId
     */
    public Integer getRoleIdFromToken(String token) {
        return getAllClaimsFromToken(token).get("roleId",Integer.class);
    }
    /**
     * 从JWT中解析ShowName
     */
    public String getShowNameFromToken(String token) {
        return getAllClaimsFromToken(token).get("showName",String.class);
    }
    /**
     * 从JWT中解析过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    /**
     * 从JWT中解析所有Claims
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    /**
     * 从JWT中判断是否过期
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    /**
     * 根据UserDetail(Security)生成Token
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        //设置额外信息
        return doGenerateToken(claims, userDetails.getUsername());
    }
    /**
     * 根据User(用户自定义)生成Token
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        //把对应的内容放到JWT报文中
        claims.put("showName",user.getShowName());
        claims.put("roleId",user.getRoleId());
        claims.put("phone",user.getPhone());
        claims.put("email",user.getEmail());
        //设置额外信息
        return doGenerateToken(claims, user.getUserName());
    }
    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
    /**
     * 校验JWT
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}