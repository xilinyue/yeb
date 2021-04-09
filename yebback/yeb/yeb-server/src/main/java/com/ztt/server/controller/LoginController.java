package com.ztt.server.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.ztt.server.pojo.Admin;
import com.ztt.server.service.AdminService;
import com.ztt.server.vo.AdminLoginVO;
import com.ztt.server.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.Principal;

/**
 * @author LiuSu
 * @create 2021/3/12 17:04
 */
@RestController
@Api(tags = "LoginController",value = "User Login Properties")
public class LoginController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private DefaultKaptcha defaultKaptcha;

    @ApiOperation(value = "用户登录接口，返回token")
    @PostMapping("/login")
    public R login(@RequestBody @Valid AdminLoginVO adminLoginVO, HttpServletRequest request) {
        return adminService.login(adminLoginVO,request);
    }

    @ApiOperation(value = "用户退出登录")
    @PostMapping("/logout")
    public R logout(){
        // 更新Security登录用户对象信息
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(null,null,null);
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        return R.success("退出登录成功");
    }

    @ApiOperation(value = "用户获取登录信息")
    @GetMapping("/getOwn")
    public R getOwn(Principal principal){
        if (principal == null) {
            return R.success(null);
        }
        String username = principal.getName();
        Admin admin = adminService.getAdminByUserName(username);
        admin.setPassword(null);
        admin.setRoles(adminService.getRolesByAdminId(admin.getId()));
        return R.success(admin);
    }

    @ApiOperation(value = "验证码")
    @GetMapping(value = "/captcha",produces = "image/jpeg")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response){
        //定义response输出类型为image/jpeg
        response.setDateHeader("Expires",0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        //---------------------------生成验证码----------------------
        //获取验证码文本内容
        String text = defaultKaptcha.createText();
        System.out.println("验证码:  " + text);
        //将验证码放到session中
        request.getSession().setAttribute("captcha",text);
        //根据文本内容创建图形验证码
        BufferedImage image = defaultKaptcha.createImage(text);
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            //输出流输出图片,格式为jpg
            ImageIO.write(image,"jpg",outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //------------------------end-----------------
    }
}
