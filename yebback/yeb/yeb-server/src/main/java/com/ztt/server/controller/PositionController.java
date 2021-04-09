package com.ztt.server.controller;


import com.ztt.server.pojo.Position;
import com.ztt.server.service.PositionService;
import com.ztt.server.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javafx.geometry.Pos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * <p>
 *  职位管理
 * </p>
 *
 * @author Bing
 * @since 2021-03-12
 */
@RestController
@RequestMapping("/system/basic/position")
@Api(tags = "Position Properties")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @ApiOperation(value = "获取职位列表")
    @GetMapping("/listPositions")
    public R listPositions() {
        return R.success(positionService.list());
    }

    @ApiOperation(value = "根据id获取职位")
    @GetMapping("/getPositionById/{id}")
    public R getPositionById(@PathVariable Integer id) {
        return R.success(positionService.getById(id));
    }

    @ApiOperation(value = "添加职位")
    @PostMapping("/addPosition")
    public R addPosition(@RequestBody Position position) {
        position.setCreateDate(LocalDateTime.now());
        return R.success(positionService.save(position));
    }

    @ApiOperation(value = "更新职位信息")
    @PutMapping("/updatePosition")
    public R updatePosition(@RequestBody Position position) {
        return R.success(positionService.updateById(position));
    }

    @ApiOperation(value = "根据id删除职位")
    @DeleteMapping("/deletePositionById/{id}")
    public R deletePositionById(@PathVariable Integer id) {
        return R.success(positionService.removeById(id));
    }

    @ApiOperation(value = "根据id列表删除职位")
    @DeleteMapping("/deletePositionByIds/")
    public R deletePositionByIds(@RequestParam Integer[] ids) {
        return R.success(positionService.removeByIds(Arrays.asList(ids)));
    }
}

