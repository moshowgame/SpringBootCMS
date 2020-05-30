package com.softdev.cms.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.softdev.cms.entity.Form;
import com.softdev.cms.entity.FormSubmit;
import com.softdev.cms.entity.FormSubmitValue;
import com.softdev.cms.entity.User;
import com.softdev.cms.entity.dto.QueryParamDTO;
import com.softdev.cms.entity.dto.FormSubmitValueDTO;
import com.softdev.cms.entity.dto.FormSubmitValueExcelDTO;
import com.softdev.cms.mapper.FormSubmitMapper;
import com.softdev.cms.mapper.FormSubmitValueMapper;
import com.softdev.cms.mapper.FormMapper;
import com.softdev.cms.mapper.UserMapper;
import com.softdev.cms.service.StorageService;
import com.softdev.cms.util.JwtTokenUtil;
import com.softdev.cms.util.ReturnT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @description form_submit_value
 * @author zhengkai.blog.csdn.net
 * @date 2020-03-06 23:33:27
 */
@Slf4j
@RestController
@RequestMapping("/formSubmitValue")
public class FormSubmitValueController {

    @Autowired
    private FormSubmitValueMapper formSubmitValueMapper;
    @Autowired
    private FormSubmitMapper formSubmitMapper;
    @Autowired
    private FormMapper formMapper;
    @Autowired
    private StorageService storageService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserMapper userMapper;

    /**
     * 新增或编辑
     */
    @PostMapping("/save")
    public Object save(@RequestBody FormSubmitValue formSubmitValue){
        log.info("formSubmitValue:"+JSON.toJSONString(formSubmitValue));
        FormSubmitValue oldFormSubmitValue = formSubmitValueMapper.selectOne(new QueryWrapper<FormSubmitValue>().eq("valueId",formSubmitValue.getValueId()));
        if(oldFormSubmitValue!=null){
            formSubmitValueMapper.updateById(formSubmitValue);
        }else{
           /* if(formSubmitValueMapper.selectOne(new QueryWrapper<FormSubmitValue>().eq("formSubmitValue_name",formSubmitValue.getFormSubmitValueName()))!=null){
                return ReturnT.ERROR("保存失败，名字重复");
            }*/
            formSubmitValueMapper.insert(formSubmitValue);
        }
        return ReturnT.SUCCESS();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public Object delete(int id){
        FormSubmitValue formSubmitValue = formSubmitValueMapper.selectOne(new QueryWrapper<FormSubmitValue>().eq("valueId",id));
        if(formSubmitValue!=null){
            formSubmitValueMapper.deleteById(id);
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
        FormSubmitValue formSubmitValue = formSubmitValueMapper.selectOne(new QueryWrapper<FormSubmitValue>().eq("valueId",id));
        if(formSubmitValue!=null){
            return ReturnT.SUCCESS(formSubmitValue);
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
        Page<FormSubmitValue> buildPage = new Page<FormSubmitValue>(page,limit);
        //条件构造器
        QueryWrapper<FormSubmitValue> queryWrapper = new QueryWrapper<FormSubmitValue>();
        if(StringUtils.isNotEmpty(searchParams)) {
            FormSubmitValue formSubmitValue = JSON.parseObject(searchParams, FormSubmitValue.class);
            //queryWrapper.eq(StringUtils.isNoneEmpty(formSubmitValue.getFormSubmitValueName()), "formSubmitValue_name", formSubmitValue.getFormSubmitValueName());
        }
        //执行分页
        IPage<FormSubmitValue> pageList = formSubmitValueMapper.selectPage(buildPage, queryWrapper);
        //返回结果
        return ReturnT.PAGE(pageList.getRecords(),pageList.getTotal());
    }
    @GetMapping("/list")
    public ModelAndView listPage(Integer formId){
        return new ModelAndView("cms/formSubmitValue-list","formId",formId);
    }
    @GetMapping("/display")
    public ModelAndView display(Integer formId,Integer submitId,String token){
        Form form = formMapper.selectById(formId);
        if(submitId==null){
            submitId=0;
        }
        String userName = jwtTokenUtil.getUsernameFromToken(token);
        User user= userMapper.selectOne(new QueryWrapper<User>().eq("user_name",userName));
        //从FormSubmit点进来，有submitId，直接显示
        //从用户formList点进来，是填写新的，还要判断类型是否为2专项，如果为2，还需要判断是否填写过。
        if(submitId==0&&user!=null&&form!=null&&form.getFormType()==2){
            //状态: 0未通过 1提交 2审核 3完成
            //未通过还可以再提交
            //每学期只能提交一次
            Integer infoCount = formSubmitMapper.selectCount(new QueryWrapper<FormSubmit>().eq("user_id",user.getUserId()).eq("form_id",formId));
            if(infoCount>0){
                return new ModelAndView("cms/error","msg","专项表单只能提交一次");
            }
        }
        List<FormSubmitValueDTO> itemValueList = formSubmitValueMapper.getFormSubmitValue(formId,submitId);
        return new ModelAndView("cms/formSubmitValue-display","formId",formId)
                .addObject("itemValueList",JSON.toJSONString(itemValueList))
                .addObject("form",form)
                .addObject("submitId",submitId)
                .addObject("loginUser",user)
                ;
    }
    /**
     * 新增或编辑
     */
    @PostMapping("/submit")
    @Transactional(rollbackFor = Exception.class)
    public Object submit(@RequestBody String submitData,String token){
        log.info("formSubmitValue:"+submitData);
        JSONObject jsonObject = JSONObject.parseObject(submitData);
        Integer formId = jsonObject.getInteger("formId");
        Integer submitId = jsonObject.getInteger("submitId");
        String userName = jwtTokenUtil.getUsernameFromToken(token);
        User user= userMapper.selectOne(new QueryWrapper<User>().eq("user_name",userName));
        Form form = formMapper.selectById(formId);

        //有submitId，直接更新，否则新增
        if(submitId!=null && submitId!=0){
            FormSubmit formSubmit = formSubmitMapper.selectById(submitId);
            formSubmit.setUpdateTime(new Date());
            if(formSubmit.getStatus()<1){
                formSubmit.setStatus(1);
            }
            formSubmitMapper.updateById(formSubmit);
            for(String key:jsonObject.keySet()){
                if("formId".equals(key)|| "userId".equals(key)|| "userName".equals(key)|| "showName".equals(key)|| "file".equals(key)|| "submitId".equals(key)){

                }else{
                    Integer itemId = Integer.valueOf(key);
                    String itemValue = jsonObject.getString(key);
                    FormSubmitValue formSubmitValue = formSubmitValueMapper.selectOne(new QueryWrapper<FormSubmitValue>().eq("form_id",formId).eq("submit_id",submitId).eq("item_id",itemId));
                    if(formSubmitValue!=null){
                        formSubmitValue.setValueText(itemValue);
                        formSubmitValueMapper.updateById(formSubmitValue);
                    } else{
                        formSubmitValue = new FormSubmitValue(formId,formSubmit.getSubmitId(),itemId,itemValue,user.getUserId());
                        formSubmitValueMapper.insert(formSubmitValue);
                    }
                }
            }
        }else{
            FormSubmit formSubmit = new FormSubmit(formId,user.getUserId(),user.getShowName());
            formSubmitMapper.insert(formSubmit);
            for(String key:jsonObject.keySet()){
                if("formId".equals(key)|| "userId".equals(key)|| "userName".equals(key)|| "showName".equals(key) || "file".equals(key)|| "submitId".equals(key)){

                }else{
                    Integer itemId = Integer.valueOf(key);
                    String itemValue = jsonObject.getString(key);
                    FormSubmitValue formSubmitValue = new FormSubmitValue(formId,formSubmit.getSubmitId(),itemId,itemValue,user.getUserId());
                    formSubmitValueMapper.insert(formSubmitValue);
                }
            }
        }
        return ReturnT.SUCCESS("保存成功");
    }
    @GetMapping("/excel/page")
    public ModelAndView excelPage(Integer formId){
        return new ModelAndView("cms/formSubmitValue-export","formId",formId);
    }
    /**
     * 分页查询
     */
    @GetMapping("/excel/export")
    public ResponseEntity<Resource> excelExport(String searchParams) throws UnsupportedEncodingException {
        log.info("excel-json:"+ JSON.toJSONString(searchParams));
        //条件构造器
        String filename ="export_"+System.currentTimeMillis()+".xlsx";
        QueryWrapper<FormSubmitValue> queryWrapper = new QueryWrapper<FormSubmitValue>();
        if(StringUtils.isNotEmpty(searchParams)) {
            QueryParamDTO queryParamDTO = JSON.parseObject(searchParams, QueryParamDTO.class);
            List<FormSubmitValueExcelDTO> formSubmitValueExcelDTOList = formSubmitValueMapper.getFormSubmitValueExcel(queryParamDTO);
            // 通过工具类创建writer
            ExcelWriter writer = ExcelUtil.getWriter(storageService.getPath().resolve(filename).toFile());

            log.info(JSON.toJSONString(formSubmitValueExcelDTOList));
            Integer nowsubmitId=0;
            List<Map<String,Object>> itemList = new ArrayList<>();
            Map<String,Object> itemMap = null;
            Integer columnTotal=0;
            for (int i = 0; i < formSubmitValueExcelDTOList.size(); i++) {
                FormSubmitValueExcelDTO item = formSubmitValueExcelDTOList.get(i);
                if(!nowsubmitId.equals(item.getSubmitId())){
                    nowsubmitId = item.getSubmitId();
                    itemMap = new LinkedHashMap<>();
                    itemList.add(itemMap);
                    itemMap.put("提交ID",item.getSubmitId());
                    itemMap.put("账号",item.getUserName());
                    itemMap.put("姓名",item.getShowName());

                    itemMap.put(item.getItemName(),(item.getItemType().contains("file"))?com.softdev.cms.util.StringUtils.SYSTEM_PATH+"/file/files/" +item.getValueText():item.getValueText());
                    //三元运算符，包含文件字段，转换为http下载地址
                    //itemMap.put(item.getItemName(),(item.getItemType().contains("file"))?"=HYPERLINK(\""+com.softdev.cms.util.StringUtils.SYSTEM_PATH+"/file/files/" +item.getValueText()+"\",\""+item.getValueText()+"\")":item.getValueText());
                }else if(i==formSubmitValueExcelDTOList.size()-1){
                    //三元运算符，包含文件字段，转换为http下载地址
                    //itemMap.put(item.getItemName(),(item.getItemType().contains("file"))?"=HYPERLINK(\""+com.softdev.cms.util.StringUtils.SYSTEM_PATH+"/file/files/" +item.getValueText()+"\",\""+item.getValueText()+"\")":item.getValueText());
                    itemMap.put(item.getItemName(),(item.getItemType().contains("file"))?com.softdev.cms.util.StringUtils.SYSTEM_PATH+"/file/files/" +item.getValueText():item.getValueText());

                    columnTotal=itemMap.keySet().size();
                    //itemList.add(itemMap);
                }else{
                    itemMap.put(item.getItemName(),(item.getItemType().contains("file"))?com.softdev.cms.util.StringUtils.SYSTEM_PATH+"/file/files/" +item.getValueText():item.getValueText());

                    //三元运算符，包含文件字段，转换为http下载地址
                    //itemMap.put(item.getItemName(),(item.getItemType().contains("file"))?"=HYPERLINK(\""+com.softdev.cms.util.StringUtils.SYSTEM_PATH+"/file/files/" +item.getValueText()+"\",\""+item.getValueText()+"\")":item.getValueText());

                }

            }
            // 合并单元格后的标题行，使用默认标题样式
            Form form = formMapper.selectById(queryParamDTO.getFormId());
            writer.merge(columnTotal-1, form.getFormName());

            log.info(JSON.toJSONString(itemList));

            // 一次性写出内容，使用默认样式，强制输出标题
            writer.write(itemList, true);
            // 关闭writer，释放内存
            writer.close();

        }
        //加载文件
        Resource file = storageService.loadAsResource(filename);
        //返回结果
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + URLEncoder.encode(filename, "UTF-8") + "\"").body(file);
    }
}



