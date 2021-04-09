package com.ztt.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ztt.server.config.AdminUtil;
import com.ztt.server.config.security.common.JwtTokenUtil;
import com.ztt.server.mapper.AdminMapper;
import com.ztt.server.mapper.AdminRoleMapper;
import com.ztt.server.mapper.RoleMapper;
import com.ztt.server.pojo.Admin;
import com.ztt.server.pojo.AdminRole;
import com.ztt.server.pojo.Role;
import com.ztt.server.service.AdminService;
import com.ztt.server.vo.AdminLoginVO;
import com.ztt.server.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Bing
 * @since 2021-03-12
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    public R login(AdminLoginVO adminLoginVO, HttpServletRequest request) {
        // 判断验证是否正确
        String code = (String) request.getSession().getAttribute("captcha");
        if (code!=null && !code.equals(adminLoginVO.getCode())) {
            return R.error("验证码错误！");
        }
        // 判断用户是否存在以及密码是否正确
        UserDetails userDetails = userDetailsService.loadUserByUsername(adminLoginVO.getUsername());
        if (userDetails == null || !passwordEncoder.matches(adminLoginVO.getPassword(),userDetails.getPassword())){
            return R.error("用户名或者密码错误！");
        }
        if (!userDetails.isEnabled()) {
            return R.error("账号被禁用，请联系管理员！");
        }

        // 更新Security登录用户对象信息
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 生成Token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        map.put("tokenHead",tokenHead);
        return R.success(map);
    }

    @Override
    public Admin getAdminByUserName(String username) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username",username).eq("enabled",true);
        return adminMapper.selectOne(queryWrapper);
    }

    @Override
    public List<Role> getRolesByAdminId(Integer adminId) {
        return roleMapper.getRolesByAdminId(adminId);
    }

    @Override
    public List<Admin> getAllAdmins(String name) {
        Admin admin = AdminUtil.getLoginAdmin();
        return adminMapper.getAllAdmins(admin.getId(),name);
    }

    @Override
    @Transactional
    public String updateRolesByAdminId(Integer adminId, Integer[] rIds) {
        // 删除已存在的roleId
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId",adminId));
        // 添加
        if (rIds != null && rIds.length > 0) {
            Integer result = adminRoleMapper.addAdminRole(adminId,rIds);
            if (result == rIds.length){
                return "更新成功";
            }
        }
        return "更新失败";
    }
}
