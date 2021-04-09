package com.ztt.server.config.security.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ztt.server.vo.R;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author LiuSu
 * @create 2021/3/12 19:26
 */
@Component
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        R r = R.error("权限不足，请联系管理员");
        r.setCode(403);
        out.write(new ObjectMapper().writeValueAsString(r));
        out.flush();
        out.close();
    }
}
