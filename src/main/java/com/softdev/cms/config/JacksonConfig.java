package com.softdev.cms.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * Jackson 全局配置
 * <p>
 * 注册自定义 Date 反序列化器，使后端能接受多种日期格式：
 * <ul>
 *   <li>yyyy-MM-dd HH:mm:ss（默认）</li>
 *   <li>yyyy-MM-dd'T'HH:mm（HTML datetime-local 提交格式）</li>
 *   <li>其他常用格式（详见 FlexibleDateDeserializer）</li>
 * </ul>
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            // 全局注册 Date 类型的反序列化器
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Date.class, new FlexibleDateDeserializer());
            builder.modulesToInstall(module);
        };
    }
}
