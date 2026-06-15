package com.softdev.cms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softdev.cms.entity.Template;
import com.softdev.cms.mapper.TemplateMapper;
import com.softdev.cms.service.FrontEndService;
import com.softdev.cms.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private TemplateMapper templateMapper;

    @Autowired
    private FrontEndService frontEndService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/save")
    public Result<String> save(@RequestBody Template template) {
        try {
            log.info("template:{}", objectMapper.writeValueAsString(template));
        } catch (Exception e) {
            log.warn("serialize template failed", e);
        }
        Template oldTemplate = templateMapper.selectById(template.getTemplateId());
        if (oldTemplate != null) {
            templateMapper.updateById(template);
        } else {
            // 检查名字重复
            List<Template> existing = templateMapper.selectByPage(template.getPage());
            boolean nameDuplicate = existing.stream()
                    .anyMatch(t -> template.getTemplateName().equals(t.getTemplateName()));
            if (nameDuplicate) {
                return Result.fail("保存失败，名字重复");
            }
            templateMapper.insert(template);
        }
        frontEndService.refreshTemplateMap(template.getPage());
        return Result.success("保存成功");
    }

    @PostMapping("/delete")
    public Result<String> delete(@RequestParam Integer id) {
        Template template = templateMapper.selectById(id);
        if (template != null) {
            templateMapper.deleteById(id);
            return Result.success("删除成功");
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/find")
    public Result<Template> find(@RequestParam Integer id) {
        Template template = templateMapper.selectById(id);
        if (template != null) {
            return Result.success(template);
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/list")
    public Result<List<Template>> list(@RequestParam(required = false) String searchParams,
                                        @RequestParam(required = false, defaultValue = "1") int page,
                                        @RequestParam(required = false, defaultValue = "10") int limit) {
        Template query = new Template();
        if (StringUtils.isNotEmpty(searchParams)) {
            try {
                query = objectMapper.readValue(searchParams, Template.class);
            } catch (Exception e) {
                log.warn("parse searchParams failed", e);
            }
        }
        List<Template> itemList = templateMapper.pageAll(query, (page - 1) * limit, limit);
        int itemCount = templateMapper.countAll(query);
        return Result.success(itemList, itemCount);
    }

    @GetMapping("/list")
    public ModelAndView listPage() {
        return new ModelAndView("cms/template-list");
    }

    @GetMapping("/edit")
    public ModelAndView editPage(@RequestParam Integer id) {
        Template template = templateMapper.selectById(id);
        return new ModelAndView("cms/template-edit", "template", template);
    }
}
