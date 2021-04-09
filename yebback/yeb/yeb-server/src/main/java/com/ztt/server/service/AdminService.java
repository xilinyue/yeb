package com.ztt.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ztt.server.pojo.Admin;
import com.ztt.server.pojo.Role;
import com.ztt.server.vo.AdminLoginVO;
import com.ztt.server.vo.R;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Bing
 * @since 2021-03-12
 */
public interface AdminService extends IService<Admin> {

    R login(AdminLoginVO adminLoginVO, HttpServletRequest request);

    Admin getAdminByUserName(String username);

    List<Role> getRolesByAdminId(Integer adminId);

    List<Admin> getAllAdmins(String keyWords);

    String updateRolesByAdminId(Integer adminId, Integer[] rIds);
}
