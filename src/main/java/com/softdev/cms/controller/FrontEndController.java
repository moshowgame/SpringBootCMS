package com.softdev.cms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.softdev.cms.entity.Template;
import com.softdev.cms.mapper.TemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 前端页面+模板值控制
 * @author zhengkai.blog.csdn.net
 */
@RestController
@RequestMapping("/page")
public class FrontEndController {

    @Autowired
    TemplateMapper templateMapper;

    @GetMapping("index")
    public ModelAndView webIndex(){
        List<Template> templateList = templateMapper.selectList(new QueryWrapper<Template>().eq("page","home"));
        Map<String,Object> templateMap = new LinkedHashMap<>(templateList.size()+1);
        templateList.stream().forEach(m->{
            templateMap.put(m.getTemplateName(),m.getTemplateValue());
        });
        return new ModelAndView("frontend/index").addObject("templateMap",templateMap);
    }

}
