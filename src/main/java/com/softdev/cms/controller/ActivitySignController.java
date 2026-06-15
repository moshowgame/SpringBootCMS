package com.softdev.cms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softdev.cms.entity.Activity;
import com.softdev.cms.entity.ActivitySign;
import com.softdev.cms.entity.User;
import com.softdev.cms.entity.dto.QueryParamDTO;
import com.softdev.cms.mapper.ActivityMapper;
import com.softdev.cms.mapper.ActivitySignMapper;
import com.softdev.cms.mapper.UserMapper;
import com.softdev.cms.util.Result;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/activitySign")
public class ActivitySignController {

    @Autowired
    private ActivitySignMapper activitySignMapper;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private UserMapper userMapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/save")
    public Result<String> save(@RequestBody ActivitySign activitySign) {
        try {
            log.info("activitySign:{}", objectMapper.writeValueAsString(activitySign));
        } catch (Exception e) {
            log.warn("serialize activitySign failed", e);
        }
        ActivitySign oldActivitySign = activitySignMapper.selectById(activitySign.getSignId());
        if (oldActivitySign != null) {
            activitySignMapper.updateById(activitySign);
        } else {
            // 判断是否已经签到 - 通过activityId和userName查询
            QueryParamDTO checkDto = new QueryParamDTO();
            checkDto.setActivityId(activitySign.getActivityId());
            checkDto.setUserName(activitySign.getUserName());
            List<ActivitySign> existing = activitySignMapper.pageAll(checkDto);
            if (!existing.isEmpty()) {
                return Result.fail("已签到，请勿重复签到");
            }
            // 判断是否在时间范围内
            Activity activity = activityMapper.selectById(activitySign.getActivityId());
            if (activity == null) {
                return Result.fail("活动不存在");
            }
            Date now = new Date();
            if (now.before(activity.getStartTime()) || now.after(activity.getEndTime())) {
                return Result.fail("不在活动时间范围，不能签到");
            }
            activitySign.setSignTime(new Date());
            activitySignMapper.insert(activitySign);
        }
        return Result.success("签到成功");
    }

    @PostMapping("/delete")
    public Result<String> delete(@RequestParam Integer id) {
        ActivitySign activitySign = activitySignMapper.selectById(id);
        if (activitySign != null) {
            activitySignMapper.deleteById(id);
            return Result.success("删除成功");
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/find")
    public Result<ActivitySign> find(@RequestParam(required = false) Integer id,
                                      @RequestParam(required = false) Integer activityId,
                                      @RequestParam(required = false) String userName) {
        ActivitySign activitySign = null;
        if (id != null) {
            activitySign = activitySignMapper.selectById(id);
        }
        if (activityId != null && userName != null) {
            QueryParamDTO dto = new QueryParamDTO();
            dto.setActivityId(activityId);
            dto.setUserName(userName);
            List<ActivitySign> list = activitySignMapper.pageAll(dto);
            if (!list.isEmpty()) {
                activitySign = list.get(0);
            }
        }
        if (activitySign != null) {
            return Result.success(activitySign);
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/list")
    public Result<List<ActivitySign>> list(@RequestParam(required = false) String searchParams,
                                            @RequestParam(required = false, defaultValue = "1") int page,
                                            @RequestParam(required = false, defaultValue = "10") int limit) {
        QueryParamDTO queryParamDTO = new QueryParamDTO();
        if (StringUtils.isNotEmpty(searchParams)) {
            try {
                queryParamDTO = objectMapper.readValue(searchParams, QueryParamDTO.class);
            } catch (Exception e) {
                log.warn("parse searchParams failed", e);
            }
        }
        queryParamDTO.setPageLimit(page, limit);
        List<ActivitySign> itemList = activitySignMapper.pageAll(queryParamDTO);
        int itemCount = activitySignMapper.countAll(queryParamDTO);
        return Result.success(itemList, itemCount);
    }

    @GetMapping("/list")
    public ModelAndView listPage(@RequestParam(required = false) Integer activityId) {
        return new ModelAndView("cms/activitySign-list", "activityId", activityId);
    }

    @GetMapping("/edit")
    public ModelAndView editPage(@RequestParam(required = false) Integer id,
                                  @RequestParam(required = false) Integer activityId) {
        ActivitySign activitySign = null;
        if (id != null) {
            activitySign = activitySignMapper.selectById(id);
        }
        return new ModelAndView("cms/activitySign-edit", "activitySign", activitySign)
                .addObject("activityId", activityId);
    }

    @GetMapping("/display")
    public ModelAndView display(@RequestParam Integer activityId, HttpSession session) {
        String userName = (String) session.getAttribute("userName");
        User user = null;
        if (StringUtils.isNotEmpty(userName)) {
            user = userMapper.selectByUserName(userName);
        }
        Activity activity = activityMapper.selectById(activityId);
        return new ModelAndView("cms/activitySign-display", "activity", activity)
                .addObject("activityId", activityId)
                .addObject("loginUser", user);
    }

    @GetMapping("/signResult")
    public ModelAndView signResult(@RequestParam(defaultValue = "false") boolean isSuccess,
                                    @RequestParam(required = false) String msg) {
        if (isSuccess) {
            return new ModelAndView("cms/success", "msg", (msg != null) ? msg : "签到成功!");
        } else {
            return new ModelAndView("cms/error", "msg", (msg != null) ? msg : "已签到，请勿重复签到!");
        }
    }

    @GetMapping("/qrcode")
    public ModelAndView qrcode(@RequestParam Integer activityId) {
        Activity activity = activityMapper.selectById(activityId);
        return new ModelAndView("cms/activitySign-qrcode", "activity", activity);
    }
}
