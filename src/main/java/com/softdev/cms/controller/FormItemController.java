package com.softdev.cms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softdev.cms.entity.FormItem;
import com.softdev.cms.mapper.FormItemMapper;
import com.softdev.cms.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/formItem")
public class FormItemController {

    @Autowired
    private FormItemMapper formItemMapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/save")
    public Result<String> save(@RequestBody FormItem formItem) {
        try {
            log.info("formItem:{}", objectMapper.writeValueAsString(formItem));
        } catch (Exception e) {
            log.warn("serialize formItem failed", e);
        }
        FormItem oldFormItem = formItemMapper.selectById(formItem.getItemId());
        if (oldFormItem != null) {
            formItemMapper.updateById(formItem);
        } else {
            // 检查同一表单下名字重复
            List<FormItem> existing = formItemMapper.selectByFormId(formItem.getFormId());
            boolean nameDuplicate = existing.stream()
                    .anyMatch(i -> formItem.getItemName().equals(i.getItemName()));
            if (nameDuplicate) {
                return Result.fail("保存失败，名字重复");
            }
            formItemMapper.insert(formItem);
        }
        return Result.success("保存成功");
    }

    @PostMapping("/delete")
    public Result<String> delete(@RequestParam Integer id) {
        FormItem formItem = formItemMapper.selectById(id);
        if (formItem != null) {
            formItemMapper.deleteById(id);
            return Result.success("删除成功");
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/find")
    public Result<FormItem> find(@RequestParam Integer id) {
        FormItem formItem = formItemMapper.selectById(id);
        if (formItem != null) {
            return Result.success(formItem);
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/list")
    public Result<List<FormItem>> list(@RequestParam(required = false) String searchParams,
                                        @RequestParam(required = false, defaultValue = "1") int page,
                                        @RequestParam(required = false, defaultValue = "10") int limit) {
        FormItem query = new FormItem();
        if (StringUtils.isNotEmpty(searchParams)) {
            try {
                query = objectMapper.readValue(searchParams, FormItem.class);
            } catch (Exception e) {
                log.warn("parse searchParams failed", e);
            }
        }
        // FormItem通过formId查询全部，内存分页
        final FormItem finalQuery = query;
        List<FormItem> allItems;
        if (finalQuery.getFormId() != null) {
            allItems = formItemMapper.selectByFormId(finalQuery.getFormId());
        } else {
            allItems = formItemMapper.selectByFormId(0); // 返回空
        }
        if (StringUtils.isNotEmpty(finalQuery.getItemName())) {
            allItems = allItems.stream()
                    .filter(i -> i.getItemName().contains(finalQuery.getItemName()))
                    .collect(java.util.stream.Collectors.toList());
        }
        int total = allItems.size();
        int fromIndex = Math.min((page - 1) * limit, total);
        int toIndex = Math.min(fromIndex + limit, total);
        return Result.success(allItems.subList(fromIndex, toIndex), total);
    }

    @GetMapping("/list")
    public ModelAndView listPage(@RequestParam(required = false) Integer formId) {
        return new ModelAndView("cms/formItem-list", "formId", formId);
    }

    @GetMapping("/edit")
    public ModelAndView editPage(@RequestParam Integer id, @RequestParam(required = false) Integer formId) {
        FormItem formItem = formItemMapper.selectById(id);
        return new ModelAndView("cms/formItem-edit", "formItem", formItem).addObject("formId", formId);
    }
}
