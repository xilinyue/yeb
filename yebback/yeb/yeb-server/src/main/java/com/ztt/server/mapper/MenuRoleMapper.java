package com.ztt.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztt.server.pojo.MenuRole;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Bing
 * @since 2021-03-12
 */
public interface MenuRoleMapper extends BaseMapper<MenuRole> {
    Integer insertMenusByRoleId(@Param(value = "mIds") Integer[] mIds,
                                @Param(value = "rid") Integer rid);
}
