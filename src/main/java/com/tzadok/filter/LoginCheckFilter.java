package com.tzadok.filter;

import com.alibaba.fastjson.JSON;
import com.tzadok.common.BaseContext;
import com.tzadok.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Tzadok
 * @date 2022/8/23 21:10:45
 * @project reggie_take_out
 * @description: 检查用户是否登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1.获取本次的url
        String requestURI = request.getRequestURI();

        log.info("拦截请求：{}",requestURI);

        //定义不需要请求的路径
        String[] urls = new String[]{
            "/employee/login",
            "/employee/logout",
            "/backend/**",
            "/front/**",
            "/user/sendMsg",
            "/user/login",
            "/doc.html",
            "/webjars/**",
            "/swagger/resources",
            "/v2/api-docs"
        };

        //2.判断本次请求是否需要被处理
        boolean check = check(urls, requestURI);
        //3.不需要直接放行
        if (check){
            filterChain.doFilter(request,response);
            return;
        }
        //4-1.判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee") != null){

            //获取登录用户id
            Long empId = (Long) request.getSession().getAttribute("employee");

            BaseContext.setThreadLocal(empId);

            filterChain.doFilter(request,response);
            return;
        }

        //4-2.判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user") != null){

            //获取登录用户id
            Long userId = (Long) request.getSession().getAttribute("user");

            BaseContext.setThreadLocal(userId);

            filterChain.doFilter(request,response);
            return;
        }
        //5.如果未登录则直接返回未登录结果,通过输出流向客户端输出数据
        response.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));
        return;
    }

    /**
     * 路径匹配，检查本次是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls,String requestURI){
        for (String url : urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
