package com.softdev.cms.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.softdev.cms.entity.FormItem;
import com.softdev.cms.mapper.FormItemMapper;
import com.softdev.cms.util.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @description form_item
 * @author zhengkai.blog.csdn.net
 * @date 2020-03-06 23:29:45
 */
@Slf4j
@RestController
@RequestMapping("/formItem")
public class FormItemController {

    @Autowired
    private FormItemMapper formItemMapper;

    /**
     * 新增或编辑
     */
    @PostMapping("/save")
    public Object save(@RequestBody FormItem formItem){
        log.info("formItem:"+JSON.toJSONString(formItem));
        FormItem oldFormItem = formItemMapper.selectOne(new QueryWrapper<FormItem>().eq("item_id",formItem.getItemId()));
        if(oldFormItem!=null){
            formItemMapper.updateById(formItem);
        }else{
            if(formItemMapper.selectOne(new QueryWrapper<FormItem>().eq("form_id",formItem.getFormId()).eq("Item_name",formItem.getItemName()))!=null){
                return ReturnT.ERROR("保存失败，名字重复");
            }
            formItemMapper.insert(formItem);
        }
        return ReturnT.SUCCESS();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public Object delete(int id){
        FormItem formItem = formItemMapper.selectOne(new QueryWrapper<FormItem>().eq("item_id",id));
        if(formItem!=null){
            formItemMapper.deleteById(id);
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
        FormItem formItem = formItemMapper.selectOne(new QueryWrapper<FormItem>().eq("item_id",id));
        if(formItem!=null){
            return ReturnT.SUCCESS(formItem);
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
        Page<FormItem> buildPage = new Page<FormItem>(page,limit);
        //条件构造器
        QueryWrapper<FormItem> queryWrapper = new QueryWrapper<FormItem>();
        if(StringUtils.isNotEmpty(searchParams)) {
            FormItem formItem = JSON.parseObject(searchParams, FormItem.class);
            queryWrapper.eq(formItem.getFormId()!=null, "form_id", formItem.getFormId());
            queryWrapper.eq(StringUtils.isNotBlank(formItem.getItemName()), "item_name", formItem.getItemName());
        }
        //执行分页
        IPage<FormItem> pageList = formItemMapper.selectPage(buildPage, queryWrapper);
        //返回结果
        return ReturnT.PAGE(pageList.getRecords(),pageList.getTotal());
    }
    @GetMapping("/list")
    public ModelAndView listPage(Integer formId){
        return new ModelAndView("cms/formItem-list","formId",formId);
    }
    @GetMapping("/edit")
    public ModelAndView editPage(int id,Integer formId){
        FormItem formItem = formItemMapper.selectOne(new QueryWrapper<FormItem>().eq("item_id",id));
        return new ModelAndView("cms/formItem-edit","formItem",formItem).addObject("formId",formId);
    }
}



