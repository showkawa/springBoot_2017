package com.kawa.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
public class SPBWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter{
	/**
	* 拦截器的配置规则
	* @param registry
	*/
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    // addPathPatterns 用于添加拦截规则
	    // excludePathPatterns 用户排除拦截
	    registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**")
	   				.excludePathPatterns("/register","/login");
	    super.addInterceptors(registry);
	}
}
