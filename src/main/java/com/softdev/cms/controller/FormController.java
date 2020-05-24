package com.softdev.cms.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.softdev.cms.entity.Form;
import com.softdev.cms.entity.FormItem;
import com.softdev.cms.entity.dto.FormSubmitValueDTO;
import com.softdev.cms.mapper.FormSubmitMapper;
import com.softdev.cms.mapper.FormItemMapper;
import com.softdev.cms.mapper.FormSubmitValueMapper;
import com.softdev.cms.mapper.FormMapper;
import com.softdev.cms.util.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 * @description form
 * @author zhengkai.blog.csdn.net
 * @date 2020-03-06 23:00:35
 */
@Slf4j
@RestController
@RequestMapping("/form")
public class FormController {

    @Autowired
    private FormMapper formMapper;
    @Autowired
    private FormItemMapper formItemMapper;
    @Autowired
    private FormSubmitMapper formSubmitMapper;
    @Autowired
    private FormSubmitValueMapper formSubmitValueMapper;

    /**
     * 新增或编辑
     */
    @PostMapping("/save")
    @Transactional(rollbackFor = Exception.class)
    public Object save(@RequestBody Form form){
        log.info("form:"+JSON.toJSONString(form));
        Form oldForm = formMapper.selectOne(new QueryWrapper<Form>().eq("form_id",form.getFormId()));
        form.setUpdateTime(new Date());
        if(oldForm!=null){
            formMapper.updateById(form);
        }else{
            if(formMapper.selectOne(new QueryWrapper<Form>().eq("form_name",form.getFormName()))!=null){
                return ReturnT.ERROR("保存失败，名字重复");
            }
            form.setCreateTime(new Date());
            formMapper.insert(form);
            //新增默认的表单项目
            FormItem formItem1 = new FormItem(form.getFormId(),"姓名","input","");
            FormItem formItem2 = new FormItem(form.getFormId(),"联系方式","input","");
            FormItem formItem3 = new FormItem(form.getFormId(),"描述","textarea","");
            FormItem formItem4 = new FormItem(form.getFormId(),"附件上传","fileupload","");
            formItemMapper.insert(formItem1);
            formItemMapper.insert(formItem2);
            formItemMapper.insert(formItem3);
            formItemMapper.insert(formItem4);
        }
        return ReturnT.SUCCESS();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @Transactional(rollbackFor = Exception.class)
    public Object delete(int id){
        Form form = formMapper.selectOne(new QueryWrapper<Form>().eq("form_id",id));
        if(form!=null){
            formMapper.deleteById(id);
            //删除对应的表单项
            formItemMapper.delete(new QueryWrapper<FormItem>().eq("form_id",id));
            return ReturnT.SUCCESS();
        }else{
            return ReturnT.ERROR();
        }
    }

    /**
     * 查询
     */
    @PostMapping("/find")
    public Object find(int id){
        Form form = formMapper.selectOne(new QueryWrapper<Form>().eq("form_id",id));
        if(form!=null){
            return ReturnT.SUCCESS(form);
        }else{
            return ReturnT.ERROR();
        }
    }

    /**
     * 分页查询
     */
    @PostMapping("/list")
    public Object list(String searchParams,
                       @RequestParam(required = false, defaultValue = "0") int page,
                       @RequestParam(required = false, defaultValue = "10") int limit) {
        log.info("page:"+page+"-limit:"+limit+"-json:"+ JSON.toJSONString(searchParams));
        //分页构造器
        Page<Form> buildPage = new Page<Form>(page,limit);
        //条件构造器
        QueryWrapper<Form> queryWrapper = new QueryWrapper<Form>();
        if(StringUtils.isNotEmpty(searchParams)) {
            Form form = JSON.parseObject(searchParams, Form.class);
            queryWrapper.eq(StringUtils.isNoneEmpty(form.getFormName()), "form_name", form.getFormName());
            queryWrapper.eq(form.getCreateUserId()!=null, "create_user_id", form.getCreateUserId());
            queryWrapper.eq(form.getStatus()!=null,"status",form.getStatus());
        }
        //执行分页
        IPage<Form> pageList = formMapper.selectPage(buildPage, queryWrapper);
        //返回结果
        return ReturnT.PAGE(pageList.getRecords(),pageList.getTotal());
    }
    @GetMapping("/list")
    public ModelAndView listPage(){
        return new ModelAndView("cms/form-list");
    }
    @GetMapping("/edit")
    public ModelAndView editPage(int id){
        Form form = formMapper.selectOne(new QueryWrapper<Form>().eq("form_id",id));
        return new ModelAndView("cms/form-edit","form",form);
    }
    @GetMapping("/dashboard")
    public ModelAndView dashboard(){

        ModelAndView modelAndView = new ModelAndView("cms/form-type3dashboard");

        List<FormSubmitValueDTO> formSubmitValueDTOList = formSubmitValueMapper.getSelectCountList();
        modelAndView.addObject("data",JSON.toJSONString(formSubmitValueDTOList));

        return modelAndView;
    }

    /**
     * 发布/暂停
     */
    @PostMapping("/publish")
    public Object publish(int id,Integer status){
        Form form = formMapper.selectOne(new QueryWrapper<Form>().eq("form_id",id));
        if(form!=null){
            form.setUpdateTime(new Date());
            form.setStatus(status);
            formMapper.updateById(form);
            return ReturnT.SUCCESS((status==1)?"表单已发布":"表单已暂停");
        }else if(status.equals(form.getStatus())){
            return ReturnT.ERROR("表单状态不正确");
        }else{
            return ReturnT.ERROR();
        }
    }
}



