package com.nan.chatai.configs;

import com.nan.chatai.interceptor.ChatAiInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebInterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ChatAiInterceptor()).addPathPatterns("/**")
               // .excludePathPatterns("/chat-ai/**")
                .excludePathPatterns("/chat-ai/register-page")
                .excludePathPatterns("/chat-ai/user-login")
                .excludePathPatterns("/chat-ai/user-register")
//                .excludePathPatterns("/chat-ai/weChat-user-register")
//                .excludePathPatterns("/chat-ai/weChat-user-login")
//                .excludePathPatterns("/chat-ai/weChat-user-logon")
                  .excludePathPatterns("/static/**")
                  .excludePathPatterns("/app-images/**");
    }
}
