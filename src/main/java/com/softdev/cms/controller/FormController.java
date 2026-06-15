package com.softdev.cms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softdev.cms.entity.Form;
import com.softdev.cms.entity.FormItem;
import com.softdev.cms.entity.dto.FormSubmitValueDTO;
import com.softdev.cms.entity.dto.QueryParamDTO;
import com.softdev.cms.mapper.FormItemMapper;
import com.softdev.cms.mapper.FormSubmitValueMapper;
import com.softdev.cms.mapper.FormMapper;
import com.softdev.cms.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/form")
public class FormController {

    @Autowired
    private FormMapper formMapper;
    @Autowired
    private FormItemMapper formItemMapper;
    @Autowired
    private FormSubmitValueMapper formSubmitValueMapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/save")
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "cache-form", allEntries = true)
    public Result<String> save(@RequestBody Form form) {
        try {
            log.info("form:{}", objectMapper.writeValueAsString(form));
        } catch (Exception e) {
            log.warn("serialize form failed", e);
        }
        Form oldForm = formMapper.selectById(form.getFormId());
        if (oldForm != null) {
            formMapper.updateById(form);
        } else {
            // 检查名字重复
            QueryParamDTO checkDto = new QueryParamDTO();
            checkDto.setPageLimit(1, 1000);
            List<Form> existing = formMapper.pageAll(checkDto);
            boolean nameDuplicate = existing.stream()
                    .anyMatch(f -> form.getFormName().equals(f.getFormName()));
            if (nameDuplicate) {
                return Result.fail("保存失败，名字重复");
            }
            formMapper.insert(form);
            // 新增默认的表单项目
            FormItem formItem1 = new FormItem(form.getFormId(), "姓名", "input", "");
            FormItem formItem2 = new FormItem(form.getFormId(), "联系方式", "input", "");
            FormItem formItem3 = new FormItem(form.getFormId(), "描述", "textarea", "");
            FormItem formItem4 = new FormItem(form.getFormId(), "附件上传", "fileupload", "");
            formItemMapper.insert(formItem1);
            formItemMapper.insert(formItem2);
            formItemMapper.insert(formItem3);
            formItemMapper.insert(formItem4);
        }
        return Result.success("保存成功");
    }

    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = "cache-form", allEntries = true)
    public Result<String> delete(@RequestParam Integer id) {
        Form form = formMapper.selectById(id);
        if (form != null) {
            formMapper.deleteById(id);
            formItemMapper.deleteByFormId(id);
            return Result.success("删除成功");
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/find")
    @Cacheable(value = "cache-form", key = "#id")
    public Result<Form> find(@RequestParam Integer id) {
        Form form = formMapper.selectById(id);
        if (form != null) {
            return Result.success(form);
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/list")
    public Result<List<Form>> list(@RequestParam(required = false) String searchParams,
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
        List<Form> itemList = formMapper.pageAll(queryParamDTO);
        int itemTotal = formMapper.countAll(queryParamDTO);
        return Result.success(itemList, itemTotal);
    }

    @GetMapping("/list")
    public ModelAndView listPage() {
        return new ModelAndView("cms/form-list");
    }

    @GetMapping("/edit")
    public ModelAndView editPage(@RequestParam Integer id) {
        Form form = formMapper.selectById(id);
        return new ModelAndView("cms/form-edit", "form", form);
    }

    @GetMapping("/dashboard")
    public ModelAndView dashboard() {
        ModelAndView modelAndView = new ModelAndView("cms/form-type3dashboard");
        List<FormSubmitValueDTO> formSubmitValueDTOList = formSubmitValueMapper.getSelectCountList();
        try {
            modelAndView.addObject("data", objectMapper.writeValueAsString(formSubmitValueDTOList));
        } catch (Exception e) {
            modelAndView.addObject("data", "[]");
        }
        return modelAndView;
    }

    @PostMapping("/publish")
    @CacheEvict(value = "cache-form", allEntries = true)
    public Result<String> publish(@RequestParam Integer id, @RequestParam Integer status) {
        Form form = formMapper.selectById(id);
        if (form != null) {
            form.setStatus(status);
            formMapper.updateById(form);
            return Result.success((status == 1) ? "表单已发布" : "表单已暂停");
        } else {
            return Result.fail("操作失败");
        }
    }
}
