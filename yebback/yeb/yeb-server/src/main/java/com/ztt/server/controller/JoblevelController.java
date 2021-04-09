package com.ztt.server.controller;


import com.ztt.server.pojo.Joblevel;
import com.ztt.server.service.JoblevelService;
import com.ztt.server.vo.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Bing
 * @since 2021-03-12
 */
@RestController
@RequestMapping("/system/basic/jobLevel")
public class JoblevelController {
    
    @Autowired
    private JoblevelService joblevelService;

    @ApiOperation(value = "获取职称列表")
    @GetMapping("/listJoblevels")
    public R listJoblevels() {
        return R.success(joblevelService.list());
    }

    @ApiOperation(value = "根据id获取职称")
    @GetMapping("/getJoblevelById/{id}")
    public R getJoblevelById(@PathVariable Integer id) {
        return R.success(joblevelService.getById(id));
    }

    @ApiOperation(value = "添加职称")
    @PostMapping("/addJoblevel")
    public R addJoblevel(@RequestBody Joblevel joblevel) {
        joblevel.setCreateDate(LocalDateTime.now());
        return R.success(joblevelService.save(joblevel));
    }

    @ApiOperation(value = "更新职称信息")
    @PutMapping("/updateJoblevel")
    public R updateJoblevel(@RequestBody Joblevel joblevel) {
        return R.success(joblevelService.updateById(joblevel));
    }

    @ApiOperation(value = "根据id删除职称")
    @DeleteMapping("/deleteJoblevelById/{id}")
    public R deleteJoblevelById(@PathVariable Integer id) {
        return R.success(joblevelService.removeById(id));
    }

    @ApiOperation(value = "根据id列表删除职称")
    @DeleteMapping("/deleteJoblevelByIds/")
    public R deleteJoblevelByIds(@RequestParam Integer[] ids) {
        return R.success(joblevelService.removeByIds(Arrays.asList(ids)));
    }
}

