package com.ztt.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ztt.server.pojo.MenuRole;
import com.ztt.server.pojo.Role;
import com.ztt.server.service.MenuRoleService;
import com.ztt.server.service.MenuService;
import com.ztt.server.service.RoleService;
import com.ztt.server.vo.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 权限控制
 * @author LiuSu
 * @create 2021/3/18 9:03
 */
@RestController
@RequestMapping(value = "/system/basic/permission")
public class PermissionController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuRoleService menuRoleService;

    @ApiOperation(value = "角色列表获取")
    @GetMapping("/roleList")
    public R roleList() {
        return R.success(roleService.list());
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("/addRole")
    public R addRole(@RequestBody Role role) {
        if (role.getName().startsWith("ROLE_")) {
            return R.success(roleService.save(role));
        }
        role.setName("ROLE_" + role.getName());
        return R.success(roleService.save(role));
    }

    @ApiOperation(value = "根据id删除角色")
    @DeleteMapping("/deleteRole/{id}")
    public R deleteRole(@PathVariable Integer id) {
        return R.success(roleService.removeById(id));
    }

    @ApiOperation(value = "查询所有菜单列表")
    @GetMapping(value = "/getAllMenuList")
    public R getAllMenuList() {
        return R.success(menuService.getAllMenuList());
    }

    @ApiOperation(value = "根据角色id查询菜单列表ids")
    @GetMapping(value = "/getMenuIdsByRoleId/{id}")
    public R getMenusByRoleId(@PathVariable Integer id) {
        QueryWrapper<MenuRole> queryWrapper = new QueryWrapper();
        queryWrapper.eq("rid",id);
        List<MenuRole> menuRoleList = menuRoleService.list(queryWrapper);
        return R.success(menuRoleList.stream().map(MenuRole::getMid).collect(Collectors.toList()));
    }

    @ApiOperation(value = "通过角色id更新角色menu")
    @PutMapping(value = "/updateMenusByRoleId")
    public R updateMenusByRoleId(@RequestParam Integer[] mIds,
                                 @RequestParam Integer rId) {
        return R.success(menuRoleService.updateMenusByRoleId(mIds,rId));
    }
}
