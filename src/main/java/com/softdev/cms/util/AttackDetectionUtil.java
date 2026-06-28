package com.softdev.cms.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 攻击检测工具类
 * <p>
 * 检测以下攻击类型：
 * <ul>
 *   <li>SQL 注入（UNION SELECT、DROP、OR 1=1、注释符、堆叠查询等）</li>
 *   <li>XSS 攻击（script、onerror、javascript:、iframe、HTML 事件等）</li>
 *   <li>SVG 注入（&lt;svg onload=...&gt;）</li>
 *   <li>命令注入（; | &amp;&amp; 链式命令）</li>
 *   <li>路径遍历（../、..\\）</li>
 *   <li>LDAP/NoSQL 注入</li>
 *   <li>XXE（外部实体注入）</li>
 *   <li>SSRF 协议（file://、gopher://、dict://）</li>
 * </ul>
 * <p>
 * <b>注意：</b>本工具作为<b>纵深防御</b>层。真正阻止注入应依靠：
 * <ol>
 *   <li>MyBatis 预编译参数（#{}）— 阻止 SQL 注入</li>
 *   <li>FreeMarker 输出转义（?html/?xhtml）— 阻止存储型 XSS</li>
 *   <li>Spring Security 认证授权 — 阻止越权</li>
 *   <li>URL 白名单/正则校验 — 阻止 SSRF</li>
 * </ol>
 * 本工具用于在<b>入口处</b>发现可疑输入并<b>主动告警</b>，便于审计和溯源。
 */
@Slf4j
public class AttackDetectionUtil {

    /** 攻击类型枚举 */
    public enum AttackType {
        SQL_INJECTION("SQL 注入"),
        XSS("XSS 跨站脚本"),
        SVG_INJECTION("SVG 注入"),
        COMMAND_INJECTION("命令注入"),
        PATH_TRAVERSAL("路径遍历"),
        LDAP_INJECTION("LDAP 注入"),
        NOSQL_INJECTION("NoSQL 注入"),
        XXE("XML 外部实体"),
        SSRF("服务端请求伪造"),
        SUSPICIOUS("可疑输入");

        private final String description;

        AttackType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /** 检测结果 */
    public static class AttackResult {
        private final boolean detected;
        private final AttackType type;
        private final String matchedKeyword;
        private final String field;
        private final String originalValue;

        public AttackResult(boolean detected, AttackType type, String matchedKeyword,
                            String field, String originalValue) {
            this.detected = detected;
            this.type = type;
            this.matchedKeyword = matchedKeyword;
            this.field = field;
            this.originalValue = originalValue;
        }

        public boolean isDetected() { return detected; }
        public AttackType getType() { return type; }
        public String getMatchedKeyword() { return matchedKeyword; }
        public String getField() { return field; }
        public String getOriginalValue() { return originalValue; }

        /** 返回不安全的原始值（用于日志审计） */
        public String getSanitized() {
            if (originalValue == null) return null;
            String s = originalValue.replaceAll("[\\x00-\\x1F]", "");
            if (s.length() > 200) s = s.substring(0, 200) + "...";
            return s;
        }
    }

    // ==================== 攻击特征规则 ====================
    // 每条规则: 类型 + 模式 + 描述
    private static final List<Rule> RULES = new ArrayList<>();

    static {
        // SQL 注入
        addRule(AttackType.SQL_INJECTION, "(?i)(\\bunion\\b[\\s/\\*]+select\\b)", "UNION SELECT");
        addRule(AttackType.SQL_INJECTION, "(?i)\\b(select|insert|update|delete|drop|truncate|alter|create|rename)\\b[\\s/\\*]+", "SQL 关键字");
        addRule(AttackType.SQL_INJECTION, "(?i)\\b(or|and)\\b\\s+[\\w'\"\\-\\.]+\\s*=\\s*[\\w'\"\\-\\.]+", "OR/AND 永真");
        addRule(AttackType.SQL_INJECTION, "(?i)\\bor\\s+1\\s*=\\s*1\\b", "OR 1=1");
        addRule(AttackType.SQL_INJECTION, "(?i)\\band\\s+1\\s*=\\s*1\\b", "AND 1=1");
        addRule(AttackType.SQL_INJECTION, "(?i)(--|/\\*|;\\s*\\-\\-)", "SQL 注释符");
        addRule(AttackType.SQL_INJECTION, "(?i)\\binformation_schema\\b", "information_schema 探测");
        addRule(AttackType.SQL_INJECTION, "(?i)\\bsleep\\s*\\(", "时间盲注 sleep()");
        addRule(AttackType.SQL_INJECTION, "(?i)\\bbenchmark\\s*\\(", "时间盲注 benchmark()");
        addRule(AttackType.SQL_INJECTION, "(?i)\\b(load_file|into\\s+(out|dump)file)\\b", "文件读写函数");
        addRule(AttackType.SQL_INJECTION, "(?i)0x[0-9a-f]{6,}", "十六进制编码");

        // XSS
        addRule(AttackType.XSS, "(?i)<\\s*script\\b", "<script> 标签");
        addRule(AttackType.XSS, "(?i)<\\s*iframe\\b", "<iframe> 标签");
        addRule(AttackType.XSS, "(?i)<\\s*object\\b", "<object> 标签");
        addRule(AttackType.XSS, "(?i)<\\s*embed\\b", "<embed> 标签");
        addRule(AttackType.XSS, "(?i)<\\s*form\\b", "<form> 标签");
        addRule(AttackType.XSS, "(?i)<\\s*input\\b", "<input> 标签");
        addRule(AttackType.XSS, "(?i)<\\s*body\\b", "<body> 标签");
        addRule(AttackType.XSS, "(?i)<\\s*img\\b", "<img> 标签（可能含 onerror）");
        addRule(AttackType.XSS, "(?i)<\\s*link\\b", "<link> 标签");
        addRule(AttackType.XSS, "(?i)<\\s*meta\\b", "<meta> 标签");
        addRule(AttackType.XSS, "(?i)<\\s*style\\b", "<style> 标签");
        addRule(AttackType.XSS, "(?i)<\\s*base\\b", "<base> 标签");
        addRule(AttackType.XSS, "(?i)<\\s*details\\b", "<details> 标签");
        addRule(AttackType.XSS, "(?i)javascript\\s*:", "javascript: 协议");
        addRule(AttackType.XSS, "(?i)vbscript\\s*:", "vbscript: 协议");
        addRule(AttackType.XSS, "(?i)data\\s*:\\s*text/html", "data:text/html 协议");
        addRule(AttackType.XSS, "(?i)\\bon(error|load|click|mouseover|focus|blur|submit|change|keydown|keypress|keyup)\\s*=", "HTML 事件处理器");
        addRule(AttackType.XSS, "(?i)\\beval\\s*\\(", "eval() 调用");
        addRule(AttackType.XSS, "(?i)\\bdocument\\s*\\.\\s*cookie\\b", "document.cookie 窃取");
        addRule(AttackType.XSS, "(?i)\\bwindow\\s*\\.\\s*location\\b", "window.location 重定向");
        addRule(AttackType.XSS, "(?i)\\bexpression\\s*\\(", "CSS expression");
        addRule(AttackType.XSS, "(?i)@import", "CSS @import");
        addRule(AttackType.XSS, "(?i)</\\s*script\\s*>", "</script> 闭合标签");

        // SVG 注入（XSS 变种）
        addRule(AttackType.SVG_INJECTION, "(?i)<\\s*svg\\b", "<svg> 标签");
        addRule(AttackType.SVG_INJECTION, "(?i)<\\s*use\\b", "<use> 标签（SVG XSS）");
        addRule(AttackType.SVG_INJECTION, "(?i)<\\s*animate\\b", "<animate> 标签");
        addRule(AttackType.SVG_INJECTION, "(?i)<\\s*foreignObject\\b", "<foreignObject> 标签");
        addRule(AttackType.SVG_INJECTION, "(?i)<\\s*set\\b", "<set> 标签");
        addRule(AttackType.SVG_INJECTION, "(?i)\\bxlink\\s*:\\s*href", "xlink:href");
        addRule(AttackType.SVG_INJECTION, "(?i)\\bhref\\s*=\\s*[\"']?\\s*javascript", "href=javascript:");

        // 命令注入
        addRule(AttackType.COMMAND_INJECTION, "[;&|`]", "命令链式符号");
        addRule(AttackType.COMMAND_INJECTION, "(?i)\\b(cat|less|more|head|tail|nc|netcat|bash|sh|zsh|cmd|powershell)\\b", "系统命令");
        addRule(AttackType.COMMAND_INJECTION, "(?i)\\$\\([^)]*\\)", "命令替换 $(...)");
        addRule(AttackType.COMMAND_INJECTION, "(?i)`[^`]*`", "命令替换反引号");

        // 路径遍历
        addRule(AttackType.PATH_TRAVERSAL, "(\\.\\./|\\.\\.\\\\)", "../ 路径遍历");
        addRule(AttackType.PATH_TRAVERSAL, "(?i)/etc/(passwd|shadow|hosts)", "/etc 系统文件");
        addRule(AttackType.PATH_TRAVERSAL, "(?i)c:\\\\windows", "Windows 系统目录");
        addRule(AttackType.PATH_TRAVERSAL, "(?i)file\\s*://", "file:// 协议");

        // LDAP 注入
        addRule(AttackType.LDAP_INJECTION, "[\\*\\(\\)\\\\]", "LDAP 元字符");
        addRule(AttackType.LDAP_INJECTION, "(?i)\\b(objectClass|cn|ou|dc)\\s*=", "LDAP 过滤器");

        // NoSQL 注入
        addRule(AttackType.NOSQL_INJECTION, "(?i)\\$\\s*(where|ne|gt|lt|eq|in|nin|exists|regex)\\s*:", "MongoDB 操作符");
        addRule(AttackType.NOSQL_INJECTION, "(?i)\\$where\\b", "$where JavaScript 执行");

        // XXE
        addRule(AttackType.XXE, "(?i)<!\\s*entity", "<!ENTITY 外部实体");
        addRule(AttackType.XXE, "(?i)(system|public)\\s+[\"']?\\s*(file|http|ftp|//|\\\\)", "XXE 外部引用");
        addRule(AttackType.XXE, "(?i)xmlns\\s*=\\s*[\"']?\\s*(file|http|ftp)", "危险 XML 命名空间");

        // SSRF
        addRule(AttackType.SSRF, "(?i)(gopher|dict|tftp|ldap|ftp)://", "危险协议");
        addRule(AttackType.SSRF, "(?i)://(127\\.0\\.0\\.1|localhost|0\\.0\\.0\\.0)", "本地回环地址");
        addRule(AttackType.SSRF, "(?i)://(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}|192\\.168\\.\\d{1,3}\\.\\d{1,3}|172\\.(1[6-9]|2\\d|3[01])\\.\\d{1,3}\\.\\d{1,3})", "内网/私网地址");
    }

    private static void addRule(AttackType type, String pattern, String keyword) {
        RULES.add(new Rule(type, Pattern.compile(pattern), keyword));
    }

    /** 单条规则 */
    private static class Rule {
        final AttackType type;
        final Pattern pattern;
        final String keyword;
        Rule(AttackType type, Pattern pattern, String keyword) {
            this.type = type;
            this.pattern = pattern;
            this.keyword = keyword;
        }
    }

    /**
     * 检测单个字段值是否包含攻击特征
     *
     * @param value 输入值
     * @param field 字段名（用于日志）
     * @return 检测结果
     */
    public static AttackResult detect(String value, String field) {
        if (value == null || value.isEmpty()) {
            return new AttackResult(false, null, null, field, value);
        }
        for (Rule rule : RULES) {
            Matcher m = rule.pattern.matcher(value);
            if (m.find()) {
                return new AttackResult(true, rule.type, rule.keyword, field, value);
            }
        }
        return new AttackResult(false, null, null, field, value);
    }

    /**
     * 批量检测多个字段
     *
     * @param params 字段名 -> 值
     * @return 第一个被检测到的攻击结果（无攻击时为 detected=false）
     */
    public static AttackResult detect(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return new AttackResult(false, null, null, null, null);
        }
        for (Map.Entry<String, String> entry : params.entrySet()) {
            AttackResult r = detect(entry.getValue(), entry.getKey());
            if (r.isDetected()) {
                return r;
            }
        }
        return new AttackResult(false, null, null, null, null);
    }

    /**
     * 检查并记录日志
     *
     * @return true=检测到攻击，false=正常
     */
    public static boolean checkAndLog(String value, String field) {
        AttackResult r = detect(value, field);
        if (r.isDetected()) {
            logAttack(r);
            return true;
        }
        return false;
    }

    /**
     * 检查并记录日志（带上下文）
     */
    public static boolean checkAndLog(Map<String, String> params) {
        AttackResult r = detect(params);
        if (r.isDetected()) {
            logAttack(r);
            return true;
        }
        return false;
    }

    /**
     * 记录攻击日志（包含 IP、UA、用户等上下文）
     */
    public static void logAttack(AttackResult result) {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest req = attrs.getRequest();
                String ip = getClientIp(req);
                String ua = req.getHeader("User-Agent");
                String uri = req.getRequestURI();
                String method = req.getMethod();
                String userId = "anonymous";
                Object principal = org.springframework.security.core.context.SecurityContextHolder
                        .getContext().getAuthentication() != null
                        ? org.springframework.security.core.context.SecurityContextHolder
                        .getContext().getAuthentication().getPrincipal() : null;
                if (principal != null) {
                    userId = principal.toString();
                }
                log.warn("⚠ 攻击检测: type={}, field={}, keyword='{}', value='{}', ip={}, ua={}, method={}, uri={}, user={}",
                        result.getType().getDescription(),
                        result.getField(),
                        result.getMatchedKeyword(),
                        result.getSanitized(),
                        ip,
                        ua,
                        method,
                        uri,
                        userId);
            } else {
                log.warn("⚠ 攻击检测: type={}, field={}, keyword='{}', value='{}'",
                        result.getType().getDescription(),
                        result.getField(),
                        result.getMatchedKeyword(),
                        result.getSanitized());
            }
        } catch (Exception e) {
            log.warn("⚠ 攻击检测: type={}, field={}, keyword='{}', value='{}'",
                    result.getType().getDescription(),
                    result.getField(),
                    result.getMatchedKeyword(),
                    result.getSanitized());
        }
    }

    private static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
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

    /**
     * 包装为友好提示（给前端展示）
     */
    public static Map<String, Object> toResponse(AttackResult result) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("detected", result.isDetected());
        if (result.isDetected()) {
            map.put("type", result.getType().name());
            map.put("typeDescription", result.getType().getDescription());
            map.put("keyword", result.getMatchedKeyword());
            map.put("field", result.getField());
            map.put("message", String.format("输入包含 %s 特征（%s），已被拒绝", result.getType().getDescription(), result.getMatchedKeyword()));
        }
        return map;
    }

    /**
     * 清理输入值（去除危险字符，保留可读性）
     * <p>
     * <b>注意：</b>这只是<b>视觉清洗</b>用于显示，<b>不能</b>作为安全防护！
     * 实际安全防护必须依靠转义、参数化、白名单等手段。
     */
    public static String sanitizeForDisplay(String value) {
        if (value == null) return "";
        // 去除控制字符
        String s = value.replaceAll("[\\x00-\\x1F\\x7F]", "");
        // 截断过长字符串
        if (s.length() > 200) s = s.substring(0, 200) + "...";
        return s;
    }

    /** 获取所有支持的规则数量（用于监控） */
    public static int getRuleCount() {
        return RULES.size();
    }

    /** 获取所有攻击类型（用于管理后台显示） */
    public static List<Map<String, String>> getAllAttackTypes() {
        List<Map<String, String>> list = new ArrayList<>();
        for (AttackType t : AttackType.values()) {
            Map<String, String> m = new LinkedHashMap<>();
            m.put("code", t.name());
            m.put("description", t.getDescription());
            list.add(m);
        }
        return Collections.unmodifiableList(list);
    }
}
