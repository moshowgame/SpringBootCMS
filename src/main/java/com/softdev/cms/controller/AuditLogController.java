package com.softdev.cms.controller;

import com.softdev.cms.entity.AuditLog;
import com.softdev.cms.mapper.AuditLogMapper;
import com.softdev.cms.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/auditLog")
public class AuditLogController {

    @Autowired
    private AuditLogMapper auditLogMapper;

    @PostMapping("/list")
    public Result<List<AuditLog>> list(@RequestParam(required = false) Integer userId,
                                       @RequestParam(required = false) String userName,
                                       @RequestParam(required = false) String action,
                                       @RequestParam(required = false) String module,
                                       @RequestParam(required = false, defaultValue = "1") int page,
                                       @RequestParam(required = false, defaultValue = "10") int limit) {
        int offset = (page - 1) * limit;
        List<AuditLog> itemList = auditLogMapper.pageAll(userId, userName, action, module, offset, limit);
        int itemTotal = auditLogMapper.countAll(userId, userName, action, module);
        return Result.success(itemList, itemTotal);
    }

    @GetMapping("/list")
    public ModelAndView listPage() {
        return new ModelAndView("cms/audit-log-list");
    }
}
