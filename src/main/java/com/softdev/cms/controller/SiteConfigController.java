package com.softdev.cms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softdev.cms.entity.SiteConfig;
import com.softdev.cms.mapper.SiteConfigMapper;
import com.softdev.cms.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/siteConfig")
public class SiteConfigController {

    @Autowired
    private SiteConfigMapper siteConfigMapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/save")
    public Result<String> save(@RequestBody SiteConfig siteConfig) {
        try {
            log.info("siteConfig:{}", objectMapper.writeValueAsString(siteConfig));
        } catch (Exception e) {
            log.warn("serialize siteConfig failed", e);
        }
        if (siteConfig.getConfigId() != null) {
            SiteConfig old = siteConfigMapper.selectById(siteConfig.getConfigId());
            if (old != null) {
                siteConfigMapper.updateById(siteConfig);
                return Result.success("保存成功");
            }
        }
        siteConfigMapper.insert(siteConfig);
        return Result.success("保存成功");
    }

    @PostMapping("/delete")
    public Result<String> delete(@RequestParam Integer id) {
        SiteConfig siteConfig = siteConfigMapper.selectById(id);
        if (siteConfig != null) {
            siteConfigMapper.deleteById(id);
            return Result.success("删除成功");
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/findByKey")
    public Result<SiteConfig> findByKey(@RequestParam String configKey) {
        SiteConfig siteConfig = siteConfigMapper.selectByKey(configKey);
        if (siteConfig != null) {
            return Result.success(siteConfig);
        } else {
            return Result.fail("没有找到该配置");
        }
    }

    @PostMapping("/findByGroup")
    public Result<List<SiteConfig>> findByGroup(@RequestParam String configGroup) {
        List<SiteConfig> list = siteConfigMapper.selectByGroup(configGroup);
        return Result.success(list);
    }

    @PostMapping("/list")
    public Result<List<SiteConfig>> list() {
        List<SiteConfig> list = siteConfigMapper.selectAll();
        return Result.success(list);
    }

    @GetMapping("/list")
    public ModelAndView listPage() {
        return new ModelAndView("cms/site-config-list");
    }

    @GetMapping("/edit")
    public ModelAndView editPage(@RequestParam(required = false) Integer id) {
        SiteConfig siteConfig = null;
        if (id != null) {
            siteConfig = siteConfigMapper.selectById(id);
        }
        return new ModelAndView("cms/site-config-edit", "siteConfig", siteConfig);
    }
}
