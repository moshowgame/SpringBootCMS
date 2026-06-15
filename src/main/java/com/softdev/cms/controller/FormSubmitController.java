package com.softdev.cms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softdev.cms.entity.FormSubmit;
import com.softdev.cms.entity.dto.QueryParamDTO;
import com.softdev.cms.mapper.FormSubmitMapper;
import com.softdev.cms.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/formSubmit")
public class FormSubmitController {

    @Autowired
    private FormSubmitMapper formSubmitMapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/save")
    public Result<String> save(@RequestBody FormSubmit formSubmit) {
        try {
            log.info("formSubmit:{}", objectMapper.writeValueAsString(formSubmit));
        } catch (Exception e) {
            log.warn("serialize formSubmit failed", e);
        }
        FormSubmit oldFormSubmit = formSubmitMapper.selectById(formSubmit.getSubmitId());
        if (oldFormSubmit != null) {
            formSubmitMapper.updateById(formSubmit);
        } else {
            formSubmitMapper.insert(formSubmit);
        }
        return Result.success("保存成功");
    }

    @PostMapping("/delete")
    public Result<String> delete(@RequestParam Integer id) {
        FormSubmit formSubmit = formSubmitMapper.selectById(id);
        if (formSubmit != null) {
            formSubmitMapper.deleteById(id);
            return Result.success("删除成功");
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/find")
    public Result<FormSubmit> find(@RequestParam Integer id) {
        FormSubmit formSubmit = formSubmitMapper.selectById(id);
        if (formSubmit != null) {
            return Result.success(formSubmit);
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/list")
    public Result<List<FormSubmit>> list(@RequestParam(required = false) String searchParams,
                                          @RequestParam(required = false, defaultValue = "1") int page,
                                          @RequestParam(required = false, defaultValue = "10") int limit) {
        QueryParamDTO queryParamDTO = new QueryParamDTO();
        if (StringUtils.isNotEmpty(searchParams)) {
            try {
                queryParamDTO = objectMapper.readValue(searchParams, QueryParamDTO.class);
            } catch (Exception e) {
                log.warn("parse searchParams failed", e);
            }
        }
        queryParamDTO.setPageLimit(page, limit);
        List<FormSubmit> itemList = formSubmitMapper.pageAll(queryParamDTO);
        int itemCount = formSubmitMapper.countAll(queryParamDTO);
        return Result.success(itemList, itemCount);
    }

    @GetMapping("/list")
    public ModelAndView listPage(@RequestParam(required = false) Integer formId) {
        return new ModelAndView("cms/formSubmit-list", "formId", formId);
    }

    @PostMapping("/audit")
    public Result<String> audit(@RequestBody List<FormSubmit> formSubmitList,
                                 @RequestParam Integer status,
                                 @RequestParam(required = false) Integer userId,
                                 @RequestParam(required = false) String userName) {
        int successCount = 0;
        for (FormSubmit formSubmit : formSubmitList) {
            if (status == 2 && formSubmit.getStatus() == 1) {
                formSubmit.setStatus(2);
                formSubmit.setAuditTime(new Date());
                formSubmit.setAuditUserId(userId);
                formSubmit.setAuditUserName(userName);
                formSubmitMapper.updateById(formSubmit);
                successCount++;
            } else if (status == 0 && formSubmit.getStatus() == 1) {
                formSubmit.setStatus(0);
                formSubmit.setAuditTime(new Date());
                formSubmit.setAuditUserId(userId);
                formSubmit.setAuditUserName(userName);
                formSubmitMapper.updateById(formSubmit);
                successCount++;
            } else if (status == 3 && formSubmit.getStatus() == 2) {
                formSubmit.setStatus(3);
                formSubmit.setAuditTime(new Date());
                formSubmit.setAuditUserId(userId);
                formSubmit.setAuditUserName(formSubmit.getAuditUserName() + "," + userName);
                formSubmitMapper.updateById(formSubmit);
                successCount++;
            }
        }
        return Result.success("审核" + ((status == 0) ? "不" : "") + "通过 " + successCount + " / " + formSubmitList.size());
    }
}
