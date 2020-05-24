package com.softdev.cms.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.softdev.cms.entity.FormSubmit;
import com.softdev.cms.entity.FormSubmitValue;
import com.softdev.cms.entity.dto.QueryParamDTO;
import com.softdev.cms.mapper.FormSubmitMapper;
import com.softdev.cms.util.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 * @description form_submit
 * @author zhengkai.blog.csdn.net
 * @date 2020-03-06 23:26:05
 */
@Slf4j
@RestController
@RequestMapping("/formSubmit")
public class FormSubmitController {

    @Autowired
    private FormSubmitMapper formSubmitMapper;

    /**
     * 新增或编辑
     */
    @PostMapping("/save")
    public ReturnT save(@RequestBody FormSubmit formSubmit){
        log.info("formSubmit:"+JSON.toJSONString(formSubmit));
        FormSubmit oldFormSubmit = formSubmitMapper.selectOne(new QueryWrapper<FormSubmit>().eq("submit_id",formSubmit.getSubmitId()));
        formSubmit.setUpdateTime(new Date());
        if(oldFormSubmit!=null){
            formSubmitMapper.updateById(formSubmit);
        }else{
            formSubmit.setCreateTime(new Date());
            formSubmitMapper.insert(formSubmit);
        }
        return ReturnT.SUCCESS();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public ReturnT delete(int id){
        FormSubmit formSubmit = formSubmitMapper.selectOne(new QueryWrapper<FormSubmit>().eq("submit_id",id));
        if(formSubmit!=null){
            formSubmitMapper.deleteById(id);
            return ReturnT.SUCCESS();
        }else{
            return ReturnT.ERROR();
        }
    }

    /**
     * 查询
     */
    @PostMapping("/find")
    public ReturnT find(int id){
        FormSubmit formSubmit = formSubmitMapper.selectOne(new QueryWrapper<FormSubmit>().eq("submit_id",id));
        if(formSubmit!=null){
            return ReturnT.SUCCESS(formSubmit);
        }else{
            return ReturnT.ERROR();
        }
    }

    /**
     * 分页查询
     */
    @PostMapping("/list")
    public ReturnT list(String searchParams,
                       @RequestParam(required = false, defaultValue = "0") int page,
                       @RequestParam(required = false, defaultValue = "10") int limit) {
        log.info("searchParams:"+ JSON.toJSONString(searchParams));
        //条件构造器
        QueryWrapper<FormSubmitValue> queryWrapper = new QueryWrapper<FormSubmitValue>();
        QueryParamDTO queryParamDTO = JSON.parseObject(searchParams, QueryParamDTO.class);
        queryParamDTO.setPage((page - 1)* limit);
        queryParamDTO.setLimit(limit);
        //(page - 1) * limit, limit
        List<FormSubmit> itemList = formSubmitMapper.pageAll(queryParamDTO);
        Integer itemCount = formSubmitMapper.countAll(queryParamDTO);
        //返回结果
        return ReturnT.PAGE(itemList,itemCount);
    }
    /**
     * 分页查询
     */
    @PostMapping("/list2")
    public ReturnT list2(String searchParams,
                       @RequestParam(required = false, defaultValue = "0") int page,
                       @RequestParam(required = false, defaultValue = "10") int limit) {
        log.info("page:"+page+"-limit:"+limit+"-json:"+ JSON.toJSONString(searchParams));
        //分页构造器
        Page<FormSubmit> buildPage = new Page<FormSubmit>(page,limit);
        //条件构造器
        QueryWrapper<FormSubmit> queryWrapper = new QueryWrapper<FormSubmit>();
        if(StringUtils.isNotEmpty(searchParams)) {
            FormSubmit formSubmit = JSON.parseObject(searchParams, FormSubmit.class);
            queryWrapper.eq(formSubmit.getUserId()!=null, "user_id", formSubmit.getUserId());
            queryWrapper.eq(formSubmit.getFormId()!=null, "form_id", formSubmit.getFormId());
            queryWrapper.eq(formSubmit.getStatus()!=null, "status", formSubmit.getStatus());
        }
        //执行分页
        IPage<FormSubmit> pageList = formSubmitMapper.selectPage(buildPage, queryWrapper);
        //返回结果
        return ReturnT.PAGE(pageList.getRecords(),Integer.parseInt(pageList.getTotal()+""));
    }
    @GetMapping("/list")
    public ModelAndView listPage(Integer formId){
        return new ModelAndView("cms/formSubmit-list","formId",formId);
    }
    @PostMapping("/audit")
    public ReturnT audit(@RequestBody List<FormSubmit> formSubmitList,Integer status,Integer userId,String userName){
        String msg = "";Integer successCount=0;
        for (FormSubmit formSubmit:formSubmitList){
            if(status==2&&formSubmit.getStatus()==1){
                formSubmit.setStatus(2);
                formSubmit.setAuditTime(new Date());
                formSubmit.setAuditUserId(userId);
                formSubmit.setAuditUserName(userName);
                formSubmitMapper.updateById(formSubmit);
                successCount++;
            }else if(status==0&&formSubmit.getStatus()==1){
                formSubmit.setStatus(0);
                formSubmit.setAuditTime(new Date());
                formSubmit.setAuditUserId(userId);
                formSubmit.setAuditUserName(userName);
                formSubmitMapper.updateById(formSubmit);
                successCount++;
            }else if(status==3&&formSubmit.getStatus()==2){
                formSubmit.setStatus(3);
                formSubmit.setAuditTime(new Date());
                formSubmit.setAuditUserId(userId);
                formSubmit.setAuditUserName(formSubmit.getAuditUserName()+","+userName);
                formSubmitMapper.updateById(formSubmit);
                successCount++;
            }
        }
        return ReturnT.SUCCESS("审核"+((status==0)?"不":"")+"通过 "+successCount+" / "+formSubmitList.size());
    }
}



