package com.ztt.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztt.server.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Bing
 * @since 2021-03-12
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<Role> getRolesByAdminId(@Param(value = "adminId") Integer adminId);
}
