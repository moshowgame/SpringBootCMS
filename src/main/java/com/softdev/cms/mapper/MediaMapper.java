package com.softdev.cms.mapper;

import com.softdev.cms.entity.Media;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MediaMapper {

    Media selectById(@Param("mediaId") Integer mediaId);

    List<Media> pageAll(@Param("fileName") String fileName,
                        @Param("mediaType") String mediaType,
                        @Param("uploadUserId") Integer uploadUserId,
                        @Param("page") int page,
                        @Param("limit") int limit);

    int countAll(@Param("fileName") String fileName,
                 @Param("mediaType") String mediaType,
                 @Param("uploadUserId") Integer uploadUserId);

    int insert(Media media);

    int updateById(Media media);

    int deleteById(@Param("mediaId") Integer mediaId);
}
