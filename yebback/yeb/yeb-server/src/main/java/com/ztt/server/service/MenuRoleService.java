package com.ztt.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ztt.server.pojo.MenuRole;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Bing
 * @since 2021-03-12
 */
public interface MenuRoleService extends IService<MenuRole> {
    /**
     * 通过roleid 更新 menus
     * @param mIds
     * @param rId
     * @return
     */
    String updateMenusByRoleId(Integer[] mIds,Integer rId);
}
