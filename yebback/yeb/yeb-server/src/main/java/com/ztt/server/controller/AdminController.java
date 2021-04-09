package com.ztt.server.controller;

import com.ztt.server.pojo.Admin;
import com.ztt.server.service.AdminService;
import com.ztt.server.service.RoleService;
import com.ztt.server.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Bing
 * @since 2021-03-12
 */
@RestController
@RequestMapping("/system/admin")
@Api(tags = "Admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "查询所有除登陆者之外的管理员")
    @GetMapping("/list")
    public R list(String keyWords) {
        return R.success(adminService.getAllAdmins(keyWords));
    }

    @ApiOperation(value = "更新管理员信息")
    @PutMapping("/update")
    public R update(@RequestBody Admin admin) {
        return R.success(adminService.updateById(admin));
    }

    @ApiOperation(value = "根据id删除管理员")
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable Integer id){
        return R.success(adminService.removeById(id));
    }

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/roleList")
    public R roleList() {
        return R.success(roleService.list());
    }

    @ApiOperation(value = "根据AdminId更新角色")
    @PutMapping("/updateRolesByAdminId")
    public R updateRolesByAdminId(@RequestParam Integer adminId,
                                  @RequestParam Integer[] rIds){
        return R.success(adminService.updateRolesByAdminId(adminId,rIds));
    }

}

