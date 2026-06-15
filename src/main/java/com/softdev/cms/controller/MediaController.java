package com.softdev.cms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softdev.cms.entity.Media;
import com.softdev.cms.mapper.MediaMapper;
import com.softdev.cms.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private MediaMapper mediaMapper;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/save")
    public Result<String> save(@RequestBody Media media) {
        try {
            log.info("media:{}", objectMapper.writeValueAsString(media));
        } catch (Exception e) {
            log.warn("serialize media failed", e);
        }
        if (media.getMediaId() != null) {
            Media old = mediaMapper.selectById(media.getMediaId());
            if (old != null) {
                mediaMapper.updateById(media);
                return Result.success("保存成功");
            }
        }
        mediaMapper.insert(media);
        return Result.success("保存成功");
    }

    @PostMapping("/delete")
    public Result<String> delete(@RequestParam Integer id) {
        Media media = mediaMapper.selectById(id);
        if (media != null) {
            mediaMapper.deleteById(id);
            return Result.success("删除成功");
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/find")
    public Result<Media> find(@RequestParam Integer id) {
        Media media = mediaMapper.selectById(id);
        if (media != null) {
            return Result.success(media);
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/list")
    public Result<List<Media>> list(@RequestParam(required = false) String fileName,
                                    @RequestParam(required = false) String mediaType,
                                    @RequestParam(required = false) Integer uploadUserId,
                                    @RequestParam(required = false, defaultValue = "1") int page,
                                    @RequestParam(required = false, defaultValue = "10") int limit) {
        int offset = (page - 1) * limit;
        List<Media> itemList = mediaMapper.pageAll(fileName, mediaType, uploadUserId, offset, limit);
        int itemTotal = mediaMapper.countAll(fileName, mediaType, uploadUserId);
        return Result.success(itemList, itemTotal);
    }

    @GetMapping("/list")
    public ModelAndView listPage() {
        return new ModelAndView("cms/media-list");
    }

    @GetMapping("/edit")
    public ModelAndView editPage(@RequestParam(required = false) Integer id) {
        Media media = null;
        if (id != null) {
            media = mediaMapper.selectById(id);
        }
        return new ModelAndView("cms/media-edit", "media", media);
    }
}
