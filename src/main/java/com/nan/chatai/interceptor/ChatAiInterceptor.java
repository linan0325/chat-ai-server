package com.nan.chatai.interceptor;

import com.alibaba.fastjson.JSON;
import com.nan.chatai.utils.RE;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ChatAiInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断请求是否微信端，不拦截。
        String userAgent = request.getHeader("User-Agent");
        if(userAgent.contains("MicroMessenger")) return true;

        HttpSession session = request.getSession();
        String userToken= (String) session.getAttribute("USER-CHAT-AI-ID");

        if("".equals(userToken) || null ==userToken){
            request.getRequestDispatcher("/chat-ai/").forward(request,response);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //System.out.println("用户已登入。。。。。。");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
