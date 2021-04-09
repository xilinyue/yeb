package com.ztt.server.controller;


import com.ztt.server.service.MenuService;
import com.ztt.server.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Bing
 * @since 2021-03-12
 */
@RestController
@RequestMapping("/menu")
@Api(tags = "Menu Properties")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/getByAdminId")
    @ApiOperation(value = "通过管理员id获取菜单")
    public R getMenuListByAdminId(){
        return R.success(menuService.getMenuListByAdminId());
    }

}

