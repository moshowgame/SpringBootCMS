package com.softdev.cms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class IndexController {

    /**
     * 首页
     */
    @GetMapping("/")
    public ModelAndView indexPage1(){
        return new ModelAndView("redirect:/page/index");
    }
    @GetMapping("/index")
    public ModelAndView indexPage2(){
        return new ModelAndView("redirect:/page/index");
    }
}
