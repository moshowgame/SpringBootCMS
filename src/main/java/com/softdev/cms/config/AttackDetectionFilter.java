package com.softdev.cms.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softdev.cms.util.AttackDetectionUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 全局攻击检测过滤器
 * <p>
 * 拦截所有进入应用的请求，检测：
 * <ul>
 *   <li>URL Query 参数</li>
 *   <li>Form 表单参数</li>
 *   <li>JSON Body 参数（一次性读取并缓存以便业务侧再次读取）</li>
 * </ul>
 * 检测到攻击特征时：
 * <ol>
 *   <li>记录完整审计日志（IP、UA、用户、URI、字段、原始值）</li>
 *   <li>返回 403 拒绝请求</li>
 * </ol>
 * <p>
 * <b>注意：</b>这是纵深防御层，<b>不替代</b>业务层的：
 * <ul>
 *   <li>MyBatis 预编译参数（#{}）</li>
 *   <li>FreeMarker 输出转义</li>
 *   <li>Spring Security 鉴权</li>
 *   <li>URL 白名单/正则校验</li>
 * </ul>
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
@Slf4j
public class AttackDetectionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 排除列表：这些 URL 不做攻击检测（一般是因为内容特殊或非业务）
     */
    private static final Set<String> EXCLUDE_URLS = Set.of(
        "/admin/login",
        "/admin/captcha",
        "/file/editorUpload",
        "/file/layuiUpload",
        "/file/saveNetworkImg",
        "/file/base64upload"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        // 文件上传接口天然包含二进制内容，跳过
        if (uri != null && (uri.startsWith("/file/upload") || uri.startsWith("/file/files/"))) {
            return true;
        }
        return EXCLUDE_URLS.contains(uri);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 缓存 body（如果需要），以便业务侧再次读取
        HttpServletRequest requestToUse = request;
        String contentType = request.getContentType();
        boolean isJsonOrForm = contentType != null && (
            contentType.toLowerCase().contains("application/json") ||
            contentType.toLowerCase().contains("application/x-www-form-urlencoded")
        );

        if (isJsonOrForm) {
            byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
            requestToUse = new CachedBodyRequestWrapper(request, body);
        }

        // 1. 检测 URL Query 参数
        AttackDetectionUtil.AttackResult attack = checkQueryParams(requestToUse);
        if (attack == null) {
            // 2. 检测 Form/JSON Body
            attack = checkBody(requestToUse);
        }

        if (attack != null && attack.isDetected()) {
            AttackDetectionUtil.logAttack(attack);
            writeRejectResponse(response, attack);
            return;
        }

        // 3. 继续处理请求（使用包装后的 request，使 @RequestBody 能正常读取 body）
        filterChain.doFilter(requestToUse, response);
    }

    /** 检测 URL Query 参数 */
    private AttackDetectionUtil.AttackResult checkQueryParams(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        if (params == null || params.isEmpty()) return null;
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            String field = entry.getKey();
            String[] values = entry.getValue();
            if (values == null) continue;
            for (String value : values) {
                if (value == null) continue;
                AttackDetectionUtil.AttackResult r = AttackDetectionUtil.detect(value, field);
                if (r.isDetected()) return r;
            }
        }
        return null;
    }

    /** 检测 Body（仅 JSON 类型） */
    private AttackDetectionUtil.AttackResult checkBody(HttpServletRequest request) throws IOException {
        String contentType = request.getContentType();
        if (contentType == null) return null;

        if (!contentType.toLowerCase().contains("application/json")) {
            // 非 JSON：仅检测 form-urlencoded
            if (contentType.toLowerCase().contains("application/x-www-form-urlencoded")) {
                return checkFormBody(request);
            }
            return null;
        }

        // 从 wrapper 中读取 body（原始流已在过滤器中读取并缓存）
        byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
        String bodyStr = new String(body, StandardCharsets.UTF_8);
        if (bodyStr.isEmpty()) return null;

        try {
            // 简单 JSON 解析：把 value 全部 flat 出来检查
            Map<String, Object> jsonMap = objectMapper.readValue(bodyStr, Map.class);
            Map<String, String> flat = flattenJson(jsonMap, "");
            for (Map.Entry<String, String> entry : flat.entrySet()) {
                AttackDetectionUtil.AttackResult r = AttackDetectionUtil.detect(entry.getValue(), entry.getKey());
                if (r.isDetected()) return r;
            }
        } catch (Exception e) {
            // JSON 解析失败时不做检测（避免误报）
            log.debug("attack detection: JSON body parse skipped: {}", e.getMessage());
        }
        return null;
    }

    /** 检测 form-urlencoded body */
    private AttackDetectionUtil.AttackResult checkFormBody(HttpServletRequest request) throws IOException {
        byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
        String bodyStr = new String(body, StandardCharsets.UTF_8);
        if (bodyStr.isEmpty()) return null;
        Map<String, String> params = new LinkedHashMap<>();
        for (String pair : bodyStr.split("&")) {
            int idx = pair.indexOf('=');
            if (idx > 0) {
                String key = pair.substring(0, idx);
                String val = pair.substring(idx + 1);
                params.put(key, val);
            }
        }
        return AttackDetectionUtil.detect(params);
    }

    /** 把嵌套 JSON 拍平为 key.value -> string */
    @SuppressWarnings("unchecked")
    private Map<String, String> flattenJson(Object obj, String prefix) {
        Map<String, String> result = new LinkedHashMap<>();
        if (obj instanceof Map) {
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) obj).entrySet()) {
                String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
                result.putAll(flattenJson(entry.getValue(), key));
            }
        } else if (obj instanceof java.util.List) {
            int idx = 0;
            for (Object item : (java.util.List<?>) obj) {
                String key = prefix + "[" + (idx++) + "]";
                result.putAll(flattenJson(item, key));
            }
        } else if (obj != null) {
            result.put(prefix, obj.toString());
        }
        return result;
    }

    /** 写拒绝响应 */
    private void writeRejectResponse(HttpServletResponse response, AttackDetectionUtil.AttackResult attack) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", 403);
        body.put("msg", attack.getType().getDescription() + " 攻击已拦截：输入包含 '" + attack.getMatchedKeyword() + "' 特征");
        body.put("data", null);
        body.put("count", 0);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

    /**
     * 缓存请求 body 的包装器
     */
    static class CachedBodyRequestWrapper extends HttpServletRequestWrapper {
        private final byte[] cachedBody;

        public CachedBodyRequestWrapper(HttpServletRequest request, byte[] cachedBody) {
            super(request);
            this.cachedBody = cachedBody;
        }

        @Override
        public jakarta.servlet.ServletInputStream getInputStream() throws IOException {
            return new jakarta.servlet.ServletInputStream() {
                private final ByteArrayInputStream bais = new ByteArrayInputStream(cachedBody);

                @Override
                public boolean isFinished() { return bais.available() == 0; }

                @Override
                public boolean isReady() { return true; }

                @Override
                public void setReadListener(jakarta.servlet.ReadListener readListener) { }

                @Override
                public int read() throws IOException { return bais.read(); }
            };
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new java.io.InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
        }
    }
}
