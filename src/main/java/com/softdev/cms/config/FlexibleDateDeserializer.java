package com.softdev.cms.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 灵活的 Date 反序列化器：支持多种日期格式
 * <p>
 * 支持的格式（按优先级匹配）：
 * <ul>
 *   <li>yyyy-MM-dd HH:mm:ss</li>
 *   <li>yyyy-MM-dd'T'HH:mm:ss</li>
 *   <li>yyyy-MM-dd'T'HH:mm</li>
 *   <li>yyyy-MM-dd'T'HH:mm:ss.SSS</li>
 *   <li>yyyy-MM-dd</li>
 *   <li>yyyy/MM/dd HH:mm:ss</li>
 *   <li>yyyy/MM/dd</li>
 * </ul>
 * 主要解决 HTML &lt;input type="datetime-local"&gt; 提交 "yyyy-MM-dd'T'HH:mm" 而后端期望 "yyyy-MM-dd HH:mm:ss" 的格式冲突
 */
public class FlexibleDateDeserializer extends JsonDeserializer<Date> {

    private static final List<String> PATTERNS = Arrays.asList(
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd'T'HH:mm",
        "yyyy-MM-dd'T'HH:mm:ss.SSS",
        "yyyy-MM-dd",
        "yyyy/MM/dd HH:mm:ss",
        "yyyy/MM/dd"
    );

    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT+8");

    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getValueAsString();
        if (value == null || value.isEmpty()) {
            return null;
        }
        // 数字时间戳（毫秒或秒）
        if (value.matches("^\\d+$")) {
            try {
                long ts = Long.parseLong(value);
                // 如果是秒级时间戳（10 位），转为毫秒
                if (ts < 10000000000L) {
                    ts = ts * 1000L;
                }
                return new Date(ts);
            } catch (NumberFormatException e) {
                throw new IOException("无法解析时间戳: " + value, e);
            }
        }
        // 尝试每种格式
        for (String pattern : PATTERNS) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                sdf.setTimeZone(TIME_ZONE);
                // setLenient(false) 严格校验日期合法性
                sdf.setLenient(false);
                return sdf.parse(value);
            } catch (ParseException ignore) {
                // 继续尝试下一种格式
            }
        }
        throw new IOException("无法解析日期字符串: '" + value + "'，支持格式: " + PATTERNS);
    }
}
