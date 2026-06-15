package com.softdev.cms.controller;

import com.softdev.cms.entity.Tag;
import com.softdev.cms.mapper.TagMapper;
import com.softdev.cms.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagMapper tagMapper;

    @PostMapping("/save")
    public Result<String> save(@RequestBody Tag tag) {
        Tag existing = tagMapper.selectByName(tag.getTagName(), tag.getTagType());
        if (existing != null) {
            return Result.fail("标签已存在");
        }
        tagMapper.insert(tag);
        return Result.success("保存成功");
    }

    @PostMapping("/delete")
    public Result<String> delete(@RequestParam Integer id) {
        Tag tag = tagMapper.selectById(id);
        if (tag != null) {
            tagMapper.deleteById(id);
            return Result.success("删除成功");
        } else {
            return Result.fail("没有找到该对象");
        }
    }

    @PostMapping("/list")
    public Result<List<Tag>> list(@RequestParam(required = false) String tagType) {
        List<Tag> list = tagMapper.selectAll(tagType);
        return Result.success(list);
    }

    @GetMapping("/list")
    public ModelAndView listPage() {
        return new ModelAndView("cms/tag-list");
    }
}
