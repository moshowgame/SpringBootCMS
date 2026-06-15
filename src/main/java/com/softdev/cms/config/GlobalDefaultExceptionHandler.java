package com.softdev.cms.config;

import com.softdev.cms.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalDefaultExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<String> defaultExceptionHandler(HttpServletRequest req, Exception e) {
        log.error("Unhandled exception on {}", req.getRequestURI(), e);
        return Result.fail("服务器内部错误: " + e.getMessage());
    }
}
