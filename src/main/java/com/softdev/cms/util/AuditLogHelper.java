package com.softdev.cms.util;

import com.softdev.cms.entity.AuditLog;
import com.softdev.cms.mapper.AuditLogMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuditLogHelper {

    @Autowired
    private AuditLogMapper auditLogMapper;

    public void log(Integer userId, String userName, String action, String module,
                    Integer targetId, String targetName, String detail, HttpServletRequest request) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setUserName(userName);
        log.setAction(action);
        log.setModule(module);
        log.setTargetId(targetId != null ? String.valueOf(targetId) : null);
        log.setTargetName(targetName);
        log.setDetail(detail);
        if (request != null) {
            log.setIp(getClientIp(request));
            log.setUserAgent(request.getHeader("User-Agent"));
        }
        auditLogMapper.insert(log);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多级代理取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
