package com.ztt.server.exception;

import com.ztt.server.vo.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常配置
 * @author LiuSu
 * @create 2021/3/17 18:50
 */
@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(SQLException.class)
    public R mySqlException(SQLException exception) {
        if (exception instanceof SQLIntegrityConstraintViolationException) {
            return R.error("该数据有关联数据，操作失败");
        }
        return R.error("数据库异常，操作失败");
    }
}
