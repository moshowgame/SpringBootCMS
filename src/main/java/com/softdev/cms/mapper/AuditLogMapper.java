package com.softdev.cms.mapper;

import com.softdev.cms.entity.AuditLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AuditLogMapper {

    int insert(AuditLog auditLog);

    List<AuditLog> pageAll(@Param("userId") Integer userId,
                           @Param("userName") String userName,
                           @Param("action") String action,
                           @Param("module") String module,
                           @Param("page") int page,
                           @Param("limit") int limit);

    int countAll(@Param("userId") Integer userId,
                 @Param("userName") String userName,
                 @Param("action") String action,
                 @Param("module") String module);
}
