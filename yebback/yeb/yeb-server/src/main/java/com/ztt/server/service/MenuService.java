package com.ztt.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ztt.server.pojo.Menu;

import java.util.*;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Bing
 * @since 2021-03-12
 */
public interface MenuService extends IService<Menu> {

    /**
     * 通过管理员id获取菜单列表
     * @param
     * @return
     */
    public List<Menu> getMenuListByAdminId();

    /**
     * 通过角色查询菜单列表
     * @return
     */
    public List<Menu> getMenuListWithRole();

    /**
     * 查询所有菜单聊表
     * @return
     */
    public List<Menu> getAllMenuList();

}
