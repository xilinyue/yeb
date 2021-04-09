package com.ztt.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ztt.server.mapper.MenuRoleMapper;
import com.ztt.server.pojo.MenuRole;
import com.ztt.server.service.MenuRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Bing
 * @since 2021-03-12
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements MenuRoleService {
    @Autowired
    private MenuRoleMapper menuRoleMapper;

    @Override
    @Transactional
    public String updateMenusByRoleId(Integer[] mIds, Integer rId) {
        // 先通过rId删除所有menus
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid",rId));
        // 然后判断mIds是否存在以及是否不为空，则进行存储
        if (mIds != null && mIds.length > 0) {
            Integer result = menuRoleMapper.insertMenusByRoleId(mIds,rId);
            if (result > 0) {
                return "更新成功";
            }else {
                return "更新失败";
            }
        }
        return "更新成功";
    }
}
