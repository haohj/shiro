package com.hao.shiro.controller.interceptor;

import com.hao.shiro.po.ActiveUser;
import com.hao.shiro.utils.ResourcesUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.List;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //得到请求的url
        String url = request.getRequestURI();

        //判断是否是公开 地址
        //实际开发中需要公开 地址配置在配置文件中
        //从配置中取匿名访问url
        List<String> list = ResourcesUtil.gekeyList("anonymousURL");
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String open_url = (String) it.next();
            if (url.indexOf(open_url) >= 0) {
                //如果是公开 地址则放行
                return true;
            }
        }

        //判断用户身份在session中是否存在
        HttpSession session = request.getSession();
        ActiveUser activeUser = (ActiveUser) session.getAttribute("activeUser");
        if (activeUser != null) {
            return true;
        }

        //执行到这里拦截，跳转到登陆页面，用户进行身份认证
        request.getRequestDispatcher("WEB-INF/jsp/login.jsp").forward(request, response);

        //如果返回false表示拦截不继续执行handler，如果返回true表示放行
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
