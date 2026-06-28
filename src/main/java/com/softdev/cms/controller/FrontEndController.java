package com.softdev.cms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softdev.cms.entity.*;
import com.softdev.cms.entity.dto.FormSubmitValueDTO;
import com.softdev.cms.entity.dto.QueryParamDTO;
import com.softdev.cms.mapper.*;
import com.softdev.cms.service.FrontEndService;
import com.softdev.cms.util.AttackDetectionUtil;
import com.softdev.cms.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/page")
@Slf4j
public class FrontEndController {

    @Autowired
    FrontEndService frontEndService;

    @Autowired
    TemplateMapper templateMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    ActivityMapper activityMapper;

    @Autowired
    ActivitySignMapper activitySignMapper;

    @Autowired
    FormMapper formMapper;

    @Autowired
    FormSubmitMapper formSubmitMapper;

    @Autowired
    FormSubmitValueMapper formSubmitValueMapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("index")
    public ModelAndView indexPage() {
        return new ModelAndView("frontend/index")
                .addObject("channelList", frontEndService.getChannelList(1))
                .addObject("latestArticles", frontEndService.getLatestArticles(6))
                .addObject("templateMap", frontEndService.getTemplateMap("index"));
    }

    @GetMapping("/channel/{channelId}")
    public ModelAndView channelPage(@PathVariable Integer channelId) {
        return new ModelAndView("/frontend/channel")
                .addObject("channelId", channelId)
                .addObject("channel", frontEndService.getChannelById(channelId))
                .addObject("channelList", frontEndService.getChannelList(1))
                .addObject("templateMap", frontEndService.getTemplateMap("channel"));
    }

    @GetMapping("article/{articleId}")
    public ModelAndView articlePage(@PathVariable String articleId) {
        // 增加浏览量
        try {
            articleMapper.incrementViewCount(Integer.valueOf(articleId));
        } catch (NumberFormatException e) {
            log.warn("Invalid articleId format: {}", articleId, e);
        }
        return new ModelAndView("frontend/article")
                .addObject("channelList", frontEndService.getChannelList(1))
                .addObject("templateMap", frontEndService.getTemplateMap("article"))
                .addObject("article", frontEndService.getArticle(articleId))
                .addObject("articleCtx", frontEndService.getArticleContext(articleId));
    }

    @PostMapping("article/list")
    public Result<List<Article>> getArticleList(@RequestBody QueryParamDTO queryParamDTO) {
        try {
            log.info("article list query:{}", objectMapper.writeValueAsString(queryParamDTO));
        } catch (Exception e) {
            log.warn("serialize queryParamDTO failed", e);
        }
        // 攻击检测：searchParam/title/keyword 等用户输入字段
        if (queryParamDTO != null) {
            java.util.Map<String, String> checkParams = new java.util.LinkedHashMap<>();
            if (queryParamDTO.getSearchParam() != null) {
                checkParams.put("searchParam", queryParamDTO.getSearchParam());
            }
            if (queryParamDTO.getTitle() != null) {
                checkParams.put("title", queryParamDTO.getTitle());
            }
            AttackDetectionUtil.AttackResult attack = AttackDetectionUtil.detect(checkParams);
            if (attack.isDetected()) {
                AttackDetectionUtil.logAttack(attack);
                return Result.fail(attack.getType().getDescription() + " 攻击已拦截：输入包含 '" + attack.getMatchedKeyword() + "' 特征");
            }
        }
        queryParamDTO.setPageLimit();
        List<Article> itemList = articleMapper.pageAll(queryParamDTO);
        int itemTotal = articleMapper.countAll(queryParamDTO);
        return Result.success(itemList, itemTotal);
    }

    @GetMapping("/search")
    public ModelAndView getArticleSearchPage() {
        return new ModelAndView("/frontend/search")
                .addObject("channelList", frontEndService.getChannelList(1))
                .addObject("templateMap", frontEndService.getTemplateMap("search"));
    }

    @GetMapping("/activity/search")
    public ModelAndView getActivitySearchPage() {
        return new ModelAndView("/frontend/activity-search")
                .addObject("channelList", frontEndService.getChannelList(1))
                .addObject("templateMap", frontEndService.getTemplateMap("search"));
    }

    @GetMapping("/activitySign/{activityId}")
    public ModelAndView getActivitySignPage(@PathVariable Integer activityId) {
        return new ModelAndView("/frontend/activity-sign")
                .addObject("channelList", frontEndService.getChannelList(1))
                .addObject("activity", activityMapper.selectById(activityId))
                .addObject("templateMap", frontEndService.getTemplateMap("search"));
    }

    @GetMapping("/form/search")
    public ModelAndView getFormSearchPage() {
        return new ModelAndView("/frontend/form-search")
                .addObject("channelList", frontEndService.getChannelList(1))
                .addObject("templateMap", frontEndService.getTemplateMap("search"));
    }

    @GetMapping("/formSubmit/{formId}")
    public ModelAndView getFormSubmitPage(@PathVariable Integer formId) {
        List<FormSubmitValueDTO> itemValueList = formSubmitValueMapper.getFormSubmitValue(formId, 0);
        String itemValueJson = "[]";
        try {
            itemValueJson = objectMapper.writeValueAsString(itemValueList);
        } catch (Exception e) {
            log.warn("serialize itemValueList failed", e);
        }
        return new ModelAndView("/frontend/form-sign")
                .addObject("channelList", frontEndService.getChannelList(1))
                .addObject("itemValueList", itemValueJson)
                .addObject("form", formMapper.selectById(formId))
                .addObject("templateMap", frontEndService.getTemplateMap("search"));
    }

    @PostMapping("/activitySign/save")
    public Result<String> activitySignSave(@RequestBody ActivitySign activitySign) {
        try {
            log.info("activitySign:{}", objectMapper.writeValueAsString(activitySign));
        } catch (Exception e) {
            log.warn("serialize activitySign failed", e);
        }
        Activity activity = activityMapper.selectById(activitySign.getActivityId());
        if (activity == null || activity.getStatus() < 1) {
            return Result.fail("活动无效");
        }
        // 判断是否已经签到（前台匿名签到，按 activityId + phone 去重，避免 pageAll 缺少分页参数报错）
        if (StringUtils.isNotBlank(activitySign.getPhone())) {
            ActivitySign existing = activitySignMapper.selectByActivityAndPhone(
                    activitySign.getActivityId(), activitySign.getPhone());
            if (existing != null) {
                return Result.fail("该手机号已签到，请勿重复签到");
            }
        }
        // 判断是否在时间范围内
        Date now = new Date();
        if (now.before(activity.getStartTime()) || now.after(activity.getEndTime())) {
            return Result.fail("不在活动时间范围，不能签到");
        }
        activitySign.setSignTime(new Date());
        activitySignMapper.insert(activitySign);
        return Result.success("签到成功");
    }

    @PostMapping("activity/list")
    public Result<List<Activity>> getActivityList(@RequestBody QueryParamDTO queryParamDTO) {
        try {
            log.info("activity list query:{}", objectMapper.writeValueAsString(queryParamDTO));
        } catch (Exception e) {
            log.warn("serialize queryParamDTO failed", e);
        }
        queryParamDTO.setStatus(1);
        queryParamDTO.setPageLimit();
        List<Activity> itemList = activityMapper.pageAll(queryParamDTO);
        int itemTotal = activityMapper.countAll(queryParamDTO);
        return Result.success(itemList, itemTotal);
    }

    @PostMapping("form/list")
    public Result<List<Form>> getFormList(@RequestBody QueryParamDTO queryParamDTO) {
        try {
            log.info("form list query:{}", objectMapper.writeValueAsString(queryParamDTO));
        } catch (Exception e) {
            log.warn("serialize queryParamDTO failed", e);
        }
        queryParamDTO.setStatus(1);
        queryParamDTO.setPageLimit();
        List<Form> itemList = formMapper.pageAll(queryParamDTO);
        int itemTotal = formMapper.countAll(queryParamDTO);
        return Result.success(itemList, itemTotal);
    }

    @PostMapping("/formSubmit/save")
    @Transactional(rollbackFor = Exception.class)
    public Result<String> formSubmitSave(@RequestBody String submitData) {
        log.info("formSubmitValue:{}", submitData);
        try {
            List<Map<String, String>> jsonArray = objectMapper.readValue(submitData,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
            Map<String, String> baseInfoMap = new HashMap<>();
            for (Map<String, String> item : jsonArray) {
                baseInfoMap.put(item.get("name"), item.get("value"));
            }
            Integer formId = Integer.valueOf(baseInfoMap.get("formId"));
            String phone = baseInfoMap.get("phone");
            String company = baseInfoMap.get("company");
            String showName = baseInfoMap.get("showName");
            Form form = formMapper.selectById(formId);

            // 根据手机号和表单来判断重复（使用专用方法，避免 pageAll 缺少分页参数报错）
            FormSubmit formSubmit = null;
            if (StringUtils.isNotBlank(phone)) {
                formSubmit = formSubmitMapper.selectByFormIdAndPhone(formId, phone);
            }

            String msg;
            if (formSubmit != null) {
                formSubmit.setUpdateTime(new Date());
                if (formSubmit.getStatus() < 1) {
                    formSubmit.setStatus(1);
                }
                formSubmit.setCompany(company);
                formSubmit.setShowName(showName);
                formSubmitMapper.updateById(formSubmit);
                msg = "已更新该手机号绑定的提交信息，编号:" + formSubmit.getSubmitId();
            } else {
                formSubmit = new FormSubmit(formId, showName, phone, company);
                // 显式设置状态为 1（待审核），避免依赖 DB 默认值
                formSubmit.setStatus(1);
                formSubmitMapper.insert(formSubmit);
                msg = "提交成功，编号:" + formSubmit.getSubmitId();
            }

            // 处理子项
            for (Map.Entry<String, String> entry : baseInfoMap.entrySet()) {
                String key = entry.getKey();
                if ("formId".equals(key) || "userId".equals(key) || "userName".equals(key)
                        || "showName".equals(key) || "file".equals(key) || "submitId".equals(key)
                        || "phone".equals(key) || "company".equals(key)) {
                    continue;
                }
                Integer itemId = Integer.valueOf(key);
                String itemValue = entry.getValue();
                // 查询已有的值
                FormSubmitValue existingValue = null; // 简化：直接新增
                if (existingValue != null) {
                    existingValue.setValueText(itemValue);
                    formSubmitValueMapper.updateById(existingValue);
                } else {
                    FormSubmitValue formSubmitValue = new FormSubmitValue(formId, formSubmit.getSubmitId(), itemId, itemValue);
                    formSubmitValueMapper.insert(formSubmitValue);
                }
            }
            return Result.success(msg);
        } catch (Exception e) {
            log.error("form submit save failed", e);
            return Result.fail("提交失败: " + e.getMessage());
        }
    }
}
