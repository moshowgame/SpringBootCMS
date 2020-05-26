package com.softdev.cms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/page")
public class FrontEndController {


    @GetMapping("index")
    public ModelAndView webIndex(){
        return new ModelAndView("frontend/index");
    }

}
