package com.ztt.server.config.security.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztt.server.vo.R;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 当未登录或者token失效时访问接口时，自定义的返回接口
 * @author LiuSu
 * @create 2021/3/12 19:21
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        R r = R.error("未登录，请登录");
        r.setCode(401);
        out.write(new ObjectMapper().writeValueAsString(r));
        out.flush();
        out.close();
    }
}
