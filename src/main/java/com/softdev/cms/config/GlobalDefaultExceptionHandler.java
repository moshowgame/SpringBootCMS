package com.softdev.cms.config;

import com.softdev.cms.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
@Slf4j
public class GlobalDefaultExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public Result<String> handleNoResourceFoundException(HttpServletRequest req, NoResourceFoundException e) {
        log.warn("Resource not found: {}", req.getRequestURI());
        return Result.fail("资源不存在: " + req.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<String> defaultExceptionHandler(HttpServletRequest req, Exception e) {
        log.error("Unhandled exception on {}", req.getRequestURI(), e);
        return Result.fail("服务器内部错误: " + e.getMessage());
    }
}
