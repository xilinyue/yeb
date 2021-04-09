package com.ztt.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztt.server.pojo.Admin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Bing
 * @since 2021-03-12
 */
public interface AdminMapper extends BaseMapper<Admin> {

    List<Admin> getAllAdmins(@Param(value = "id") Integer id,
                             @Param(value = "name") String name);
}
