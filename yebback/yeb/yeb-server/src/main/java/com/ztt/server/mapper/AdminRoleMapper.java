package com.ztt.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztt.server.pojo.AdminRole;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Bing
 * @since 2021-03-12
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    Integer addAdminRole(@Param(value = "adminId") Integer adminId,
                         @Param(value = "rIds") Integer[] rIds);
}
