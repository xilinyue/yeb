package com.ztt.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ztt.server.mapper.MenuMapper;
import com.ztt.server.pojo.Admin;
import com.ztt.server.pojo.Menu;
import com.ztt.server.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Bing
 * @since 2021-03-12
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    public List<Menu> getMenuListByAdminId() {
        Admin admin = (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer adminId = admin.getId();
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // 从redis获取菜单
        List<Menu> menus = (List<Menu>) valueOperations.get("menu_" + adminId);
        // 如果为空，去数据库获取
        if (CollectionUtils.isEmpty(menus)) {
            menus = menuMapper.getMenuListByAdminId(adminId);
            // 将数据设置到redis中
            valueOperations.set("menu_" + adminId,menus);
        }
        return menus;
    }

    @Override
    public List<Menu> getMenuListWithRole() {
        return menuMapper.getMenuListWithRole();
    }

    @Override
    public List<Menu> getAllMenuList() {
        return menuMapper.getAllMenuList();
    }

}
