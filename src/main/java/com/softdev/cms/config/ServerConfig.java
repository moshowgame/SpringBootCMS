package com.softdev.cms.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ServerConfig implements ApplicationListener<WebServerInitializedEvent> {

    private int serverPort;
    private String serverPath;

    public int getPort() {
        return this.serverPort;
    }

    public String getServerUrl() {
        return "http://localhost:" + serverPort + serverPath;
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        this.serverPort = event.getWebServer().getPort();
        this.serverPath = event.getApplicationContext().getApplicationName();
        log.info("项目启动成功！访问地址: {}", getServerUrl());
    }
}
