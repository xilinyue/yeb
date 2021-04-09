package com.ztt.server.config;

import com.ztt.server.pojo.Admin;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author LiuSu
 * @create 2021/4/9 18:40
 */
public class AdminUtil {

    public static Admin getLoginAdmin() {
        return (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
