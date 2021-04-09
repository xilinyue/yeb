package com.ztt.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ztt.server.pojo.Menu;
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
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> getMenuListByAdminId(@Param("id") Integer id);


    List<Menu> getMenuListWithRole();

    List<Menu> getAllMenuList();

}
