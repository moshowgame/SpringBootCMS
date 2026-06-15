package com.softdev.cms.mapper;

import com.softdev.cms.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper {

    Tag selectById(@Param("tagId") Integer tagId);

    Tag selectByName(@Param("tagName") String tagName, @Param("tagType") String tagType);

    List<Tag> selectAll(@Param("tagType") String tagType);

    int insert(Tag tag);

    int deleteById(@Param("tagId") Integer tagId);
}
