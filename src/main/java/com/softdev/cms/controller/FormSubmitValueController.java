package com.softdev.cms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softdev.cms.entity.Form;
import com.softdev.cms.entity.FormSubmit;
import com.softdev.cms.entity.FormSubmitValue;
import com.softdev.cms.entity.User;
import com.softdev.cms.entity.dto.FormSubmitValueDTO;
import com.softdev.cms.entity.dto.QueryParamDTO;
import com.softdev.cms.mapper.FormMapper;
import com.softdev.cms.mapper.FormSubmitMapper;
import com.softdev.cms.mapper.FormSubmitValueMapper;
import com.softdev.cms.mapper.UserMapper;
import com.softdev.cms.service.StorageService;
import com.softdev.cms.util.Result;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

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
    private UserMapper userMapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/save")
    public Result<String> save(@RequestBody FormSubmitValue formSubmitValue) {
        try {
            log.info("formSubmitValue:{}", objectMapper.writeValueAsString(formSubmitValue));
        } catch (Exception e) {
            log.warn("serialize formSubmitValue failed", e);
        }
        FormSubmitValue oldFormSubmitValue = formSubmitValueMapper.selectById(formSubmitValue.getValueId());
        if (oldFormSubmitValue != null) {
            formSubmitValueMapper.updateById(formSubmitValue);
        } else {
            formSubmitValueMapper.insert(formSubmitValue);
        }
        return Result.success("保存成功");
    }

    @PostMapping("/delete")
    public Result<String> delete(@RequestParam Integer id) {
        FormSubmitValue formSubmitValue = formSubmitValueMapper.selectById(id);
        if (formSubmitValue != null) {
            formSubmitValueMapper.deleteById(id);
            return Result.success("删除成功");
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/find")
    public Result<FormSubmitValue> find(@RequestParam Integer id) {
        FormSubmitValue formSubmitValue = formSubmitValueMapper.selectById(id);
        if (formSubmitValue != null) {
            return Result.success(formSubmitValue);
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/list")
    public Result<List<FormSubmitValue>> list(@RequestParam(required = false) String searchParams,
                                               @RequestParam(required = false, defaultValue = "1") int page,
                                               @RequestParam(required = false, defaultValue = "10") int limit) {
        // 简化：FormSubmitValue没有独立的分页查询，返回空
        return Result.success(Collections.emptyList(), 0);
    }

    @GetMapping("/list")
    public ModelAndView listPage(@RequestParam(required = false) Integer formId) {
        return new ModelAndView("cms/formSubmitValue-list", "formId", formId);
    }

    @GetMapping("/display")
    public ModelAndView display(@RequestParam Integer formId,
                                 @RequestParam(required = false) Integer submitId,
                                 HttpSession session) {
        Form form = formMapper.selectById(formId);
        if (submitId == null) {
            submitId = 0;
        }
        String userName = (String) session.getAttribute("userName");
        User user = null;
        if (StringUtils.isNotEmpty(userName)) {
            user = userMapper.selectByUserName(userName);
        }
        // 专项表单检查
        if (submitId == 0 && user != null && form != null && form.getFormType() == 2) {
            // 使用专用查询方法（避免 pageAll 缺少分页参数报错）
            FormSubmit existing = formSubmitMapper.selectByFormIdAndUserId(formId, user.getUserId());
            if (existing != null) {
                return new ModelAndView("cms/error", "msg", "专项表单只能提交一次");
            }
        }
        FormSubmit formSubmit = null;
        if (submitId != 0) {
            formSubmit = formSubmitMapper.selectById(submitId);
        }
        List<FormSubmitValueDTO> itemValueList = formSubmitValueMapper.getFormSubmitValue(formId, submitId);
        String itemValueJson = "[]";
        try {
            itemValueJson = objectMapper.writeValueAsString(itemValueList);
        } catch (Exception e) {
            log.warn("serialize itemValueList failed", e);
        }
        return new ModelAndView("cms/formSubmitValue-display", "formId", formId)
                .addObject("itemValueList", itemValueJson)
                .addObject("form", form)
                .addObject("formSubmit", formSubmit)
                .addObject("submitId", submitId)
                .addObject("loginUser", user);
    }

    @PostMapping("/submit")
    @Transactional(rollbackFor = Exception.class)
    public Result<String> submit(@RequestBody String submitData, HttpSession session) {
        log.info("formSubmitValue:{}", submitData);
        try {
            Map<String, Object> jsonObject = objectMapper.readValue(submitData, Map.class);
            Integer formId = (Integer) jsonObject.get("formId");
            Integer submitId = jsonObject.get("submitId") != null ?
                    Integer.valueOf(jsonObject.get("submitId").toString()) : null;
            String userName = (String) session.getAttribute("userName");
            User user = null;
            if (StringUtils.isNotEmpty(userName)) {
                user = userMapper.selectByUserName(userName);
            }
            Form form = formMapper.selectById(formId);

            if (submitId != null && submitId != 0) {
                FormSubmit formSubmit = formSubmitMapper.selectById(submitId);
                formSubmit.setUpdateTime(new Date());
                if (formSubmit.getStatus() < 1) {
                    formSubmit.setStatus(1);
                }
                formSubmitMapper.updateById(formSubmit);
                for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                    String key = entry.getKey();
                    if ("formId".equals(key) || "userId".equals(key) || "userName".equals(key)
                            || "showName".equals(key) || "file".equals(key) || "submitId".equals(key)) {
                        continue;
                    }
                    Integer itemId = Integer.valueOf(key);
                    String itemValue = entry.getValue() != null ? entry.getValue().toString() : "";
                    // 查询已有的值 - 通过formId+submitId+itemId
                    QueryParamDTO valueDto = new QueryParamDTO();
                    valueDto.setFormId(formId);
                    valueDto.setSubmitId(submitId);
                    List<FormSubmitValue> existingValues = new ArrayList<>(); // 简化查询
                    FormSubmitValue formSubmitValue = existingValues.isEmpty() ?
                            null : existingValues.get(0);
                    if (formSubmitValue != null) {
                        formSubmitValue.setValueText(itemValue);
                        formSubmitValueMapper.updateById(formSubmitValue);
                    } else {
                        formSubmitValue = new FormSubmitValue(formId, formSubmit.getSubmitId(), itemId, itemValue,
                                user != null ? user.getUserId() : null);
                        formSubmitValueMapper.insert(formSubmitValue);
                    }
                }
            } else {
                FormSubmit formSubmit = new FormSubmit(formId,
                        user != null ? user.getUserId() : null,
                        user != null ? user.getUserName() : "");
                formSubmitMapper.insert(formSubmit);
                for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                    String key = entry.getKey();
                    if ("formId".equals(key) || "userId".equals(key) || "userName".equals(key)
                            || "showName".equals(key) || "file".equals(key) || "submitId".equals(key)) {
                        continue;
                    }
                    Integer itemId = Integer.valueOf(key);
                    String itemValue = entry.getValue() != null ? entry.getValue().toString() : "";
                    FormSubmitValue formSubmitValue = new FormSubmitValue(formId, formSubmit.getSubmitId(), itemId, itemValue,
                            user != null ? user.getUserId() : null);
                    formSubmitValueMapper.insert(formSubmitValue);
                }
            }
        } catch (Exception e) {
            log.error("submit form failed", e);
            return Result.fail("提交失败: " + e.getMessage());
        }
        return Result.success("保存成功");
    }

    @GetMapping("/excel/page")
    public ModelAndView excelPage(@RequestParam Integer formId) {
        return new ModelAndView("cms/formSubmitValue-export", "formId", formId);
    }

    @GetMapping("/excel/export")
    public ResponseEntity<Resource> excelExport(@RequestParam(required = false) String searchParams) throws UnsupportedEncodingException {
        log.info("excel-json:{}", searchParams);
        String filename = "export_" + System.currentTimeMillis() + ".xlsx";
        try {
            QueryParamDTO queryParamDTO = new QueryParamDTO();
            if (StringUtils.isNotEmpty(searchParams)) {
                queryParamDTO = objectMapper.readValue(searchParams, QueryParamDTO.class);
            }
            List<Map<String, Object>> formSubmitValueExcelDTOList = formSubmitValueMapper.getFormSubmitValueExcel(queryParamDTO)
                    .stream().map(dto -> {
                        Map<String, Object> map = new LinkedHashMap<>();
                        map.put("submitId", dto.getSubmitId());
                        map.put("itemId", dto.getItemId());
                        map.put("itemType", dto.getItemType());
                        map.put("itemName", dto.getItemName());
                        map.put("userId", dto.getUserId());
                        map.put("userName", dto.getUserName());
                        map.put("showName", dto.getShowName());
                        map.put("valueText", dto.getValueText());
                        return map;
                    }).toList();

            // 使用Apache POI生成Excel
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Export");

                // 标题行
                if (!formSubmitValueExcelDTOList.isEmpty()) {
                    Map<String, Object> firstRow = formSubmitValueExcelDTOList.get(0);
                    Row headerRow = sheet.createRow(0);
                    int colIdx = 0;
                    for (String key : firstRow.keySet()) {
                        headerRow.createCell(colIdx++).setCellValue(key);
                    }

                    // 数据行
                    int rowIdx = 1;
                    for (Map<String, Object> dataRow : formSubmitValueExcelDTOList) {
                        Row row = sheet.createRow(rowIdx++);
                        colIdx = 0;
                        for (Object value : dataRow.values()) {
                            row.createCell(colIdx++).setCellValue(value != null ? value.toString() : "");
                        }
                    }
                }

                // 写入文件
                try (FileOutputStream fos = new FileOutputStream(storageService.getPath().resolve(filename).toFile())) {
                    workbook.write(fos);
                }
            }
        } catch (Exception e) {
            log.error("export excel failed", e);
        }

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + URLEncoder.encode(filename, "UTF-8") + "\"").body(file);
    }
}
