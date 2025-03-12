package org.example.carrent.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.carrent.pojo.Result;
import org.example.carrent.utils.JsonUtils;
import org.example.carrent.utils.JwtUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("------------------Auth Interceptor----------------------------");
        System.out.println(request.getRequestURL());
        String method = request.getMethod();
        // 所有 options的请求就放行
        if ("options".equalsIgnoreCase(method)) {
            return false;
        }
        // 获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.length() > 0 && JwtUtils.verifyToken(token)) {
            String strId = JwtUtils.getAudience(token);
            System.out.println(strId);
            Long userId = Long.parseLong(strId);
            request.setAttribute("userId", userId);
            // 放行
            return true;
        }
        //  给前端一个响应
        Result result = new Result(555, "令牌无效，请登录 ！", null);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        // 将pojo对象转换为 json字符串，输出到浏览器
        writer.write(JsonUtils.objectToJson(result));
        // 清空输出
        writer.flush();
        // 拦截请求
        return false;
    }
}
