package com.ztt.server.controller;

import com.ztt.server.pojo.Department;
import com.ztt.server.service.DepartmentService;
import com.ztt.server.vo.R;
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
@RequestMapping("/system/basic/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "查询部门列表")
    @GetMapping("/list")
    public R list(){
        return R.success(departmentService.getDepartmentList());
    }

    @ApiOperation(value = "添加部门")
    @PostMapping("/add")
    public R add(@RequestBody Department department) {
        return R.success(departmentService.addDepartment(department));
    }

    @ApiOperation(value = "删除部门")
    @DeleteMapping("/delete/{id}")
    public R delete(@PathVariable Integer id) {
        return R.success(departmentService.deleteDepartment(id));
    }

}

